package com.nhhgrp.customer.controller;

import com.nhhgrp.customer.request.CustomerRequest;
import com.nhhgrp.customer.response.ApiResponse;
import com.nhhgrp.customer.response.CustomerResponse;
import com.nhhgrp.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createCustomer(@RequestBody @Valid CustomerRequest request, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        String customerId = this.service.createCustomer(request);
        String message = messageSource.getMessage("customer.created", new Object[]{customerId}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse<>(customerId, message, "SUCCESS"));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateCustomer(@RequestBody @Valid CustomerRequest request, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        this.service.updateCustomer(request);
        String message = messageSource.getMessage("customer.updated", null, LocaleContextHolder.getLocale());
        return ResponseEntity.accepted().body(new ApiResponse<>(null, message, "SUCCESS"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        List<CustomerResponse> customers = this.service.findAllCustomers();
        String message = messageSource.getMessage("customers.found", new Object[]{customers.size()}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse<>(customers, message, "SUCCESS"));
    }

    @GetMapping("/exists/{customer-id}")
    public ResponseEntity<ApiResponse<Boolean>> existsById(@PathVariable("customer-id") String customerId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        boolean exists = this.service.existsById(customerId);
        String messageKey = exists ? "customer.exists" : "customer.not.exists";
        String message = messageSource.getMessage(messageKey, new Object[]{customerId}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse<>(exists, message, "SUCCESS"));
    }

    @GetMapping("/{customer-id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> findById(@PathVariable("customer-id") String customerId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        CustomerResponse customer = this.service.findById(customerId);
        String message = messageSource.getMessage("customer.found", new Object[]{customerId}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse<>(customer, message, "SUCCESS"));
    }

    @DeleteMapping("/{customer-id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable("customer-id") String customerId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        this.service.deleteCustomer(customerId);
        String message = messageSource.getMessage("customer.deleted", new Object[]{customerId}, LocaleContextHolder.getLocale());
        return ResponseEntity.accepted().body(new ApiResponse<>(null, message, "SUCCESS"));
    }
}
