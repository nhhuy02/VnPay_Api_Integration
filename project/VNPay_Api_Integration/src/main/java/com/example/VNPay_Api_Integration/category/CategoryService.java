package com.example.VNPay_Api_Integration.category;

import com.example.VNPay_Api_Integration.Util.MapperUtil;
import com.example.VNPay_Api_Integration.exception.CustomException;
import com.example.VNPay_Api_Integration.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    public CategoryDto.CategoryResponse addCategory(CategoryDto.CreateRequest category) {
        log.info("CategoryService.addCategory");
        return MapperUtil
                .mapObject(
                        categoryRepository.save(MapperUtil.mapObject(category, Category.class)),
                        CategoryDto.CategoryResponse.class
                );
    }

    public CategoryDto.CategoryResponse getCategory(String id) {
        log.info("CategoryService.getCategory");
        return MapperUtil
                .mapObject(
                        categoryRepository.findById(id).orElseThrow(() ->
                                new CustomException(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND.getCode(), ErrorCode.NOT_FOUND.getMessage(), null)),
                        CategoryDto.CategoryResponse.class
                );
    }

    public void deleteCategory(String id) {
        log.info("CategoryService.deleteCategory");
        categoryRepository.deleteById(id);
    }
    public CategoryDto.CategoryResponse updateCategory(CategoryDto.UpdateRequest category) {
        log.info("CategoryService.updateCategory");
        Category categoryToUpdate = categoryRepository.findById(category.getId()).orElseThrow(() ->
                new CustomException(HttpStatus.NOT_FOUND, ErrorCode.CATEGORY_NOT_FOUND.getCode(), ErrorCode.CATEGORY_NOT_FOUND.getMessage(), null));
        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setDescription(category.getDescription());
        categoryToUpdate.setUpdatedAt(LocalDateTime.now());
        return MapperUtil
                .mapObject(
                        categoryRepository.save(categoryToUpdate),
                        CategoryDto.CategoryResponse.class
                );
    }

    public List<CategoryDto.CategoryResponse> getCategories() {
        log.info("CategoryService.getCategories");
        return MapperUtil.mapList(categoryRepository.findAll(), CategoryDto.CategoryResponse.class);
    }
}
