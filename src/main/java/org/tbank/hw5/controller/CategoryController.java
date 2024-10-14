package org.tbank.hw5.controller;

import org.example.annotation.LogExecutionTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/places/categories")
@LogExecutionTime
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<ResponseDto<List<CategoryDto>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryDto> categoryDtoList = categoryMapper.toCategoryDtoList(categories);
        return ResponseEntity.ok(new ResponseDto<>(categoryDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryDto>> getCategoryById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        CategoryDto categoryDto = categoryMapper.toCategoryDto(category);
        return ResponseEntity.ok(new ResponseDto<>(categoryDto));
    }

    @PostMapping
    public ResponseEntity<ResponseDto<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDTO) {
        Category category = categoryMapper.toCategory(categoryDTO);
        Category createdCategory = categoryService.createCategory(category);
        CategoryDto createdCategoryDto = categoryMapper.toCategoryDto(createdCategory);
        return ResponseEntity.ok(new ResponseDto<>(createdCategoryDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<CategoryDto>> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        Category category = categoryMapper.toCategory(categoryDto);
        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryDto updatedCategoryDto = categoryMapper.toCategoryDto(updatedCategory);
        return ResponseEntity.ok(new ResponseDto<>(updatedCategoryDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

}
