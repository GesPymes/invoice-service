package com.gespyme.application.invoicedata.port.input;

import com.gespyme.application.invoicedata.usecase.CreateInvoiceDataUseCase;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateInvoiceDataPort implements CreateInvoiceDataUseCase {
  private final InvoiceDataRepository repository;

  public InvoiceData createInvoiceData(InvoiceData invoiceData) {
    return repository.save(invoiceData);
  }
}
