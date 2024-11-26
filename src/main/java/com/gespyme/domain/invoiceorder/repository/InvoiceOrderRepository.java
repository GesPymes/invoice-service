package com.gespyme.domain.invoiceorder.repository;

import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;

import java.util.Optional;

public interface InvoiceOrderRepository {
    Optional<InvoiceOrder> findById(String id);

    void deleteById(String id);

    InvoiceOrder save(InvoiceOrder entity);

    InvoiceOrder merge(InvoiceOrder newEntity, InvoiceOrder entity);

    void createPdf(InvoiceData invoiceData);

    void singInvoice(String invoiceId);
}
