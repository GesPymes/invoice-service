package com.gespyme.domain.filter;

import lombok.Data;

@Data
public class InvoiceFilter {
    private Integer subtotalAmount;
    private Integer taxRate;
    private Integer totalAmount;
    private String customerName;
    private String description;
    private String status;
}
