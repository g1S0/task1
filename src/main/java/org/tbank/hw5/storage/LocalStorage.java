package org.tbank.hw5.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.tbank.hw5.exception.EntityAlreadyExistsException;
import org.tbank.hw5.exception.EntityNotFoundException;
import org.tbank.hw5.storage.memento.History;
import org.tbank.hw5.storage.memento.Memento;
import org.tbank.hw5.storage.memento.OperationType;

public class LocalStorage<K, T> {
    private final Map<K, T> storage = new ConcurrentHashMap<>();
    private final History<K, T> history = new History<>();

    public T save(K id, T entity) throws EntityAlreadyExistsException {
        if (storage.containsKey(id)) {
            throw new EntityAlreadyExistsException("Entity with id " + id + " already exists.");
        }

        Memento<T> memento = new Memento<>(entity, OperationType.CREATE);
        history.addMemento(id, memento);

        storage.put(id, entity);
        return entity;
    }

    public T findById(K id) {
        return storage.get(id);
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public T update(K newId, K oldId, T entity) {
        if (!storage.containsKey(oldId) || !oldId.equals(newId)) {
            throw new EntityNotFoundException("Can not update entity");
        }

        Memento<T> memento = new Memento<>(entity, OperationType.UPDATE);
        history.addMemento(oldId, memento);

        storage.put(oldId, entity);
        return entity;
    }

    public void deleteById(K id) {
        if (!storage.containsKey(id)) {
            throw new EntityNotFoundException("Can not delete entity");
        }

        Memento<T> memento = new Memento<>(storage.get(id), OperationType.DELETE);
        history.addMemento(id, memento);

        storage.remove(id);
    }

    public List<Memento<T>> getHistory(K id) {
        return history.getHistory(id);
    }
}
