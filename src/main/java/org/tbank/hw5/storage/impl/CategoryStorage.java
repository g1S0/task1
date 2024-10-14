package org.tbank.hw5.storage.impl;

import org.springframework.stereotype.Repository;
import org.tbank.hw5.model.Category;
import org.tbank.hw5.storage.LocalStorage;

@Repository
public class CategoryStorage extends LocalStorage<Long, Category> {
}
