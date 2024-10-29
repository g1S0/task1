package org.tbank.hw5.storage.memento;

import lombok.Getter;

@Getter
public class Memento<T> {
    private final T state;
    private final OperationType operationType;

    public Memento(T state, OperationType operationType) {
        this.state = state;
        this.operationType = operationType;
    }
}
