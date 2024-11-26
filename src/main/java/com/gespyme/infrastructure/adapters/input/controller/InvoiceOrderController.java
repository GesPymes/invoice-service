package com.gespyme.infrastructure.adapters.input.controller;

import com.gespyme.application.invoiceorder.usecase.CreateInvoiceOrderUseCase;
import com.gespyme.application.invoiceorder.usecase.DeleteInvoiceOrderUseCase;
import com.gespyme.application.invoiceorder.usecase.FindInvoiceOrderByIdUseCase;
import com.gespyme.application.invoiceorder.usecase.ModifyInvoiceOrderUseCase;
import com.gespyme.commons.model.invoice.InvoiceOrderModelApi;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.infrastructure.mapper.InvoiceOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/invoiceOrder")
public class InvoiceOrderController {
    private final InvoiceOrderMapper invoiceOrderMapper;
    private final FindInvoiceOrderByIdUseCase findInvoiceOrderByIdUseCase;
    private final DeleteInvoiceOrderUseCase deleteInvoiceOrderUseCase;
    private final CreateInvoiceOrderUseCase createInvoiceOrderUseCase;
    private final ModifyInvoiceOrderUseCase modifyInvoiceOrderUseCase;
    private final ValidatorService<InvoiceOrderModelApi> validatorService;


    @GetMapping("/{invoiceOrderId}")
    public InvoiceOrderModelApi getInvoiceOrderById(@PathVariable("invoiceOrderId") String invoiceOrderId) {
        validatorService.validateId(invoiceOrderId);
        InvoiceOrder invoiceOrder = findInvoiceOrderByIdUseCase.getInvoiceOrderById(invoiceOrderId);
        return invoiceOrderMapper.map(invoiceOrder);
    }

    @DeleteMapping("/{invoiceOrderId}")
    public void deleteInvoiceOrder(@PathVariable("invoiceOrderId") String invoiceOrderId) {
        validatorService.validateId(invoiceOrderId);
        deleteInvoiceOrderUseCase.deleteInvoiceOrder(invoiceOrderId);
    }

    @PostMapping
    public String createInvoiceOrder(@RequestBody InvoiceOrderModelApi invoiceOrderApiModel) {
        validatorService.validate(invoiceOrderApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
        InvoiceOrder invoiceOrder = createInvoiceOrderUseCase.createInvoiceOrder(invoiceOrderMapper.map(invoiceOrderApiModel));
        return invoiceOrder.getInvoiceOrderId();
    }

    @PatchMapping("/{invoiceOrderId}")
    public InvoiceOrderModelApi modifyInvoiceOrder(@PathVariable("invoiceOrderId") String invoiceOrderId, @RequestBody InvoiceOrderModelApi invoiceOrderApiModel) {
        validatorService.validate(invoiceOrderApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
        InvoiceOrder invoiceOrder = modifyInvoiceOrderUseCase.modifyInvoiceOrder(invoiceOrderId, invoiceOrderMapper.map(invoiceOrderApiModel));
        return invoiceOrderMapper.map(invoiceOrder);
    }
}