package com.gespyme.infrastructure.adapters.output.repository;

import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.model.InvoiceStatus;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import com.gespyme.infrastructure.adapters.output.autofirma.AutofirmaService;
import com.gespyme.infrastructure.adapters.output.aws.S3Repository;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceOrderEntity;
import com.gespyme.infrastructure.adapters.output.pdf.PdfModificationService;
import com.gespyme.infrastructure.adapters.output.repository.jpa.InvoiceOrderRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.InvoiceOrderMapper;
import com.querydsl.core.BooleanBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InvoiceOrderRepositoryImpl implements InvoiceOrderRepository {

  @Value("${aws.s3.pendingkeypath}")
  private String pendingKeyPath;

  @Value("${aws.s3.signedkeypath}")
  private String signedKeyPath;

  @Value("${sign.tempdirectory}")
  private String outputSignedTempDirectory;

  private final InvoiceOrderRepositorySpringJpa invoiceOrderJpaRepository;
  private final PdfModificationService pdfModificationService;
  private final AutofirmaService autofirmaService;
  private final InvoiceOrderMapper mapper;
  private final S3Repository s3Repository;
  private final Map<String, QueryField> queryFieldMap;

  public InvoiceOrderRepositoryImpl(
      InvoiceOrderRepositorySpringJpa invoiceOrderJpaRepository,
      PdfModificationService pdfModificationService,
      AutofirmaService autofirmaService,
      InvoiceOrderMapper mapper,
      S3Repository s3Repository,
      List<QueryField> queryFields) {
    this.invoiceOrderJpaRepository = invoiceOrderJpaRepository;
    this.pdfModificationService = pdfModificationService;
    this.autofirmaService = autofirmaService;
    this.mapper = mapper;
    this.s3Repository = s3Repository;
    queryFieldMap =
        queryFields.stream()
            .collect(Collectors.toMap(QueryField::getFieldName, queryField -> queryField));
  }

  @Override
  public Optional<InvoiceOrder> findById(String id) {
    return invoiceOrderJpaRepository.findById(id).map(mapper::map);
  }

  @Override
  public void deleteById(String id) throws NotFoundException {
    Optional<InvoiceOrderEntity> invoiceOrder = invoiceOrderJpaRepository.findById(id);
    String status =
        invoiceOrder
            .map(InvoiceOrderEntity::getStatus)
            .orElseThrow(() -> new NotFoundException("id not found"));
    if (InvoiceStatus.SIGNED.toString().equals(status)) {
      s3Repository.deleteInvoiceFromS3(signedKeyPath + "Invoice-" + id + "signed.pdf");
    } else {
      s3Repository.deleteInvoiceFromS3(pendingKeyPath + "Invoice-" + id + ".pdf");
    }
    invoiceOrderJpaRepository.deleteById(id);
  }

  @Override
  public InvoiceOrder save(InvoiceOrder invoiceOrder) {
    InvoiceOrderEntity invoiceDataEntity =
        invoiceOrderJpaRepository.save(mapper.mapToEntity(invoiceOrder));
    return mapper.map(invoiceDataEntity);
  }

  @Override
  public InvoiceOrder merge(InvoiceOrder newInvoiceDataData, InvoiceOrder invoiceData) {
    InvoiceOrder merged = mapper.merge(newInvoiceDataData, invoiceData);
    InvoiceOrderEntity savedEntity = invoiceOrderJpaRepository.save(mapper.mapToEntity(merged));
    return mapper.map(savedEntity);
  }

  @Override
  public byte[] createInvoicePdfFile(InvoiceData invoiceData) {
    return pdfModificationService.createInvoicePdf(invoiceData);
  }

  @Override
  public void saveInvoice(byte[] invoiceFile, String invoiceId) {
    String invoiceName = getInvoiceFileName(invoiceId, false);
    s3Repository.uploadInvoiceToS3(invoiceFile, invoiceName, pendingKeyPath);
  }

  @Override
  public void singInvoice(String invoiceId) {
    String objectKey = pendingKeyPath + getInvoiceFileName(invoiceId, false);
    String key = "/Invoice-" + invoiceId + ".pdf";

    createDirectories();

    File file = new File(outputSignedTempDirectory + "/created" + key);

    try (InputStream invoice = s3Repository.downloadInvoiceFromS3(objectKey);
        FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = invoice.read(buffer)) != -1) {
        fileOutputStream.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      throw new InternalServerError("Error downloading Invoice", e);
    }
    byte[] signedFile = autofirmaService.getSignedFile(invoiceId);

    s3Repository.uploadInvoiceToS3(signedFile, getInvoiceFileName(invoiceId, true), signedKeyPath);
  }

  @Override
  public InputStream downloadInvoicePdf(String invoiceId) {
    return s3Repository.downloadInvoiceFromS3(invoiceId);
  }

  @Override
  public List<InvoiceOrder> findByCriteria(List<SearchCriteria> searchCriteria) {
    BooleanBuilder booleanBuilder = new BooleanBuilder();
    searchCriteria.stream()
        .forEach(sc -> queryFieldMap.get(sc.getKey()).addToQuery(booleanBuilder, sc));
    List<InvoiceOrderEntity> entities =
        (List<InvoiceOrderEntity>) invoiceOrderJpaRepository.findAll(booleanBuilder);
    return mapper.map(entities);
  }

  private String getInvoiceFileName(String invoiceId, boolean signed) {
    StringBuilder sb = new StringBuilder();
    sb.append("Invoice-");
    sb.append(invoiceId);
    if (signed) {
      sb.append("-signed");
    }
    sb.append(".pdf");
    return sb.toString();
  }

  private void createDirectories() {
    File inputDir = new File(outputSignedTempDirectory + "/created");
    File outputDir = new File(outputSignedTempDirectory + "/signed");
    createDirIfNotExist(inputDir);
    createDirIfNotExist(outputDir);
    cleanTempFolder(inputDir);
    cleanTempFolder(outputDir);
  }

  private void cleanTempFolder(File dir) {
    if (dir.exists() && dir.listFiles() != null) {
      Arrays.stream(dir.listFiles()).forEach(File::delete);
    }
  }

  private void createDirIfNotExist(File dir) {
    if (!dir.exists()) {
      if (dir.mkdirs()) {
        throw new InternalServerError("Cannot create directory");
      }
    }
  }
}
