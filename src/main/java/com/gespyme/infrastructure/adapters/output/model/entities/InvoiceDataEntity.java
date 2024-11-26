package com.gespyme.infrastructure.adapters.output.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INVOICE_DATA")
public class InvoiceDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String invoiceDataId;

    @Column(name = "appointment_id")
    private String appointmentId;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "subtotal_amount")
    private Integer subtotalAmount;

    @Column(name = "tax_rate")
    private Integer taxRate;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "description")
    private String description;

    @OneToMany
    @JoinColumn(name = "invoice_data_id")
    private List<InvoiceOrderEntity> invoiceOrder;
}
