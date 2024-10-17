package com.nhhgrp.product.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse implements Serializable {
    Integer id;
    String name;
    String description;
    double availableQuantity;
    BigDecimal price;
    Integer categoryId;
    String categoryName;
    String categoryDescription;
}
