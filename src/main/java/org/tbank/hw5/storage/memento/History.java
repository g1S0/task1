package org.tbank.hw5.storage.memento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class History<K, T> {
    private final Map<K, List<Memento<T>>> history = new ConcurrentHashMap<>();

    public void addMemento(K id, Memento<T> memento) {
        history.computeIfAbsent(id, k -> new ArrayList<>()).add(memento);
    }

    public List<Memento<T>> getHistory(K id) {
        return history.getOrDefault(id, Collections.emptyList());
    }
}