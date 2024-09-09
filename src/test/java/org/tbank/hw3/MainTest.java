package org.tbank.hw3;

import org.junit.jupiter.api.Test;
import org.tbank.hw3.CustomLinkedList.CustomLinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void fillCustomLinkedListWithStreamApiAndCollectMethod() {
        CustomLinkedList<Integer> customLinkedList = Main.fillCustomLinkedListWithStreamApiAndCollectMethod();

        assertEquals(5, customLinkedList.size());

        assertEquals(1, customLinkedList.get(0));
        assertEquals(2, customLinkedList.get(1));
        assertEquals(3, customLinkedList.get(2));
        assertEquals(4, customLinkedList.get(3));
        assertEquals(5, customLinkedList.get(4));
    }

    @Test
    public void testFillCustomLinkedListWithStreamApi() {
        CustomLinkedList<Integer> customLinkedList = Main.fillCustomLinkedListWithStreamApi();

        assertEquals(5, customLinkedList.size());

        assertEquals(1, customLinkedList.get(0));
        assertEquals(2, customLinkedList.get(1));
        assertEquals(3, customLinkedList.get(2));
        assertEquals(4, customLinkedList.get(3));
        assertEquals(5, customLinkedList.get(4));
    }

    @Test
    public void testUseReduceMethodForTerminateOperations() {
        assertEquals(21, Main.useReduceMethodForTerminateOperations());
    }
}