package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.SignInvoiceUseCase;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInvoicePort implements SignInvoiceUseCase {
  private final InvoiceOrderRepository invoiceOrderRepository;

  @Override
  public void signInvoice(String invoiceId) {
    invoiceOrderRepository.singInvoice(invoiceId);
  }
}
