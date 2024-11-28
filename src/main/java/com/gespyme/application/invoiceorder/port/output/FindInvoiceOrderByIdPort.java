package com.gespyme.application.invoiceorder.port.output;

import com.gespyme.application.invoiceorder.usecase.FindInvoiceOrderByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindInvoiceOrderByIdPort implements FindInvoiceOrderByIdUseCase {
  private final InvoiceOrderRepository repository;

  public InvoiceOrder getInvoiceOrderById(String invoiceOrderId) {
    Optional<InvoiceOrder> invoiceOrder = repository.findById(invoiceOrderId);
    if (invoiceOrder.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceOrderId + " not found");
    }
    return invoiceOrder.get();
  }
}
