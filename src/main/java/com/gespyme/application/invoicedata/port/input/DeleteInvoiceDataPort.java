package com.gespyme.application.invoicedata.port.input;

import com.gespyme.application.invoicedata.usecase.DeleteInvoiceDataUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteInvoiceDataPort implements DeleteInvoiceDataUseCase {
  private final InvoiceDataRepository repository;

  public void deleteInvoiceData(String invoiceDataId) {
    Optional<InvoiceData> invoiceData = repository.findById(invoiceDataId);
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    repository.deleteById(invoiceDataId);
  }
}
