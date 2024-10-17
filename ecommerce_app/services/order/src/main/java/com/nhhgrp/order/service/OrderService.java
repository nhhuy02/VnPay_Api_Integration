package com.nhhgrp.order.service;

import com.nhhgrp.order.customer.CustomerClient;
import com.nhhgrp.order.exception.BusinessException;
import com.nhhgrp.order.kafka.OrderConfirmation;
import com.nhhgrp.order.kafka.OrderProducer;
import com.nhhgrp.order.mapper.OrderMapper;
import com.nhhgrp.order.orderline.OrderLineRequest;
import com.nhhgrp.order.orderline.OrderLineService;
import com.nhhgrp.order.payment.PaymentClient;
import com.nhhgrp.order.payment.PaymentRequest;
import com.nhhgrp.order.product.ProductClient;
import com.nhhgrp.order.product.PurchaseRequest;
import com.nhhgrp.order.repository.OrderRepository;
import com.nhhgrp.order.request.OrderRequest;
import com.nhhgrp.order.response.OrderResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final CustomerClient customerClient;
    private final PaymentClient paymentClient;
    private final ProductClient productClient;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    @Transactional
    @CachePut(key = "#result")
    public Integer createOrder(OrderRequest request) {
        log.info("Creating order with request: {}", request);

        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", request.customerId());
                    return new BusinessException("Cannot create order:: No customer exists with the provided ID");
                });

        log.debug("Customer found: {}", customer);

        var purchasedProducts = productClient.purchaseProducts(request.products());
        log.debug("Products purchased: {}", purchasedProducts);

        var order = this.repository.save(mapper.toOrder(request));
        log.info("Order created with ID: {}", order.getId());

        for (PurchaseRequest purchaseRequest : request.products()) {
            OrderLineRequest orderLineRequest = new OrderLineRequest(
                    null,
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()
            );
            orderLineService.saveOrderLine(orderLineRequest);
            log.debug("Order line saved for product ID: {}", purchaseRequest.productId());
        }

        var paymentRequest = new PaymentRequest(
                request.amount(),
                request.paymentMethod(),
                order.getId(),
                order.getReference(),
                customer
        );
        paymentClient.requestOrderPayment(paymentRequest);
        log.info("Payment requested for order ID: {}", order.getId());

        var orderConfirmation = new OrderConfirmation(
                request.reference(),
                request.amount(),
                request.paymentMethod(),
                customer,
                purchasedProducts
        );
        orderProducer.sendOrderConfirmation(orderConfirmation);
        log.info("Order confirmation sent for order reference: {}", request.reference());

        return order.getId();
    }

    @Cacheable(value = "allOrders")
    public List<OrderResponse> findAllOrders() {
        log.info("Fetching all orders");
        List<OrderResponse> orders = this.repository.findAll()
                .stream()
                .map(this.mapper::fromOrder)
                .collect(Collectors.toList());
        log.info("Found {} orders", orders.size());
        return orders;
    }

    @Cacheable(key = "#id")
    public OrderResponse findById(Integer id) {
        log.info("Finding order with ID: {}", id);
        return this.repository.findById(id)
                .map(this.mapper::fromOrder)
                .orElseThrow(() -> {
                    log.error("Order not found with ID: {}", id);
                    return new EntityNotFoundException(String.format("No order found with the provided ID: %d", id));
                });
    }
}