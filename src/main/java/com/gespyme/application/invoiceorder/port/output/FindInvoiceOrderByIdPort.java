package com.gespyme.application.invoiceorder.port.output;

import com.gespyme.application.invoiceorder.usecase.FindInvoiceOrderByIdUseCase;
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
public class FindInvoiceOrderByIdPort implements FindInvoiceOrderByIdUseCase {
  private final InvoiceOrderRepository repository;
  private final InvoiceDataRepository invoiceDataRepository;

  public InvoiceOrder getInvoiceOrderById(String invoiceDataId, String invoiceOrderId) {
    Optional<InvoiceOrder> invoiceOrder = repository.findById(invoiceOrderId);
    Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceDataId);
    if (invoiceOrder.isEmpty()) {
      throw new NotFoundException("InvoiceOrder " + invoiceOrderId + " not found");
    }
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    return invoiceOrder.get();
  }
}
