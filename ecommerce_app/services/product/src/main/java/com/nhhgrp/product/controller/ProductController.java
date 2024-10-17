package com.nhhgrp.product.controller;

import com.nhhgrp.product.request.ProductPurchaseRequest;
import com.nhhgrp.product.request.ProductRequest;
import com.nhhgrp.product.response.ApiResponse;
import com.nhhgrp.product.response.ProductPurchaseResponse;
import com.nhhgrp.product.response.ProductResponse;
import com.nhhgrp.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;
    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody @Valid ProductRequest request, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        Integer productId = service.createProduct(request);
        String message = messageSource.getMessage("product.created", new Object[]{productId}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse(productId, message, "SUCCESS"));
    }

    @PostMapping("/purchase")
    public ResponseEntity<List<ProductPurchaseResponse>> purchaseProducts(
            @RequestBody List<ProductPurchaseRequest> request
    ) {
        return ResponseEntity.ok(service.purchaseProducts(request));
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<?> findById(@PathVariable("product-id") Integer productId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        ProductResponse product = service.findById(productId);
        String message = messageSource.getMessage("product.found", new Object[]{productId}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse(product, message, "SUCCESS"));
    }

    @GetMapping
    public ResponseEntity<?> findAll(@RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        List<ProductResponse> products = service.findAll();
        String message = messageSource.getMessage("products.found", new Object[]{products.size()}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse(products, message, "SUCCESS"));
    }

    @DeleteMapping("/{product-id}")
    public ResponseEntity<?> delete(@PathVariable("product-id") Integer productId, @RequestHeader(name = "Accept-Language", required = false) Locale locale) {
        this.service.deleteProduct(productId);
        String message = messageSource.getMessage("product.deleted", new Object[]{productId}, LocaleContextHolder.getLocale());
        return ResponseEntity.ok(new ApiResponse("", message, "SUCCESS"));
    }
}