package com.gespyme.application.invoicedata.port.input;

import com.gespyme.application.invoicedata.usecase.SignInvoiceUseCase;
import com.gespyme.domain.invoiceorder.repository.InvoiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInvoicePort implements SignInvoiceUseCase {

    private final InvoiceOrderRepository invoiceOrderRepository;

    @Override
    public void signInvoice(String invoiceId) {
        /*Optional<InvoiceOrder> invoiceOrder = invoiceOrderRepository.findById(invoiceId);
        if (invoiceOrder.isEmpty()) {
            throw  new NotFoundException("The invoice select cannot be found");
        }

        if(!invoiceOrder.get().getStatus().equals(InvoiceStatus.CREATED)) {
            throw new BadRequestException("Only invoices in CREATED status can be signed");
        }*/
        invoiceOrderRepository.singInvoice(invoiceId);
        //InvoiceOrder signedInvoice =  invoiceOrder.get().toBuilder().status(InvoiceStatus.SIGNED).build();
        //invoiceOrderRepository.save(signedInvoice);
    }
}
