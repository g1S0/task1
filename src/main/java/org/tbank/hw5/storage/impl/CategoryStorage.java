package org.tbank.hw5.storage.impl;

import org.springframework.stereotype.Repository;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.storage.DataObserver;
import org.tbank.hw5.storage.LocalStorage;

import java.util.List;

@Repository
public class CategoryStorage extends LocalStorage<Long, Category> implements DataObserver<Category> {
    @Override
    public void update(List<Category> elements) {
        for (Category category : elements) {
            save(category.getId(), category);
        }
    }
}
