package com.gespyme.domain.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import com.gespyme.domain.invoiceorder.model.InvoiceOrderFilter;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class InvoiceOrderStatusFilter implements FieldFilter<InvoiceOrderFilter> {
  @Override
  public boolean apply(InvoiceOrderFilter invoiceOrderFilter) {
    return Objects.nonNull(invoiceOrderFilter.getStatus());
  }

  @Override
  public void addSearchCriteria(
      InvoiceOrderFilter invoiceOrderFilter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("status")
            .operation(SearchOperation.EQUAL)
            .value(invoiceOrderFilter.getStatus())
            .build());
  }
}
