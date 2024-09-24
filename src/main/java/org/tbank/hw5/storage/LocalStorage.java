package org.tbank.hw5.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tbank.hw5.exceptions.EntityAlreadyExistsException;

public class LocalStorage<K, T> {
    private final Map<K, T> storage = new ConcurrentHashMap<>();

    public T save(K id, T entity) throws EntityAlreadyExistsException {
        if (storage.containsKey(id)) {
            throw new EntityAlreadyExistsException("Entity with id " + id + " already exists.");
        }
        storage.put(id, entity);
        return entity;
    }

    public T findById(K id) {
        return storage.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T update(K id, T entity) {
        storage.put(id, entity);
        return entity;
    }

    public void deleteById(K id) {
        storage.remove(id);
    }
}
