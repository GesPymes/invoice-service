package com.gespyme.application.invoiceorder.usecase;

import java.io.InputStream;

public interface DownloadInvoiceUseCase {
  InputStream downloadInvoice(String invoiceId);
}
