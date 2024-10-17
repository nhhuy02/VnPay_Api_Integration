package com.nhhgrp.customer.response;

import com.nhhgrp.customer.model.Address;

public record CustomerResponse(
        String id,
        String firstname,
        String lastname,
        String email,
        Address address
) {
}
