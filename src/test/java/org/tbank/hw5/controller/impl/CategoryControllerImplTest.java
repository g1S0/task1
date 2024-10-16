package org.tbank.hw5.controller.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.tbank.hw5.controller.CategoryController;
import org.tbank.hw5.dto.CategoryDto;
import org.tbank.hw5.dto.ResponseDto;
import org.tbank.hw5.mapper.CategoryMapper;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.service.CategoryService;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerImplTest {

    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_SLUG = "airports";
    private static final String CATEGORY_NAME = "Airports";
    private static final int STATUS_OK = 200;
    private static final int STATUS_NO_CONTENT = 204;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryController categoryController;

    private final Category category = new Category(CATEGORY_ID, CATEGORY_SLUG, CATEGORY_NAME);
    private final CategoryDto categoryDto = new CategoryDto(CATEGORY_ID, CATEGORY_SLUG, CATEGORY_NAME);

    @Test
    public void testGetAllCategories() {
        when(categoryService.getAllCategories()).thenReturn(List.of(category));
        when(categoryMapper.toCategoryDtoList(anyList())).thenReturn(List.of(categoryDto));

        ResponseEntity<ResponseDto<List<CategoryDto>>> response = categoryController.getAllCategories();

        assertEquals(STATUS_OK, response.getStatusCode().value());
        assertEquals(1, Objects.requireNonNull(response.getBody()).getData().size());
        assertEquals(CATEGORY_NAME, response.getBody().getData().get(0).getName());

        verify(categoryService, times(1)).getAllCategories();
        verify(categoryMapper, times(1)).toCategoryDtoList(anyList());
    }

    @Test
    public void testGetCategoryById() {
        when(categoryService.getCategoryById(CATEGORY_ID)).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        ResponseEntity<ResponseDto<CategoryDto>> response = categoryController.getCategoryById(CATEGORY_ID);

        assertEquals(STATUS_OK, response.getStatusCode().value());
        assertEquals(CATEGORY_NAME, Objects.requireNonNull(response.getBody()).getData().getName());

        verify(categoryService, times(1)).getCategoryById(CATEGORY_ID);
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    public void testCreateCategory() {
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryService.createCategory(any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        ResponseEntity<ResponseDto<CategoryDto>> response = categoryController.createCategory(categoryDto);

        assertEquals(STATUS_OK, response.getStatusCode().value());
        assertEquals(CATEGORY_NAME, Objects.requireNonNull(response.getBody()).getData().getName());

        verify(categoryMapper, times(1)).toCategory(any(CategoryDto.class));
        verify(categoryService, times(1)).createCategory(any(Category.class));
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    public void testUpdateCategory() {
        when(categoryMapper.toCategory(any(CategoryDto.class))).thenReturn(category);
        when(categoryService.updateCategory(eq(CATEGORY_ID), any(Category.class))).thenReturn(category);
        when(categoryMapper.toCategoryDto(any(Category.class))).thenReturn(categoryDto);

        ResponseEntity<ResponseDto<CategoryDto>> response = categoryController.updateCategory(CATEGORY_ID, categoryDto);

        assertEquals(STATUS_OK, response.getStatusCode().value());
        assertEquals(CATEGORY_NAME, Objects.requireNonNull(response.getBody()).getData().getName());

        verify(categoryMapper, times(1)).toCategory(any(CategoryDto.class));
        verify(categoryService, times(1)).updateCategory(eq(CATEGORY_ID), any(Category.class));
        verify(categoryMapper, times(1)).toCategoryDto(any(Category.class));
    }

    @Test
    public void testDeleteCategory() {
        ResponseEntity<Void> response = categoryController.deleteCategory(CATEGORY_ID);

        assertEquals(STATUS_NO_CONTENT, response.getStatusCode().value());

        verify(categoryService, times(1)).deleteCategory(CATEGORY_ID);
    }
}