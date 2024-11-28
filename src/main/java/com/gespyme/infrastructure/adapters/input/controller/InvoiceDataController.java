package com.gespyme.infrastructure.adapters.input.controller;

import com.gespyme.application.invoicedata.usecase.*;
import com.gespyme.commons.model.invoice.InvoiceDataFilterModelApi;
import com.gespyme.commons.model.invoice.InvoiceDataModelApi;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.filter.InvoiceFilter;
import com.gespyme.domain.invoicedata.model.InvoiceData;
import com.gespyme.infrastructure.mapper.InvoiceDataMapper;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/invoiceData")
public class InvoiceDataController {
  private final InvoiceDataMapper invoiceDataMapper;
  private final FindInvoiceDataUseCase findInvoiceDataUseCase;
  private final FindInvoiceDataByIdUseCase findInvoiceDataByIdUseCase;
  private final DeleteInvoiceDataUseCase deleteInvoiceDataUseCase;
  private final CreateInvoiceDataUseCase createInvoiceDataUseCase;
  private final ModifyInvoiceDataUseCase modifyInvoiceDataUseCase;
  private final ValidatorService<InvoiceDataModelApi> validatorService;

  @GetMapping("/{invoiceDataId}")
  public ResponseEntity<InvoiceDataModelApi> getInvoiceDataById(
      @PathVariable("invoiceDataId") String invoiceDataId) {
    validatorService.validateId(invoiceDataId);
    InvoiceData invoiceData = findInvoiceDataByIdUseCase.getInvoiceDataById(invoiceDataId);
    return ResponseEntity.ok(invoiceDataMapper.map(invoiceData));
  }

  @GetMapping
  public ResponseEntity<List<InvoiceDataModelApi>> findInvoiceDatas(
      InvoiceDataFilterModelApi invoiceDataFilterModelApi) {
    InvoiceFilter invoiceFilter = invoiceDataMapper.map(invoiceDataFilterModelApi);
    List<InvoiceData> invoiceDatas = findInvoiceDataUseCase.findInvoiceData(invoiceFilter);
    return ResponseEntity.ok(invoiceDataMapper.map(invoiceDatas));
  }

  @DeleteMapping("/{invoiceDataId}")
  public ResponseEntity<Void> deleteInvoiceData(
      @PathVariable("invoiceDataId") String invoiceDataId) {
    validatorService.validateId(invoiceDataId);
    deleteInvoiceDataUseCase.deleteInvoiceData(invoiceDataId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<InvoiceDataModelApi> createInvoiceData(
      @RequestBody InvoiceDataModelApi invoiceDataApiModel) {
    validatorService.validate(invoiceDataApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
    InvoiceData invoiceData =
        createInvoiceDataUseCase.createInvoiceData(invoiceDataMapper.map(invoiceDataApiModel));
    URI location = URI.create("/invoiceData/" + invoiceData.getCustomerId());
    return ResponseEntity.created(location).body(invoiceDataMapper.map(invoiceData));
  }

  @PatchMapping("/{invoiceDataId}")
  public ResponseEntity<InvoiceDataModelApi> modifyInvoiceData(
      @PathVariable("invoiceDataId") String invoiceDataId,
      @RequestBody InvoiceDataModelApi invoiceDataApiModel) {
    validatorService.validate(invoiceDataApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    InvoiceData invoiceData =
        modifyInvoiceDataUseCase.modifyInvoiceData(
            invoiceDataId, invoiceDataMapper.map(invoiceDataApiModel));
    return ResponseEntity.ok(invoiceDataMapper.map(invoiceData));
  }
}
