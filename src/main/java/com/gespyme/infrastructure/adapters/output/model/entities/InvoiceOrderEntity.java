package com.gespyme.infrastructure.adapters.output.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "INVOICE_ORDER")
public class InvoiceOrderEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "invoice_order_id")
  private String invoiceOrderId;

  @Column(name = "appointment_id")
  private String appointmentId;

  @Column(name = "status")
  private String status;

  @ManyToOne
  @JoinColumn(name = "invoice_data_id", nullable = false)
  private InvoiceDataEntity invoiceData;

}
