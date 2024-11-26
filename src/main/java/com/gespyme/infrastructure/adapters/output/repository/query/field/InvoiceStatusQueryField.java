package com.gespyme.infrastructure.adapters.output.repository.query.field;

import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.output.model.entities.QInvoiceOrderEntity;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceStatusQueryField implements QueryField {
    private final PredicateBuilder<String> predicateBuilder;

    @Override
    public String getFieldName() {
        return "status";
    }

    @Override
    public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
        booleanBuilder.and(predicateBuilder.getBooleanBuilder(QInvoiceOrderEntity.invoiceOrderEntity.status, searchCriteria));
    }
}
