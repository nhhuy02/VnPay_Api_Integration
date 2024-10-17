package com.nhhgrp.order.payment;


import com.nhhgrp.order.customer.CustomerResponse;
import com.nhhgrp.order.domain.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
