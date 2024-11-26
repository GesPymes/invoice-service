package com.gespyme.application.invoiceorder.usecase;

import com.gespyme.domain.invoiceorder.model.InvoiceOrder;

public interface FindInvoiceOrderByIdUseCase {
    InvoiceOrder getInvoiceOrderById(String invoiceOrderId);
}
