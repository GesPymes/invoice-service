package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.CreateInvoiceOrderUseCase;
import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.model.InvoiceStatus;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateInvoiceOrderPort implements CreateInvoiceOrderUseCase {
  private final InvoiceOrderRepository repository;
  private final InvoiceDataRepository invoiceDataRepository;

  @Override
  public InvoiceOrder createInvoiceOrder(String invoiceDataId) {
    Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceDataId);
    if (invoiceData.isEmpty()) {
      throw new InternalServerError("Not invoice data found");
    }
    InvoiceOrder toSave =
            InvoiceOrder.builder()
            .invoiceData(invoiceData.get())
            .status(InvoiceStatus.CREATED)
            .appointmentId(invoiceData.get().getAppointmentId())
            .build();

    InvoiceOrder savedInvoiceOrder = repository.save(toSave);
    byte[] file = repository.createInvoicePdfFile(invoiceData.get());
    repository.saveInvoice(file, savedInvoiceOrder.getInvoiceOrderId());
    return savedInvoiceOrder;
  }
}
