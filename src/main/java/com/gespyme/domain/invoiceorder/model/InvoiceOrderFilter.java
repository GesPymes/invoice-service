package com.gespyme.domain.invoiceorder.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceOrderFilter {
  private String status;
  private String invoiceDataId;
}
