package org.tbank.hw5.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.storage.impl.CategoryStorage;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryStorage categoryStorage;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category getCategory() {
        return new Category(1L, "slug1", "Category 1");
    }

    @Test
    public void testGetAllCategories_PositiveScenario() {
        List<Category> mockCategories = List.of(
                new Category(1L, "slug1", "Category 1"),
                new Category(2L, "slug2", "Category 2")
        );

        when(categoryStorage.findAll()).thenReturn(mockCategories);

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        assertEquals("Category 1", categories.get(0).getName());
    }

    @Test
    public void testGetAllCategories_EmptyResult() {
        when(categoryStorage.findAll()).thenReturn(Collections.emptyList());

        List<Category> categories = categoryService.getAllCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    public void testGetCategoryById_PositiveScenario() {
        when(categoryStorage.findById(1L)).thenReturn(getCategory());

        Category category = categoryService.getCategoryById(1L);

        assertNotNull(category);
        assertEquals("Category 1", category.getName());
    }

    @Test
    public void testGetCategoryById_NegativeScenario() {
        when(categoryStorage.findById(1L)).thenReturn(null);

        Category category = categoryService.getCategoryById(1L);

        assertNull(category);
    }

    @Test
    public void testCreateCategory_PositiveScenario() throws EntityAlreadyExistsException {
        Category newCategory = getCategory();

        when(categoryStorage.save(1L, newCategory)).thenReturn(newCategory);

        Category savedCategory = categoryService.createCategory(newCategory);

        assertEquals(newCategory, savedCategory);
    }

    @Test
    public void testCreateCategory_NegativeScenario() throws EntityAlreadyExistsException {
        Category newCategory = getCategory();

        when(categoryStorage.save(1L, newCategory))
                .thenThrow(new EntityAlreadyExistsException("Entity already exists"));

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.createCategory(newCategory));
    }

    @Test
    public void testUpdateCategory_PositiveScenario() {
        Category updatedCategory = new Category(1L, "slug1", "Updated Category");

        when(categoryStorage.update(1L, 1L, updatedCategory)).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(1L, updatedCategory);

        assertEquals("Updated Category", result.getName());
    }

    @Test
    public void testUpdateCategory_NegativeScenario() {
        Category updatedCategory = getCategory();

        when(categoryStorage.update(1L, 1L, updatedCategory))
                .thenThrow(new EntityNotFoundException("Entity not found"));

        assertThrows(EntityNotFoundException.class, () -> categoryService.updateCategory(1L, updatedCategory));
    }

    @Test
    public void testDeleteCategory_PositiveScenario() {
        Mockito.doNothing().when(categoryStorage).deleteById(1L);

        categoryService.deleteCategory(1L);

        Mockito.verify(categoryStorage, Mockito.times(1)).deleteById(1L);
    }

    @Test
    public void testDeleteCategory_NegativeScenario() {
        doThrow(new EntityNotFoundException("Entity not found")).when(categoryStorage).deleteById(1L);

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(1L));
    }
}