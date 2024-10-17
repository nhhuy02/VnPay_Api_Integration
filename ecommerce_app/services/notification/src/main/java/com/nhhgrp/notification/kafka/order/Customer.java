package com.nhhgrp.notification.kafka.order;

public record Customer(
        String id,
        String firstname,
        String lastname,
        String email
) {

}