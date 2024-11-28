package com.gespyme.application.invoiceorder.port.output;

import com.gespyme.application.invoiceorder.usecase.SendInvoiceUseCase;
import org.springframework.stereotype.Component;

@Component
public class SendInvoicePort implements SendInvoiceUseCase {
  @Override
  public void sendInvoice(String invoiceId) {}
}
