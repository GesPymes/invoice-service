package com.gespyme.application.invoicedata.usecase;

import com.gespyme.domain.invoicedata.model.InvoiceData;

public interface FindInvoiceDataByIdUseCase {
  InvoiceData getInvoiceDataById(String invoiceDataId);
}
