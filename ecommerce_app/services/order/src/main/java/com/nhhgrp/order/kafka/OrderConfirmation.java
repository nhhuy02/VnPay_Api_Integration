package com.nhhgrp.order.kafka;

import com.nhhgrp.order.customer.CustomerResponse;
import com.nhhgrp.order.domain.PaymentMethod;
import com.nhhgrp.order.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation (
        String orderReference,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products

) {
}
