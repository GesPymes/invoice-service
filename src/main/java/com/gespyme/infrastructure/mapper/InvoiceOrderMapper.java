package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.invoice.InvoiceOrderModelApi;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceOrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceOrderMapper {
    InvoiceOrder map(InvoiceOrderModelApi invoiceOrderModelApi);

    InvoiceOrderModelApi map(InvoiceOrder invoiceOrder);

    InvoiceOrder map(InvoiceOrderEntity invoiceOrderEntity);

    InvoiceOrderEntity mapToEntity(InvoiceOrder invoiceOrder);

    InvoiceOrder merge(InvoiceOrder newInvoiceOrder, @MappingTarget InvoiceOrder invoiceOrder);
}
