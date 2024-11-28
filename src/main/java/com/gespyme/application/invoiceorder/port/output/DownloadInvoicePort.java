package com.gespyme.application.invoiceorder.port.output;

import com.gespyme.application.invoiceorder.usecase.DownloadInvoiceUseCase;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DownloadInvoicePort implements DownloadInvoiceUseCase {

  private final InvoiceOrderRepository repository;

  @Override
  public InputStream downloadInvoice(String invoiceId) {
    return repository.downloadInvoicePdf(invoiceId);
  }
}
