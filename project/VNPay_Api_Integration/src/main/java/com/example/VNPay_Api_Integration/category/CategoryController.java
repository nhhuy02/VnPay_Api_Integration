package com.example.VNPay_Api_Integration.category;

import com.example.VNPay_Api_Integration.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;

    @RestController
    @RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/all")
    public ResponseObject<List<CategoryDto.CategoryResponse>> getAll() {
        return new ResponseObject<>(HttpStatus.OK, "Fetch all category completed",categoryService.getCategories());
    }
    @PostMapping("/add")
    public ResponseObject<CategoryDto.CategoryResponse> add(@RequestBody CategoryDto.CreateRequest createRequest) {
        return new ResponseObject<>(HttpStatus.CREATED, "Add category completed", categoryService.addCategory(createRequest));
    }
    @PutMapping("/update")
    public ResponseObject<CategoryDto.CategoryResponse> update(@RequestBody CategoryDto.UpdateRequest updateRequest) {
        return new ResponseObject<>(HttpStatus.ACCEPTED, "Update category completed", categoryService.updateCategory(updateRequest));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseObject<ResponseObject.Payload<?>> delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return new ResponseObject<>(HttpStatus.OK, "Delete category completed", null);
    }
}