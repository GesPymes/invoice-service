package com.gespyme.domain.invoicedata.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceData {
    private String invoiceDataId;
    private String appointmentId;
    private String customerId;
    private Integer subtotalAmount;
    private Integer taxRate;
    private Integer totalAmount;
    private String description;
    private String status;
}
