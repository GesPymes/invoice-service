package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.ModifyInvoiceOrderUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyInvoiceOrderPort implements ModifyInvoiceOrderUseCase {
  private final InvoiceOrderRepository repository;

  @Override
  public InvoiceOrder modifyInvoiceOrder(String invoiceOrderId, InvoiceOrder newInvoiceOrder) {
    Optional<InvoiceOrder> invoiceOrder = repository.findById(invoiceOrderId);
    if (invoiceOrder.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceOrderId + " not found");
    }
    return repository.merge(newInvoiceOrder, invoiceOrder.get());
  }
}
