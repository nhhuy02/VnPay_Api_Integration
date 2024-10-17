package com.example.VNPay_Api_Integration.product;

import com.example.VNPay_Api_Integration.Util.MapperUtil;
import com.example.VNPay_Api_Integration.category.Category;
import com.example.VNPay_Api_Integration.category.CategoryRepository;
import com.example.VNPay_Api_Integration.exception.CustomException;
import com.example.VNPay_Api_Integration.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductDto.ProductResponse create(ProductDto.CreateRequest request) {
        log.info("ProductService.create");
        Product product = MapperUtil.mapObject(request, Product.class);
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
        if (categories.isEmpty()) throw new CustomException(HttpStatus.BAD_REQUEST,
                ErrorCode.CATEGORY_NOT_FOUND.getCode(),
                ErrorCode.CATEGORY_NOT_FOUND.getMessage(),
                null);
        product.setCategories(categories);
        return MapperUtil.mapObject(productRepository.save(product), ProductDto.ProductResponse.class);
    }

    public ProductDto.ProductResponse get(String id) {
        log.info("ProductService.get");
        return MapperUtil.mapObject(productRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Product not found")), ProductDto.ProductResponse.class);
    }
    public ProductDto.ProductResponse update(ProductDto.UpdateRequest request) {
        log.info("ProductService.update");
        Product product = productRepository.findById(request.id).orElseThrow(() ->
                new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.PRODUCT_NOT_FOUND.getCode(), ErrorCode.PRODUCT_NOT_FOUND.getMessage(),null));
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(request.getCategoryIds()));
        product.setCategories(categories);
        return MapperUtil.mapObject(productRepository.save(product), ProductDto.ProductResponse.class);
    }
    public void delete(String id) {
        log.info("ProductService.delete");
        productRepository.deleteById(id);
    }
    public List<ProductDto.ProductResponse> getAll() {
        log.info("ProductService.getAll");
        return MapperUtil.mapList(productRepository.findAll(), ProductDto.ProductResponse.class);
    }
}
