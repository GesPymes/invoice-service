package com.gespyme.infrastructure.adapters.input.controller;

import com.gespyme.application.invoiceorder.usecase.*;
import com.gespyme.commons.model.invoice.InvoiceOrderModelApi;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.infrastructure.mapper.InvoiceOrderMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/invoiceOrder")
public class InvoiceOrderController {
  private final InvoiceOrderMapper invoiceOrderMapper;
  private final FindInvoiceOrderByIdUseCase findInvoiceOrderByIdUseCase;
  private final DeleteInvoiceOrderUseCase deleteInvoiceOrderUseCase;
  private final CreateInvoiceOrderUseCase createInvoiceOrderUseCase;
  private final ModifyInvoiceOrderUseCase modifyInvoiceOrderUseCase;
  private final SendInvoiceUseCase sendInvoiceUseCase;
  private final DownloadInvoiceUseCase downloadInvoiceUseCase;
  private final SignInvoiceUseCase signInvoiceUseCase;
  private final ValidatorService<InvoiceOrderModelApi> validatorService;

  @GetMapping("/{invoiceOrderId}")
  public ResponseEntity<InvoiceOrderModelApi> getInvoiceOrderById(
      @PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    InvoiceOrder invoiceOrder = findInvoiceOrderByIdUseCase.getInvoiceOrderById(invoiceOrderId);
    return ResponseEntity.ok(invoiceOrderMapper.map(invoiceOrder));
  }

  @DeleteMapping("/{invoiceOrderId}")
  public ResponseEntity<Void> deleteInvoiceOrder(
      @PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    deleteInvoiceOrderUseCase.deleteInvoiceOrder(invoiceOrderId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<InvoiceOrderModelApi> createInvoiceOrder(
      @RequestBody InvoiceOrderModelApi invoiceOrderApiModel) {
    validatorService.validate(invoiceOrderApiModel, List.of(Validator.ALL_PARAMS_NOT_NULL));
    InvoiceOrder invoiceOrder =
        createInvoiceOrderUseCase.createInvoiceOrder(invoiceOrderMapper.map(invoiceOrderApiModel));
    URI location = URI.create("/invoiceOrder/" + invoiceOrder.getInvoiceOrderId());
    return ResponseEntity.created(location).body(invoiceOrderMapper.map(invoiceOrder));
  }

  @PatchMapping("/{invoiceOrderId}")
  public ResponseEntity<InvoiceOrderModelApi> modifyInvoiceOrder(
      @PathVariable("invoiceOrderId") String invoiceOrderId,
      @RequestBody InvoiceOrderModelApi invoiceOrderApiModel) {
    validatorService.validate(invoiceOrderApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    InvoiceOrder invoiceOrder =
        modifyInvoiceOrderUseCase.modifyInvoiceOrder(
            invoiceOrderId, invoiceOrderMapper.map(invoiceOrderApiModel));
    return ResponseEntity.ok(invoiceOrderMapper.map(invoiceOrder));
  }

  @PostMapping("/{invoiceOrderId}/sign")
  public ResponseEntity<Void> signInvoice(@PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    signInvoiceUseCase.signInvoice(invoiceOrderId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{invoiceOrderId}/download")
  public ResponseEntity<InputStreamResource> downloadInvoice(@PathVariable("invoiceOrderId") String invoiceOrderId) throws IOException {
    validatorService.validateId(invoiceOrderId);
    String signedKeyPath = "invoice/signed/";
    String key = signedKeyPath + invoiceOrderId;
    InputStream inputStream = downloadInvoiceUseCase.downloadInvoice(key);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=" + invoiceOrderId);
    return ResponseEntity.ok()
            .headers(headers)
            .contentType(MediaType.APPLICATION_PDF)
            .body(new InputStreamResource(inputStream));
  }
}
