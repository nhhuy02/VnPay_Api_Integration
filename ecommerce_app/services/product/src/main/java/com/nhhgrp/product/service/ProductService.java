package com.nhhgrp.product.service;

import com.nhhgrp.product.exception.ProductPurchaseException;
import com.nhhgrp.product.mapper.ProductMapper;
import com.nhhgrp.product.repository.ProductRepository;
import com.nhhgrp.product.request.ProductPurchaseRequest;
import com.nhhgrp.product.request.ProductRequest;
import com.nhhgrp.product.response.ProductPurchaseResponse;
import com.nhhgrp.product.response.ProductResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @CachePut(value = "products", key = "#result")
    public Integer createProduct(ProductRequest request) {
        log.debug("Creating product with request: {}", request);
        var product = mapper.toProduct(request);
        Integer productId = repository.save(product).getId();
        log.info("Product created with ID: {}", productId);
        return productId;
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse findById(Integer id) {
        log.debug("Finding product with ID: {}", id);
        return repository.findById(id)
                .map(mapper::toProductResponse)
                .orElseThrow(() -> {
                    log.error("Product not found with ID: {}", id);
                    return new EntityNotFoundException("Product not found with ID: " + id);
                });
    }

    @Cacheable(value = "allProducts")
    public List<ProductResponse> findAll() {
        log.debug("Finding all products");
        List<ProductResponse> products = repository.findAll()
                .stream()
                .map(mapper::toProductResponse)
                .collect(Collectors.toList());
        log.info("Found {} products", products.size());
        return products;
    }

    @Caching(evict = {
            @CacheEvict(value = "products", allEntries = true),
            @CacheEvict(value = "allProducts", allEntries = true)
    })
    @Transactional(rollbackFor = ProductPurchaseException.class)
    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {
        log.debug("Purchasing products with request: {}", request);
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
        var storedProducts = repository.findAllByIdInOrderById(productIds);

        if (productIds.size() != storedProducts.size()) {
            log.error("One or more products do not exist: {}", productIds);
            throw new ProductPurchaseException("One or more products do not exist");
        }

        var sortedRequest = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

        for (int i = 0; i < storedProducts.size(); i++) {
            var product = storedProducts.get(i);
            var productRequest = sortedRequest.get(i);

            if (product.getAvailableQuantity() < productRequest.quantity()) {
                log.error("Insufficient stock for product with ID: {}. Requested: {}, Available: {}",
                        productRequest.productId(), productRequest.quantity(), product.getAvailableQuantity());
                throw new ProductPurchaseException("Insufficient stock quantity for product with ID: " + productRequest.productId());
            }

            var newAvailableQuantity = product.getAvailableQuantity() - productRequest.quantity();
            product.setAvailableQuantity(newAvailableQuantity);
            repository.save(product);
            log.info("Product with ID: {} purchased, new available quantity: {}", product.getId(), newAvailableQuantity);
            purchasedProducts.add(mapper.toproductPurchaseResponse(product, productRequest.quantity()));
        }

        log.info("Products purchased successfully: {}", purchasedProducts);
        return purchasedProducts;
    }

    @Caching(evict = {
            @CacheEvict(value = "products", key = "#id"),
            @CacheEvict(value = "allProducts", allEntries = true)
    })
    public void deleteProduct(Integer id) {
        log.debug("Deleting product with ID: {}", id);
        repository.deleteById(id);
        log.info("Product with ID: {} deleted", id);
    }
}