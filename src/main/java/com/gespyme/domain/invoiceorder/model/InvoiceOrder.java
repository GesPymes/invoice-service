package com.gespyme.domain.invoiceorder.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class InvoiceOrder {
  private String invoiceDataId;
  private String invoiceOrderId;
  private String appointmentId;
  private InvoiceStatus status;
}
