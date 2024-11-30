package com.gespyme.application.invoiceorder.usecase;

import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.model.InvoiceOrderFilter;
import java.util.List;

public interface FindInvoiceOrderUseCase {
  List<InvoiceOrder> findInvoiceOrder(InvoiceOrderFilter invoiceFilter);
}
