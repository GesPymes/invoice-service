package com.gespyme.application.invoicedata.port.output;

import com.gespyme.application.invoicedata.usecase.FindInvoiceDataUseCase;
import com.gespyme.commons.model.filter.FieldFilter;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.domain.filter.InvoiceFilter;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FindInvoiceDataPort implements FindInvoiceDataUseCase {
    private final InvoiceDataRepository invoiceDataRepository;
    private final List<FieldFilter<InvoiceFilter>> invoiceDataFilters;

    public List<InvoiceData> findInvoiceData(InvoiceFilter invoiceFilter) {
        List<SearchCriteria> searchCriterias = new ArrayList<>();
        invoiceDataFilters.stream().filter(f -> f.apply(invoiceFilter)).forEach(f -> f.addSearchCriteria(invoiceFilter, searchCriterias));
        List<InvoiceData> result = invoiceDataRepository.findByCriteria(searchCriterias);
        return result;
    }
}
