package com.gespyme.application.invoiceorder.port.input;

import com.gespyme.application.invoiceorder.usecase.CreateInvoiceOrderUseCase;
import com.gespyme.commons.exeptions.InternalServerError;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreateInvoiceOrderPort implements CreateInvoiceOrderUseCase {
    private final InvoiceOrderRepository repository;
    private final InvoiceDataRepository invoiceDataRepository;

    @Override
    public InvoiceOrder createInvoiceOrder(InvoiceOrder invoiceOrder) {
        Optional<InvoiceData> invoiceData = invoiceDataRepository.findById(invoiceOrder.getInvoiceDataId());
        if (invoiceData.isEmpty()) {
            throw new InternalServerError("Not invoice data found");
        }
        repository.createPdf(invoiceData.get());
        return repository.save(invoiceOrder);
    }
}
