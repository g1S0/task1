package org.tbank.hw5.storage;

import java.util.List;

public interface DataObserver<T> {
    void update(List<T> elements);
}
