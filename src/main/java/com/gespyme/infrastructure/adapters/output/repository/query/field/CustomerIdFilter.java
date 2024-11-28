package com.gespyme.infrastructure.adapters.output.repository.query.field;

import com.gespyme.commons.model.customer.CustomerModelApi;
import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.input.rest.customer.CustomerApiRestCallService;
import com.gespyme.infrastructure.adapters.output.model.entities.QInvoiceDataEntity;
import com.querydsl.core.BooleanBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerIdFilter implements QueryField {
  private final PredicateBuilder<String> predicateBuilder;
  private final CustomerApiRestCallService customerApiRestCallService;

  @Override
  public String getFieldName() {
    return "customer_name";
  }

  @Override
  public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
    List<CustomerModelApi> customerResponses =
        customerApiRestCallService.getCustomers((String) searchCriteria.getValue());
    List<String> customers = new ArrayList<>();
    customerResponses.stream()
        .forEach(customerModelApi -> customers.add(customerModelApi.getCustomerId()));
    searchCriteria.setValue(customers);
    booleanBuilder.and(
        predicateBuilder.getBooleanBuilder(
            QInvoiceDataEntity.invoiceDataEntity.customerId, searchCriteria));
  }
}
