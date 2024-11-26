package com.gespyme.domain.filter;

import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class SubtotalAmountInvoiceFilter implements FieldFilter {
    @Override
    public boolean apply(InvoiceFilter invoiceFilter) {
        return Objects.nonNull(invoiceFilter.getSubtotalAmount());
    }

    @Override
    public void addSearchCriteria(InvoiceFilter invoiceFilter, List<SearchCriteria> searchCriteriaList) {
        searchCriteriaList.add(SearchCriteria.builder().key("subtotal_amount").operation(SearchOperation.EQUAL).value(invoiceFilter.getSubtotalAmount()).build());
    }
}
