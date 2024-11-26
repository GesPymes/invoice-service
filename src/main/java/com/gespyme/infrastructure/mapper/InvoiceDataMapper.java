package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.invoice.InvoiceDataFilterModelApi;
import com.gespyme.commons.model.invoice.InvoiceDataModelApi;
import com.gespyme.domain.filter.InvoiceFilter;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceDataEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceDataMapper {
    InvoiceFilter map(InvoiceDataFilterModelApi invoiceDataFilterModelApi);

    InvoiceData map(InvoiceDataModelApi invoiceDataApiModel);

    List<InvoiceDataModelApi> map(List<InvoiceData> invoiceDatas);

    InvoiceDataModelApi map(InvoiceData invoiceDatas);

    InvoiceData map(InvoiceDataEntity invoiceDataEntity);

    InvoiceDataEntity mapToEntity(InvoiceData invoiceData);

    InvoiceData merge(InvoiceData newInvoiceDataData, @MappingTarget InvoiceData invoiceData);
}
