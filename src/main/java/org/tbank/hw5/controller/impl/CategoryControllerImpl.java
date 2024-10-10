package org.tbank.hw5.controller.impl;

import org.example.annotation.LogExecutionTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.tbank.hw5.controller.CategoryController;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.service.CategoryService;

import java.util.List;

@RestController
@LogExecutionTime
public class CategoryControllerImpl implements CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryControllerImpl(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResponseEntity<ResponseDto<List<CategoryDto>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDto> categoryDtoList = categoryMapper.toCategoryDtoList(categories);
        return ResponseEntity.ok(new ResponseDto<>(categoryDtoList));
    }

    @Override
    public ResponseEntity<ResponseDto<CategoryDto>> getCategoryById(Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        return ResponseEntity.ok(new ResponseDto<>(categoryDto));
    }

    @Override
    public ResponseEntity<ResponseDto<CategoryDto>> createCategory(CategoryDto CategoryDto) {
        Category category = categoryMapper.toCategory(CategoryDto);
        Category createdCategory = categoryService.createCategory(category);
        CategoryDto createdCategoryDto = categoryMapper.toCategoryDto(createdCategory);
        return ResponseEntity.ok(new ResponseDto<>(createdCategoryDto));
    }

    @Override
    public ResponseEntity<ResponseDto<CategoryDto>> updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryDto updatedCategoryDto = categoryMapper.toCategoryDto(updatedCategory);
        return ResponseEntity.ok(new ResponseDto<>(updatedCategoryDto));
    }

    @Override
    public ResponseEntity<Void> deleteCategory(Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
