package com.gespyme.domain.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class TaxRateInvoiceFilter implements FieldFilter<InvoiceFilter> {

  @Override
  public boolean apply(InvoiceFilter invoiceFilter) {
    return Objects.nonNull(invoiceFilter.getTaxRate());
  }

  @Override
  public void addSearchCriteria(
      InvoiceFilter invoiceFilter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("tax_rate")
            .operation(SearchOperation.EQUAL)
            .value(invoiceFilter.getTaxRate())
            .build());
  }
}
