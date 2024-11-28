package com.gespyme.application.invoicedata.port.input;

import com.gespyme.application.invoicedata.usecase.ModifyInvoiceDataUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModifyInvoiceDataPort implements ModifyInvoiceDataUseCase {
  private final InvoiceDataRepository repository;

  public InvoiceData modifyInvoiceData(String invoiceDataId, InvoiceData newInvoiceDataData) {
    Optional<InvoiceData> invoiceData = repository.findById(invoiceDataId);
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    return repository.merge(newInvoiceDataData, invoiceData.get());
  }
}
