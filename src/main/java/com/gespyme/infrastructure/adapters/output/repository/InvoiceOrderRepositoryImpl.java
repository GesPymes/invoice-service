package com.gespyme.infrastructure.adapters.output.repository;

import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import com.gespyme.infrastructure.adapters.output.autofirma.AutofirmaService;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceOrderEntity;
import com.gespyme.infrastructure.adapters.output.pdf.PdfModificationService;
import com.gespyme.infrastructure.adapters.output.repository.jpa.InvoiceOrderRepositorySpringJpa;
import com.gespyme.infrastructure.mapper.InvoiceOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class InvoiceOrderRepositoryImpl implements InvoiceOrderRepository {
    private final InvoiceOrderRepositorySpringJpa invoiceOrderJpaRepository;
    private final PdfModificationService pdfModificationService;
    private final AutofirmaService autofirmaService;
    private final InvoiceOrderMapper mapper;

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
        InvoiceOrderEntity invoiceDataEntity = invoiceOrderJpaRepository.save(mapper.mapToEntity(invoiceOrder));
        return mapper.map(invoiceDataEntity);
    }

    @Override
    public InvoiceOrder merge(InvoiceOrder newInvoiceDataData, InvoiceOrder invoiceData) {
        InvoiceOrder merged = mapper.merge(newInvoiceDataData, invoiceData);
        InvoiceOrderEntity savedEntity = invoiceOrderJpaRepository.save(mapper.mapToEntity(merged));
        return mapper.map(savedEntity);
    }

    @Override
    public void createPdf(InvoiceData invoiceData) {
        pdfModificationService.createInvoicePdf(invoiceData);
    }

    @Override
    public void singInvoice(String invoiceId) {
        autofirmaService.executeSignCommand(invoiceId);
    }
}
