package org.tbank.hw5.service.impl;

import org.springframework.stereotype.Service;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.service.CategoryService;
import org.tbank.hw5.storage.impl.CategoryStorage;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryStorage categoryStorage;

    public CategoryServiceImpl(CategoryStorage categoryStorage) {
        this.categoryStorage = categoryStorage;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryStorage.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryStorage.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryStorage.save(category.getId(), category);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        return categoryStorage.update(category.getId(), id, category);
    }

    @Override
    public void deleteCategory(Long id) {
        categoryStorage.deleteById(id);
    }
}
