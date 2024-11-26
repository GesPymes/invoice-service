package com.gespyme.domain.filter;

import com.gespyme.commons.repository.criteria.SearchCriteria;

import java.util.List;


public interface FieldFilter {

    boolean apply(InvoiceFilter invoiceFilter);

    void addSearchCriteria(InvoiceFilter invoiceFilter, List<SearchCriteria> searchCriteriaList);
}