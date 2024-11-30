package com.gespyme.domain.invoiceorder.repository;

import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public interface InvoiceOrderRepository {
  Optional<InvoiceOrder> findById(String id);

  void deleteById(String id);

  InvoiceOrder save(InvoiceOrder entity);

  InvoiceOrder merge(InvoiceOrder newEntity, InvoiceOrder entity);

  byte[] createInvoicePdfFile(InvoiceData invoiceData);

  void saveInvoice(byte[] invoiceFile, String invoiceId);

  void singInvoice(String invoiceId);

  InputStream downloadInvoicePdf(String invoiceId);

  List<InvoiceOrder> findByCriteria(List<SearchCriteria> filters);
}
