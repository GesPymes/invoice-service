package com.gespyme.application.invoiceorder.port.output;

import com.gespyme.application.invoiceorder.usecase.DownloadInvoiceUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.io.InputStream;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DownloadInvoicePort implements DownloadInvoiceUseCase {

  private final InvoiceOrderRepository repository;
  private final InvoiceDataRepository invoiceDataRepository;

  @Override
  public InputStream downloadInvoice(String invoiceDataId, String invoiceOrderId) {
    Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceDataId);
    if (invoiceData.isEmpty()) {
      throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
    }
    return repository.downloadInvoicePdf(invoiceOrderId);
  }
}
