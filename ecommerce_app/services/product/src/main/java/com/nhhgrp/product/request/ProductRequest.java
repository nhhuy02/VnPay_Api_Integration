package com.nhhgrp.product.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(

        Integer id,
        @NotBlank(message = "{javax.validation.constraints.NotNull.message}")
        String name,
        @NotBlank(message = "{javax.validation.constraints.NotNull.message}")
        String description,
        @Positive(message = "{javax.validation.constraints.NotNull.message}")
        double availableQuantity,
        @Positive(message = "{javax.validation.constraints.NotNull.message}")
        BigDecimal price,
        @NotNull(message = "{javax.validation.constraints.NotNull.message}")
        Integer categoryId
) {
}
