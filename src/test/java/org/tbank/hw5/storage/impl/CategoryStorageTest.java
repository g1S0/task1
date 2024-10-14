package org.tbank.hw5.storage.impl;

import org.junit.jupiter.api.Test;
import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.model.Category;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStorageTest {

    private static final Long CATEGORY_ID_1 = 123L;
    private static final Long CATEGORY_ID_2 = 456L;
    private static final String CATEGORY_SLUG_1 = "airport";
    private static final String CATEGORY_SLUG_2 = "station";
    private static final String CATEGORY_NAME_1 = "Airport";
    private static final String CATEGORY_NAME_2 = "Station";
    private static final String UPDATED_CATEGORY_SLUG = "port";
    private static final String UPDATED_CATEGORY_NAME = "Port";

    public CategoryStorage getCategoryStorage() {
        return new CategoryStorage();
    }

    private Category createCategory(Long id, String slug, String name) {
        return new Category(id, slug, name);
    }

    @Test
    void testSaveAndFindById() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);

        categoryStorage.save(CATEGORY_ID_1, category);
        Category foundCategory = categoryStorage.findById(CATEGORY_ID_1);

        assertNotNull(foundCategory);
        assertEquals(CATEGORY_ID_1, foundCategory.getId());
        assertEquals(CATEGORY_SLUG_1, foundCategory.getSlug());
        assertEquals(CATEGORY_NAME_1, foundCategory.getName());
    }

    @Test
    void testFindAll() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category1 = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);
        Category category2 = createCategory(CATEGORY_ID_2, CATEGORY_SLUG_2, CATEGORY_NAME_2);

        categoryStorage.save(CATEGORY_ID_1, category1);
        categoryStorage.save(CATEGORY_ID_2, category2);
        List<Category> categories = categoryStorage.findAll();

        assertEquals(2, categories.size());
        assertTrue(categories.contains(category1));
        assertTrue(categories.contains(category2));
    }

    @Test
    void testUpdate() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);
        categoryStorage.save(CATEGORY_ID_1, category);

        Category updatedCategory = createCategory(CATEGORY_ID_1, UPDATED_CATEGORY_SLUG, UPDATED_CATEGORY_NAME);

        categoryStorage.update(updatedCategory.getId(), category.getId(), updatedCategory);
        Category foundCategory = categoryStorage.findById(CATEGORY_ID_1);

        assertNotNull(foundCategory);
        assertEquals(UPDATED_CATEGORY_SLUG, foundCategory.getSlug());
        assertEquals(UPDATED_CATEGORY_NAME, foundCategory.getName());
    }

    @Test
    void testDeleteById() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);
        categoryStorage.save(CATEGORY_ID_1, category);

        categoryStorage.deleteById(CATEGORY_ID_1);
        Category foundCategory = categoryStorage.findById(CATEGORY_ID_1);

        assertNull(foundCategory);
    }

    @Test
    void testSaveDuplicateIdThrowsException() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);
        categoryStorage.save(CATEGORY_ID_1, category);

        Category duplicateCategory = createCategory(CATEGORY_ID_1, UPDATED_CATEGORY_SLUG, UPDATED_CATEGORY_NAME);

        assertThrows(EntityAlreadyExistsException.class, () -> {
            categoryStorage.save(CATEGORY_ID_1, duplicateCategory);
        });
    }

    @Test
    void testUpdateWithDifferentIdsThrowsException() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category category = createCategory(CATEGORY_ID_1, CATEGORY_SLUG_1, CATEGORY_NAME_1);
        categoryStorage.save(CATEGORY_ID_1, category);

        Category updatedCategory = createCategory(CATEGORY_ID_2, UPDATED_CATEGORY_SLUG, UPDATED_CATEGORY_NAME);

        assertThrows(EntityNotFoundException.class, () -> {
            categoryStorage.update(updatedCategory.getId(), category.getId(), updatedCategory);
        });
    }

    @Test
    void testUpdateNonExistentEntityThrowsException() {
        CategoryStorage categoryStorage = getCategoryStorage();

        Category updatedCategory = createCategory(CATEGORY_ID_1, UPDATED_CATEGORY_SLUG, UPDATED_CATEGORY_NAME);

        assertThrows(EntityNotFoundException.class, () -> {
            categoryStorage.update(CATEGORY_ID_1, CATEGORY_ID_1, updatedCategory);
        });
    }

    @Test
    void testDeleteNonExistentCategoryThrowsException() {
        CategoryStorage categoryStorage = getCategoryStorage();

        assertThrows(EntityNotFoundException.class, () -> {
            categoryStorage.deleteById(CATEGORY_ID_1);
        });
    }
}