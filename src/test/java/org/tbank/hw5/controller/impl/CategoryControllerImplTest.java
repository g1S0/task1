package org.tbank.hw5.controller.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerImplTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryControllerImpl categoryController;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "slug1", "Category 1");
        categoryDto = new CategoryDto(1L, "slug1", "Category 1");
    }

    @Test
    public void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDtoList(anyList())).thenReturn(List.of(categoryDto));

        ResponseEntity<ResponseDto<List<CategoryDto>>> response = categoryController.getAllCategories();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getData().size());
        assertEquals("Category 1", response.getBody().getData().get(0).getName());

        verify(categoryService, times(1)).getAllCategories();
        verify(categoryMapper, times(1)).toCategoryDtoList(anyList());
    }

    @Test
    public void testGetCategoryById() {
        when(categoryService.getCategoryById(1L)).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        ResponseEntity<ResponseDto<CategoryDto>> response = categoryController.getCategoryById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Category 1", response.getBody().getData().getName());

        verify(categoryService, times(1)).getCategoryById(1L);
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    public void testCreateCategory() {
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        ResponseEntity<ResponseDto<CategoryDto>> response = categoryController.createCategory(categoryDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Category 1", response.getBody().getData().getName());

        verify(categoryMapper, times(1)).toCategory(any(CategoryDto.class));
        verify(categoryService, times(1)).createCategory(any(Category.class));
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    public void testUpdateCategory() {
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryService.updateCategory(eq(1L), any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        ResponseEntity<ResponseDto<CategoryDto>> response = categoryController.updateCategory(1L, categoryDto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Category 1", response.getBody().getData().getName());

        verify(categoryMapper, times(1)).toCategory(any(CategoryDto.class));
        verify(categoryService, times(1)).updateCategory(eq(1L), any(Category.class));
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    public void testDeleteCategory() {
        ResponseEntity<Void> response = categoryController.deleteCategory(1L);

        assertEquals(204, response.getStatusCodeValue());

        verify(categoryService, times(1)).deleteCategory(1L);
    }
}