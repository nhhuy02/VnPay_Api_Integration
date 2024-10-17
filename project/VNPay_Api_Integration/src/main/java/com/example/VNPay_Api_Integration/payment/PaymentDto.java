package com.example.VNPay_Api_Integration.payment;

import lombok.Builder;

public abstract class PaymentDto {
    @Builder
    public static class VNPayResponse {
        public String code;
        public String message;
        public String paymentUrl;
    }
}