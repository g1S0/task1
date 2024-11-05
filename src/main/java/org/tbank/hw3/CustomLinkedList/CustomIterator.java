package org.tbank.hw3.CustomLinkedList;

import java.util.function.Consumer;

public interface CustomIterator<E> {
    boolean hasNext();
    E next();
    void forEachRemaining(Consumer<? super E> action);
}
