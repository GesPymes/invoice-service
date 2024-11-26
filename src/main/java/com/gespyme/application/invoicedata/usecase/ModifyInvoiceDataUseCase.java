package com.gespyme.application.invoicedata.usecase;

import com.gespyme.domain.invoicedata.model.InvoiceData;

public interface ModifyInvoiceDataUseCase {
    InvoiceData modifyInvoiceData(String invoiceDataId, InvoiceData newInvoiceDataData);
}
