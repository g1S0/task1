package org.tbank.hw3;

import org.tbank.hw3.CustomLinkedList.CustomLinkedList;
import org.tbank.hw3.collector.CustomLinkedListCollector;

import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        operateCustomLinkedList();
        fillCustomLinkedListWithStreamApiAndCollectMethod();
        fillCustomLinkedListWithStreamApi();
        useReduceMethodForTerminateOperations();
    }

    public static void operateCustomLinkedList() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        Integer value = list.get(0);
        System.out.println(value);
        boolean res = list.contains(0);
        System.out.println(res);
        list.remove(0);
        ArrayList<Integer> data = new ArrayList<>();
        data.add(1);
        data.add(2);
        list.addAll(data);
    }

    public static CustomLinkedList<Integer> fillCustomLinkedListWithStreamApiAndCollectMethod() {
        return Stream.of(1, 2, 3, 4, 5)
                .collect(CustomLinkedListCollector.toCustomLinkedList());
    }

    public static CustomLinkedList<Integer> fillCustomLinkedListWithStreamApi() {
        Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5);

        return stream.reduce(
                new CustomLinkedList<>(),
                (list, item) -> {
                    list.add(item);
                    return list;
                },
                (list1, list2) -> {
                    list1.addAll(list2);
                    return list1;
                }
        );
    }

    public static int useReduceMethodForTerminateOperations() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);

        Optional<Integer> sum = numbers.stream()
                .reduce(Integer::sum);

        return sum.orElse(0);
    }
}
