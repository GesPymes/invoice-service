package com.gespyme.application.invoicedata.port.output;

import com.gespyme.application.invoicedata.usecase.FindInvoiceDataByIdUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindInvoiceDataByIdPort implements FindInvoiceDataByIdUseCase {
  private final InvoiceDataRepository repository;

  public InvoiceData getInvoiceDataById(String invoiceDataId) {
    Optional<InvoiceData> invoiceData = repository.findById(invoiceDataId);
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    return invoiceData.get();
  }
}
