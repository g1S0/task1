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

    private static final Long CATEGORY_ID_1 = 1L;
    private static final Long CATEGORY_ID_2 = 2L;
    private static final String CATEGORY_SLUG_1 = "airports";
    private static final String CATEGORY_SLUG_2 = "Airports";
    private static final String CATEGORY_NAME_1 = "amusement";
    private static final String CATEGORY_NAME_2 = "Amusement parks (Entertainment)";
    private static final String UPDATED_CATEGORY_NAME = "AntiCafe";
    private static final String ENTITY_NOT_FOUND_MESSAGE = "Entity not found";
    private static final String ENTITY_ALREADY_EXISTS_MESSAGE = "Entity already exists";

    @Mock
    private CategoryStorage categoryStorage;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category createCategory(Long id, String slug, String name) {
        return new Category(id, slug, name);
    }

    private Category getCategory() {
        return createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);
    }

    @Test
    public void testGetAllCategories_PositiveScenario() {
        List<Category> mockCategories = List.of(
                createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1),
                createCategory(CATEGORY_ID_2, CATEGORY_SLUG_2, CATEGORY_NAME_2)
        );

        when(categoryStorage.findAll()).thenReturn(mockCategories);

        List<Category> categories = categoryService.getAllCategories();

        assertEquals(2, categories.size());
        assertEquals(CATEGORY_NAME_1, categories.get(0).getName());
    }

    @Test
    public void testGetAllCategories_EmptyResult() {
        when(categoryStorage.findAll()).thenReturn(Collections.emptyList());

        List<Category> categories = categoryService.getAllCategories();

        assertTrue(categories.isEmpty());
    }

    @Test
    public void testGetCategoryById_PositiveScenario() {
        when(categoryStorage.findById(CATEGORY_ID_1)).thenReturn(getCategory());

        Category category = categoryService.getCategoryById(CATEGORY_ID_1);

        assertNotNull(category);
        assertEquals(CATEGORY_NAME_1, category.getName());
    }

    @Test
    public void testGetCategoryById_NegativeScenario() {
        when(categoryStorage.findById(CATEGORY_ID_1)).thenReturn(null);

        Category category = categoryService.getCategoryById(CATEGORY_ID_1);

        assertNull(category);
    }

    @Test
    public void testCreateCategory_PositiveScenario() throws EntityAlreadyExistsException {
        Category newCategory = getCategory();

        when(categoryStorage.save(CATEGORY_ID_1, newCategory)).thenReturn(newCategory);

        Category savedCategory = categoryService.createCategory(newCategory);

        assertEquals(newCategory, savedCategory);
    }

    @Test
    public void testCreateCategory_NegativeScenario() throws EntityAlreadyExistsException {
        Category newCategory = getCategory();

        when(categoryStorage.save(CATEGORY_ID_1, newCategory))
                .thenThrow(new EntityAlreadyExistsException(ENTITY_ALREADY_EXISTS_MESSAGE));

        assertThrows(EntityAlreadyExistsException.class, () -> categoryService.createCategory(newCategory));
    }

    @Test
    public void testUpdateCategory_PositiveScenario() {
        Category updatedCategory = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, UPDATED_CATEGORY_NAME);

        when(categoryStorage.update(CATEGORY_ID_1, CATEGORY_ID_1, updatedCategory)).thenReturn(updatedCategory);

        Category result = categoryService.updateCategory(CATEGORY_ID_1, updatedCategory);

        assertEquals(UPDATED_CATEGORY_NAME, result.getName());
    }

    @Test
    public void testUpdateCategory_NegativeScenario() {
        Category updatedCategory = getCategory();

        when(categoryStorage.update(CATEGORY_ID_1, CATEGORY_ID_1, updatedCategory))
                .thenThrow(new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE));

        assertThrows(EntityNotFoundException.class, () -> categoryService.updateCategory(CATEGORY_ID_1, updatedCategory));
    }

    @Test
    public void testDeleteCategory_PositiveScenario() {
        Mockito.doNothing().when(categoryStorage).deleteById(CATEGORY_ID_1);

        categoryService.deleteCategory(CATEGORY_ID_1);

        Mockito.verify(categoryStorage, Mockito.times(1)).deleteById(CATEGORY_ID_1);
    }

    @Test
    public void testDeleteCategory_NegativeScenario() {
        doThrow(new EntityNotFoundException(ENTITY_NOT_FOUND_MESSAGE)).when(categoryStorage).deleteById(CATEGORY_ID_1);

        assertThrows(EntityNotFoundException.class, () -> categoryService.deleteCategory(CATEGORY_ID_1));
    }
}