package com.gespyme.infrastructure.adapters.input.controller;

import com.gespyme.application.invoiceorder.usecase.*;
import com.gespyme.commons.model.invoice.InvoiceOrderBaseModelApi;
import com.gespyme.commons.model.invoice.InvoiceOrderFilterModelApi;
import com.gespyme.commons.model.invoice.InvoiceOrderModelApi;
import com.gespyme.commons.validator.Validator;
import com.gespyme.commons.validator.ValidatorService;
import com.gespyme.domain.invoiceorder.model.InvoiceOrder;
import com.gespyme.infrastructure.mapper.InvoiceOrderMapper;
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
@RequestMapping(value = "/invoiceData/{invoiceDataId}/invoiceOrder")
public class InvoiceOrderController {
  private final InvoiceOrderMapper invoiceOrderMapper;
  private final FindInvoiceOrderByIdUseCase findInvoiceOrderByIdUseCase;
  private final DeleteInvoiceOrderUseCase deleteInvoiceOrderUseCase;
  private final CreateInvoiceOrderUseCase createInvoiceOrderUseCase;
  private final ModifyInvoiceOrderUseCase modifyInvoiceOrderUseCase;
  private final DownloadInvoiceUseCase downloadInvoiceUseCase;
  private final SignInvoiceUseCase signInvoiceUseCase;
  private final FindInvoiceOrderUseCase findInvoiceOrderUseCase;
  private final ValidatorService<InvoiceOrderBaseModelApi> validatorService;

  @GetMapping("/{invoiceOrderId}")
  public ResponseEntity<InvoiceOrderModelApi> getInvoiceOrderById(
          @PathVariable("invoiceDataId") String invoiceDataId, @PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    InvoiceOrder invoiceOrder = findInvoiceOrderByIdUseCase.getInvoiceOrderById(invoiceDataId, invoiceOrderId);
    return ResponseEntity.ok(invoiceOrderMapper.map(invoiceOrder));
  }

  @GetMapping
  public ResponseEntity<List<InvoiceOrderModelApi>> getInvoiceOrderById(
          @PathVariable("invoiceDataId") String invoiceDataId, InvoiceOrderFilterModelApi invoiceOrderFilter) {
    validatorService.validate(invoiceOrderFilter, List.of(Validator.ONE_PARAM_NOT_NULL));
    List<InvoiceOrder> invoiceOrder =
        findInvoiceOrderUseCase.findInvoiceOrder(invoiceDataId, invoiceOrderMapper.map(invoiceOrderFilter));
    return ResponseEntity.ok(invoiceOrderMapper.mapToList(invoiceOrder));
  }

  @DeleteMapping("/{invoiceOrderId}")
  public ResponseEntity<Void> deleteInvoiceOrder(
          @PathVariable("invoiceDataId") String invoiceDataId, @PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    deleteInvoiceOrderUseCase.deleteInvoiceOrder(invoiceDataId, invoiceOrderId);
    return ResponseEntity.noContent().build();
  }

  @PostMapping
  public ResponseEntity<InvoiceOrderModelApi> createInvoiceOrder(
          @PathVariable("invoiceDataId") String invoiceDataId) {
    InvoiceOrder invoiceOrder =
        createInvoiceOrderUseCase.createInvoiceOrder(invoiceDataId);
    URI location = URI.create("/invoiceOrder/" + invoiceOrder.getInvoiceOrderId());
    return ResponseEntity.created(location).body(invoiceOrderMapper.map(invoiceOrder));
  }

  @PatchMapping("/{invoiceOrderId}")
  public ResponseEntity<InvoiceOrderModelApi> modifyInvoiceOrder(@PathVariable("invoiceDataId") String invoiceDataId,
      @PathVariable("invoiceOrderId") String invoiceOrderId,
      @RequestBody InvoiceOrderModelApi invoiceOrderApiModel) {
    validatorService.validate(invoiceOrderApiModel, List.of(Validator.ONE_PARAM_NOT_NULL));
    InvoiceOrder invoiceOrder =
        modifyInvoiceOrderUseCase.modifyInvoiceOrder(invoiceDataId,
            invoiceOrderId, invoiceOrderMapper.map(invoiceOrderApiModel));
    return ResponseEntity.ok(invoiceOrderMapper.map(invoiceOrder));
  }

  @PostMapping("/{invoiceOrderId}/sign")
  public ResponseEntity<Void> signInvoice(@PathVariable("invoiceDataId") String invoiceDataId, @PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    signInvoiceUseCase.signInvoice(invoiceDataId, invoiceOrderId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{invoiceOrderId}/download")
  public ResponseEntity<InputStreamResource> downloadInvoice(
          @PathVariable("invoiceDataId") String invoiceDataId, @PathVariable("invoiceOrderId") String invoiceOrderId) {
    validatorService.validateId(invoiceOrderId);
    String signedKeyPath = "invoice/signed/";
    String key = signedKeyPath + invoiceOrderId;
    InputStream inputStream = downloadInvoiceUseCase.downloadInvoice(invoiceDataId, key);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Disposition", "attachment; filename=" + invoiceOrderId);
    return ResponseEntity.ok()
        .headers(headers)
        .contentType(MediaType.APPLICATION_PDF)
        .body(new InputStreamResource(inputStream));
  }
}
