package org.tbank.hw5.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.dto.ResponseDto;

import java.util.List;

@RequestMapping("/api/v1/places/categories")
public interface CategoryController {

    @GetMapping
    ResponseEntity<ResponseDto<List<CategoryDto>>> getAllCategories();

    @GetMapping("/{id}")
    ResponseEntity<ResponseDto<CategoryDto>> getCategoryById(@PathVariable Long id);

    @PostMapping
    ResponseEntity<ResponseDto<CategoryDto>> createCategory(@RequestBody CategoryDto categoryDTO);

    @PutMapping("/{id}")
    ResponseEntity<ResponseDto<CategoryDto>> updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteCategory(@PathVariable Long id);
}
