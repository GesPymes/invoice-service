package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.ModifyInvoiceOrderUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyInvoiceOrderPort implements ModifyInvoiceOrderUseCase {
  private final InvoiceOrderRepository repository;
  private final InvoiceDataRepository invoiceDataRepository;

  @Override
  public InvoiceOrder modifyInvoiceOrder(String invoiceDataId, String invoiceOrderId, InvoiceOrder newInvoiceOrder) {
    Optional<InvoiceOrder> invoiceOrder = repository.findById(invoiceOrderId);
    Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceDataId);
    if (invoiceOrder.isEmpty()) {
      throw new NotFoundException("InvoiceOrder " + invoiceOrderId + " not found");
    }
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    return repository.merge(newInvoiceOrder, invoiceOrder.get());
  }
}
