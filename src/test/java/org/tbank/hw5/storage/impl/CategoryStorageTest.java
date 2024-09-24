package org.tbank.hw5.storage.impl;

import org.junit.jupiter.api.Test;
import org.tbank.hw5.exceptions.EntityAlreadyExistsException;
import org.tbank.hw5.model.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStorageTest {
    public CategoryStorage getCategoryStorage() {
        return new CategoryStorage();
    }

    @Test
    void testSaveAndFindById() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = new Category(123L, "airports", "Аэропорты");

        categoryStorage.save(123L, category);
        Category foundCategory = categoryStorage.findById(123L);

        assertNotNull(foundCategory);
        assertEquals(123L, foundCategory.getId());
        assertEquals("airports", foundCategory.getSlug());
        assertEquals("Аэропорты", foundCategory.getName());
    }

    @Test
    void testFindAll() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category1 = new Category(1L, "airports", "Аэропорты");
        Category category2 = new Category(2L, "stations", "Станции");

        categoryStorage.save(1L, category1);
        categoryStorage.save(2L, category2);
        List<Category> categories = categoryStorage.findAll();

        assertEquals(2, categories.size());
        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));
    }

    @Test
    void testUpdate() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = new Category(123L, "airports", "Аэропорты");
        categoryStorage.save(123L, category);

        Category updatedCategory = new Category(123L, "ports", "Порты");
        categoryStorage.update(123L, updatedCategory);
        Category foundCategory = categoryStorage.findById(123L);

        assertNotNull(foundCategory);
        assertEquals("ports", foundCategory.getSlug());
        assertEquals("Порты", foundCategory.getName());
    }

    @Test
    void testDeleteById() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = new Category(123L, "airports", "Аэропорты");
        categoryStorage.save(123L, category);

        categoryStorage.deleteById(123L);
        Category foundCategory = categoryStorage.findById(123L);

        assertNull(foundCategory);
    }

    @Test
    void testSaveDuplicateIdThrowsException() {
        CategoryStorage categoryStorage = new CategoryStorage();

        Category category = new Category(123L, "airports", "Аэропорты");
        categoryStorage.save(123L, category);

        Category duplicateCategory = new Category(123L, "ports", "Порты");

        assertThrows(EntityAlreadyExistsException.class, () -> {
            categoryStorage.save(123L, duplicateCategory);
        });
    }
}