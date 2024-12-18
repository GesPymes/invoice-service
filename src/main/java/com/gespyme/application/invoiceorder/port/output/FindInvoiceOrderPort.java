package com.gespyme.application.invoiceorder.port.output;

import com.gespyme.application.invoiceorder.usecase.FindInvoiceOrderUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.model.InvoiceOrderFilter;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindInvoiceOrderPort implements FindInvoiceOrderUseCase {

  private final InvoiceOrderRepository invoiceOrderRepository;
  private final List<FieldFilter<InvoiceOrderFilter>> invoiceOrderFilter;
  private final InvoiceDataRepository invoiceDataRepository;

  @Override
  public List<InvoiceOrder> findInvoiceOrder(String invoiceDataId, InvoiceOrderFilter invoiceFilter) {
    Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceDataId);
    invoiceFilter.setInvoiceDataId(invoiceDataId);
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    List<SearchCriteria> searchCriterias = new ArrayList<>();
    invoiceOrderFilter.stream()
        .filter(f -> f.apply(invoiceFilter))
        .forEach(f -> f.addSearchCriteria(invoiceFilter, searchCriterias));
    return invoiceOrderRepository.findByCriteria(searchCriterias);
  }
}
