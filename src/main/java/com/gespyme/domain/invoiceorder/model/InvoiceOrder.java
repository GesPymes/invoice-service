package com.gespyme.domain.invoiceorder.model;

import com.gespyme.domain.invoicedata.model.InvoiceData;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class InvoiceOrder {
  private InvoiceData invoiceData;
  private String invoiceOrderId;
  private String appointmentId;
  private InvoiceStatus status;
}
