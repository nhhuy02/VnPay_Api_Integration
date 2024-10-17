package com.example.VNPay_Api_Integration;

import com.example.VNPay_Api_Integration.category.CategoryDto;
import com.example.VNPay_Api_Integration.category.CategoryService;
import com.example.VNPay_Api_Integration.product.ProductDto;
import com.example.VNPay_Api_Integration.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class VnPayApiIntegrationApplication implements CommandLineRunner {
	private final CategoryService categoryService;
	private final ProductService productService;

	public static void main(String[] args) {
		SpringApplication.run(VnPayApiIntegrationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		categoryService.addCategory(CategoryDto.CreateRequest.builder().name("Category 1").description("Description 1").build());
		categoryService.addCategory(CategoryDto.CreateRequest.builder().name("Category 2").description("Description 2").build());
		categoryService.addCategory(CategoryDto.CreateRequest.builder().name("Category 3").description("Description 3").build());

		productService.create(ProductDto.CreateRequest
				.builder()
				.name("Product 1")
				.description("Description 1")
				.price(1000)
				.categoryIds(categoryService.getCategories().stream().map(CategoryDto.CategoryResponse::getId).collect(Collectors.toSet()))
				.build());
	}
}
