package com.gespyme.infrastructure.adapters.output.repository.query.field;

import com.gespyme.commons.repository.PredicateBuilder;
import com.gespyme.commons.repository.QueryField;
import com.gespyme.commons.repository.criteria.SearchCriteria;
import com.gespyme.infrastructure.adapters.output.model.entities.QInvoiceDataEntity;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InvoiceSubtotalAmountQueryField implements QueryField {

    private final PredicateBuilder<String> predicateBuilder;

    @Override
    public String getFieldName() {
        return "subtotal_amount";
    }

    @Override
    public void addToQuery(BooleanBuilder booleanBuilder, SearchCriteria searchCriteria) {
        booleanBuilder.and(predicateBuilder.getBooleanBuilder(QInvoiceDataEntity.invoiceDataEntity.subtotalAmount, searchCriteria));
    }
}
