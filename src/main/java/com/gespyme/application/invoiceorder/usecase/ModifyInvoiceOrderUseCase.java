package com.gespyme.application.invoiceorder.usecase;

import com.gespyme.domain.invoiceorder.model.InvoiceOrder;

public interface ModifyInvoiceOrderUseCase {
  InvoiceOrder modifyInvoiceOrder(String invoiceDataId, String invoiceOrderId, InvoiceOrder newInvoiceOrder);
}
