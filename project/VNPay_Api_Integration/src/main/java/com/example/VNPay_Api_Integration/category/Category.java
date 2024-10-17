package com.example.VNPay_Api_Integration.category;

import com.example.VNPay_Api_Integration.model.AbstractEntity;
import com.example.VNPay_Api_Integration.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category extends AbstractEntity {
    @NotBlank(message = "Name is mandatory")
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Product> products;
}
