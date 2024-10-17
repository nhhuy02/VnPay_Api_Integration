package com.example.VNPay_Api_Integration.product;

import com.example.VNPay_Api_Integration.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/all")
    public ResponseObject<List<ProductDto.ProductResponse>> getAllProducts() {
        return new ResponseObject<>(HttpStatus.OK, "Success", productService.getAll());
    }
    @GetMapping("/{id}")
    public ResponseObject<ProductDto.ProductResponse> getProduct(@PathVariable String id) {
        return new ResponseObject<>(HttpStatus.OK, "Success", productService.get(id));
    }
    @PutMapping("/update")
    public ResponseObject<ProductDto.ProductResponse> updateProduct(@RequestBody ProductDto.UpdateRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", productService.update(request));
    }
    @PostMapping("/add")
    public ResponseObject<ProductDto.ProductResponse> addProduct(@Valid @RequestBody ProductDto.CreateRequest request) {
        return new ResponseObject<>(HttpStatus.CREATED, "Success", productService.create(request));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseObject<ResponseObject.Payload<?>> deleteProduct(@PathVariable String id) {
        productService.delete(id);
        return new ResponseObject<>(HttpStatus.OK, "Success", null);
    }
}