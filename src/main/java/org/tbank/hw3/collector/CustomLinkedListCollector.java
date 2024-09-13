package org.tbank.hw3.collector;

import org.tbank.hw3.CustomLinkedList.CustomLinkedList;

import java.util.stream.Collector;

public class CustomLinkedListCollector {
    public static <T> Collector<T, ?, CustomLinkedList<T>> toCustomLinkedList() {
        return Collector.of(
                CustomLinkedList::new,
                CustomLinkedList::add,
                (left, right) -> {
                    throw new UnsupportedOperationException("Parallel processing is not supported");
                },
                Collector.Characteristics.IDENTITY_FINISH
        );
    }
}
