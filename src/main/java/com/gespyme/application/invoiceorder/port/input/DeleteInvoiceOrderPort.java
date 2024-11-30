package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.DeleteInvoiceOrderUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteInvoiceOrderPort implements DeleteInvoiceOrderUseCase {
  private final InvoiceOrderRepository repository;


  @Override
  public void deleteInvoiceOrder(String invoiceOrderId) {
    Optional<InvoiceOrder> invoiceData = repository.findById(invoiceOrderId);
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceOrderId + " not found");
    }
    repository.deleteById(invoiceOrderId);
  }
}
