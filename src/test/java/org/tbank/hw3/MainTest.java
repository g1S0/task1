package org.tbank.hw3;

import org.junit.jupiter.api.Test;
import org.tbank.hw3.CustomLinkedList.CustomLinkedList;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    public void testFillCustomLinkedListWithStreamApi() {
        CustomLinkedList<Integer> customLinkedList = Main.fillCustomLinkedListWithStreamApi();

        assertEquals(5, customLinkedList.size());

        assertEquals(Integer.valueOf(1), customLinkedList.get(0));
        assertEquals(Integer.valueOf(2), customLinkedList.get(1));
        assertEquals(Integer.valueOf(3), customLinkedList.get(2));
        assertEquals(Integer.valueOf(4), customLinkedList.get(3));
        assertEquals(Integer.valueOf(5), customLinkedList.get(4));
    }
}