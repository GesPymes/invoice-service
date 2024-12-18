package com.gespyme.infrastructure.mapper;

import com.gespyme.commons.model.invoice.InvoiceOrderFilterModelApi;
import com.gespyme.commons.model.invoice.InvoiceOrderModelApi;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.model.InvoiceOrderFilter;
import com.gespyme.infrastructure.adapters.output.model.entities.InvoiceOrderEntity;
import java.util.List;

import org.mapstruct.*;

@Mapper(
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface InvoiceOrderMapper {

  InvoiceOrder map(InvoiceOrderModelApi invoiceOrderModelApi);

  InvoiceOrderFilter map(InvoiceOrderFilterModelApi invoiceOrderFilterModelApi);

  @Mapping(target = "invoiceDataId", source = "invoiceOrder.invoiceData.invoiceDataId")
  InvoiceOrderModelApi map(InvoiceOrder invoiceOrder);

  List<InvoiceOrderModelApi> mapToList(List<InvoiceOrder> invoiceOrder);

  InvoiceOrder map(InvoiceOrderEntity invoiceOrderEntity);

  List<InvoiceOrder> map(List<InvoiceOrderEntity> invoiceOrderEntity);

  InvoiceOrderEntity mapToEntity(InvoiceOrder invoiceOrder);

  InvoiceOrder merge(InvoiceOrder newInvoiceOrder, @MappingTarget InvoiceOrder invoiceOrder);
}
