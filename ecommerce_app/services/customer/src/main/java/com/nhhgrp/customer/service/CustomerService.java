package com.nhhgrp.customer.service;

import com.nhhgrp.customer.exception.CustomerNotFoundException;
import com.nhhgrp.customer.mapper.CustomerMapper;
import com.nhhgrp.customer.model.Customer;
import com.nhhgrp.customer.repository.CustomerRepository;
import com.nhhgrp.customer.request.CustomerRequest;
import com.nhhgrp.customer.response.CustomerResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    @CachePut(key = "#result")
    public String createCustomer(CustomerRequest request) {
        log.info("Creating customer with request: {}", request);
        var customer = this.repository.save(mapper.toCustomer(request));
        log.info("Customer created with ID: {}", customer.getId());
        return customer.getId();
    }

    @Caching(
            put = @CachePut(key = "#request.id"),
            evict = @CacheEvict(value = "allCustomers", allEntries = true)
    )
    public void updateCustomer(CustomerRequest request) {
        log.info("Updating customer with ID: {}", request.id());
        var customer = this.repository.findById(request.id())
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", request.id());
                    return new CustomerNotFoundException(
                            String.format("Cannot update customer:: No customer found with the provided ID: %s", request.id())
                    );
                });
        mergeCustomer(customer, request);
        this.repository.save(customer);
        log.info("Customer with ID: {} updated successfully", request.id());
    }

    private void mergeCustomer(Customer customer, CustomerRequest request) {
        log.debug("Merging customer data for customer ID: {}", customer.getId());
        if (StringUtils.isNotBlank(request.firstname())) {
            customer.setFirstname(request.firstname());
        }
        if (StringUtils.isNotBlank(request.email())) {
            customer.setEmail(request.email());
        }
        if (request.address() != null) {
            customer.setAddress(request.address());
        }
    }

    @Cacheable(value = "allCustomers")
    public List<CustomerResponse> findAllCustomers() {
        log.info("Fetching all customers");
        return this.repository.findAll()
                .stream()
                .map(this.mapper::fromCustomer)
                .peek(customer -> log.debug("Fetched customer: {}", customer))
                .collect(Collectors.toList());
    }

    @Cacheable(key = "#id")
    public CustomerResponse findById(String id) {
        log.info("Finding customer with ID: {}", id);
        return this.repository.findById(id)
                .map(mapper::fromCustomer)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", id);
                    return new CustomerNotFoundException(String.format("No customer found with the provided ID: %s", id));
                });
    }

    @Cacheable(key = "#id")
    public boolean existsById(String id) {
        log.info("Checking if customer exists with ID: {}", id);
        boolean exists = this.repository.findById(id).isPresent();
        log.info("Customer with ID: {} exists: {}", id, exists);
        return exists;
    }

    @Caching(evict = {
            @CacheEvict(key = "#id"),
            @CacheEvict(value = "allCustomers", allEntries = true)
    })
    public void deleteCustomer(String id) {
        log.info("Deleting customer with ID: {}", id);
        this.repository.deleteById(id);
        log.info("Customer with ID: {} deleted successfully", id);
    }
}