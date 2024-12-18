package com.gespyme.domain.filter;

import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.commons.repository.criteria.SearchOperation;
import com.gespyme.domain.invoiceorder.model.InvoiceOrderFilter;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class InvoiceDataIdFilter implements FieldFilter<InvoiceOrderFilter> {
  @Override
  public boolean apply(InvoiceOrderFilter invoiceOrderFilter) {
    return true;
  }

  @Override
  public void addSearchCriteria(
      InvoiceOrderFilter invoiceOrderFilter, List<SearchCriteria> searchCriteriaList) {
    searchCriteriaList.add(
        SearchCriteria.builder()
            .key("invoice_data_id")
            .operation(SearchOperation.EQUAL)
            .value(invoiceOrderFilter.getInvoiceDataId())
            .build());
  }
}
