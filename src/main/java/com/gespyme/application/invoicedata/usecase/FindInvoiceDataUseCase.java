package com.gespyme.application.invoicedata.usecase;

import com.gespyme.domain.filter.InvoiceFilter;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import java.util.List;

public interface FindInvoiceDataUseCase {
  List<InvoiceData> findInvoiceData(InvoiceFilter invoiceFilter);
}
