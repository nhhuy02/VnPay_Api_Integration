package com.nhhgrp.customer.request;

import com.nhhgrp.customer.model.Address;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        String id,
        @NotBlank(message = "{javax.validation.constraints.NotNull.message}")
        String firstname,
        @NotBlank(message = "{javax.validation.constraints.NotNull.message}")
        String lastname,
        @NotBlank(message = "{javax.validation.constraints.NotNull.message}")
        @Email(message = "{javax.validation.constraints.Email.message}")
        String email,
        Address address
) {

}