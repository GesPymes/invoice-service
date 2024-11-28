package com.gespyme.infrastructure.adapters.output.repository;

import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import com.gespyme.infrastructure.adapters.output.autofirma.AutofirmaService;
import com.gespyme.infrastructure.adapters.output.aws.S3Repository;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceOrderEntity;
import com.gespyme.infrastructure.adapters.output.pdf.PdfModificationService;
import com.gespyme.infrastructure.adapters.output.repository.jpa.InvoiceOrderRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.InvoiceOrderMapper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceOrderRepositoryImpl implements InvoiceOrderRepository {

  // @Value("${aws.keyPath}")
  private String pendingKeyPath = "invoice/pending/";

  // @Value("${aws.keyPath}")
  private String signedKeyPath = "invoice/signed/";

  // @Value("${sign.tempdirectory}")
  private String outputSignedTempDirectory = "target/generated-sources/temp";

  private final InvoiceOrderRepositorySpringJpa invoiceOrderJpaRepository;
  private final PdfModificationService pdfModificationService;
  private final AutofirmaService autofirmaService;
  private final InvoiceOrderMapper mapper;
  private final S3Repository s3Repository;

  @Override
  public Optional<InvoiceOrder> findById(String id) {
    return invoiceOrderJpaRepository.findById(id).map(mapper::map);
  }

  @Override
  public void deleteById(String id) {
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
    File file = new File(key);
    createDirIfNotExist(file);
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

  private void cleanDirectory() {

  }

  private void createDirIfNotExist(File file) {
    if (!file.exists()) {
      if (file.mkdirs()) {
        throw new InternalServerError("Cannot create directory");
      }
    }
  }
}
