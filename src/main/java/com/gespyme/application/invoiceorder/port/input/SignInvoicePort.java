package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.SignInvoiceUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignInvoicePort implements SignInvoiceUseCase {
  private final InvoiceOrderRepository invoiceOrderRepository;
  private final InvoiceDataRepository invoiceDataRepository;

  @Override
  public void signInvoice(String invoiceDataId, String invoiceOrderId) {
    Optional<InvoiceOrder> invoiceOrder = invoiceOrderRepository.findById(invoiceOrderId);
    Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceDataId);
    if (invoiceOrder.isEmpty()) {
      throw new NotFoundException("InvoiceOrder " + invoiceOrderId + " not found");
    }
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    invoiceOrderRepository.singInvoice(invoiceOrderId);
  }
}
