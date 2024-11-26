package com.gespyme.domain.filter;

import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class InvoiceStatusFilter implements FieldFilter {
    @Override
    public boolean apply(InvoiceFilter invoiceFilter) {
        return Objects.nonNull(invoiceFilter.getStatus());
    }

    @Override
    public void addSearchCriteria(InvoiceFilter invoiceFilter, List<SearchCriteria> searchCriteriaList) {
        searchCriteriaList.add(SearchCriteria.builder().key("status").operation(SearchOperation.EQUAL).value(invoiceFilter.getStatus()).build());
    }
}
