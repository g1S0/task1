package org.tbank.hw3.CustomLinkedListTest;

import org.tbank.hw3.CustomLinkedList.CustomLinkedList;
import org.junit.jupiter.api.Test;
import org.tbank.hw3.model.Person;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CustomLinkedListTest {
    @Test
    public void testAdd() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        assertEquals(1, list.get(0));
    }

    @Test
    public void testGet() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testGetEmptyList() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }

    @Test
    public void testRemove() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);
        list.remove(0);
        assertEquals(2, list.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    public void testRemoveEmptyList() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }

    @Test
    public void testContains() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);
        assertTrue(list.contains(1));
        assertFalse(list.contains(3));
    }

    @Test
    public void testAddAllFromCustomLinkedList() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        CustomLinkedList<Integer> otherList = new CustomLinkedList<>();
        otherList.add(1);
        otherList.add(2);
        list.addAll(otherList);

        assertTrue(list.contains(1));
        assertTrue(list.contains(2));
        assertEquals(2, list.size());
    }

    @Test
    public void testAddAllFromList() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        list.addAll(numbers);

        assertTrue(list.contains(1));
        assertTrue(list.contains(5));
        assertEquals(5, list.size());
    }

    @Test
    public void testPrintAll() {
        CustomLinkedList<Integer> list = new CustomLinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(outputStream));

        list.printAll();

        String output = outputStream.toString().trim();
        assertEquals("1 -> 2 -> 3", output);

        System.setOut(System.out);
    }

    @Test
    public void testContainsWithCustomClass() {
        CustomLinkedList<Person> list = new CustomLinkedList<>();
        Person person1 = new Person("Alice", 30);
        Person person2 = new Person("Bob", 25);
        Person person3 = new Person("Alice", 30);

        list.add(person1);
        list.add(person2);

        assertTrue(list.contains(person1));
        assertTrue(list.contains(person3));
        assertFalse(list.contains(new Person("Charlie", 40)));
    }
}