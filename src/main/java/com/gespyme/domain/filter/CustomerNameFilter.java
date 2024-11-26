package com.gespyme.domain.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class CustomerNameFilter implements FieldFilter<InvoiceFilter> {

    @Override
    public boolean apply(InvoiceFilter invoiceFilter) {
        return Objects.nonNull(invoiceFilter.getCustomerName());
    }

    @Override
    public void addSearchCriteria(InvoiceFilter invoiceFilter, List<SearchCriteria> searchCriteriaList) {
        searchCriteriaList.add(SearchCriteria.builder().key("customer_name").operation(SearchOperation.IN).value(invoiceFilter.getCustomerName()).build());
    }
}
