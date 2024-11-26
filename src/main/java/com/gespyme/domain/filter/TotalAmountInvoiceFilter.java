package com.gespyme.domain.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class TotalAmountInvoiceFilter implements FieldFilter<InvoiceFilter> {
    @Override
    public boolean apply(InvoiceFilter invoiceFilter) {
        return Objects.nonNull(invoiceFilter.getTotalAmount());
    }

    @Override
    public void addSearchCriteria(InvoiceFilter invoiceFilter, List<SearchCriteria> searchCriteriaList) {
        searchCriteriaList.add(SearchCriteria.builder().key("total_amount").operation(SearchOperation.EQUAL).value(invoiceFilter.getTotalAmount()).build());
    }
}
