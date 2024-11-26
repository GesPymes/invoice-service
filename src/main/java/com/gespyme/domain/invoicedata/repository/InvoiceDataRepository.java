package com.gespyme.domain.invoicedata.repository;

import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.invoicedata.model.InvoiceData;

import java.util.List;
import java.util.Optional;

public interface InvoiceDataRepository {
    List<InvoiceData> findByCriteria(List<SearchCriteria> filters);

    Optional<InvoiceData> findById(String id);

    void deleteById(String id);

    InvoiceData save(InvoiceData entity);

    InvoiceData merge(InvoiceData newEntity, InvoiceData entity);
}
