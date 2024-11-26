package com.gespyme.application.invoicedata.port.input;

import com.gespyme.application.invoicedata.usecase.DeleteInvoiceDataUseCase;
import com.gespyme.commons.exeptions.NotFoundException;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.domain.invoicedata.repository.InvoiceDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeleteInvoiceDataPort implements DeleteInvoiceDataUseCase {
    private final InvoiceDataRepository repository;

    public void deleteInvoiceData(String invoiceDataId) {
        Optional<InvoiceData> invoiceData = repository.findById(invoiceDataId);
        if (invoiceData.isEmpty()) {
            throw new NotFoundException("InvoiceData " + invoiceDataId + " not found");
        }
        repository.deleteById(invoiceDataId);
    }
}
