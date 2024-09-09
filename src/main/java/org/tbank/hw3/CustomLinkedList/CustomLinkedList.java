package org.tbank.hw3.CustomLinkedList;

import java.util.LinkedList;
import java.util.List;

public class CustomLinkedList<E> {
    Node<E> head;
    Node<E> tail;
    int size = 0;

    public CustomLinkedList() {
        this.head = null;
        this.tail = null;
    }

    public void add(E data) {
        Node<E> temp = new Node<>(data);
        if (tail == null) {
            head = temp;
            tail = temp;
        } else {
            tail.next = temp;
            temp.prev = tail;
            tail = temp;
        }
        size++;
    }

    private Node<E> getNode(int index) {
        checkIndex(index);
        Node<E> current;

        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    public E get(int index) {
        checkIndex(index);
        Node<E> current = getNode(index);

        return current.data;
    }

    public void remove(int index) {
        checkIndex(index);

        Node<E> toRemove = getNode(index);

        if (toRemove.prev != null) {
            toRemove.prev.next = toRemove.next;
        } else {
            head = toRemove.next;
        }

        if (toRemove.next != null) {
            toRemove.next.prev = toRemove.prev;
        } else {
            tail = toRemove.prev;
        }

        size--;
    }

    public boolean contains(E data) {
        Node<E> current = head;
        while (current != null) {
            if (current.data.equals(data)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public void addAll(CustomLinkedList<E> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Input list cannot be null");
        }

        Node<E> current = collection.head;
        while (current != null) {
            this.add(current.data);
            current = current.next;
        }
    }

    public void addAll(List<E> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Input list cannot be null");
        }


        for (E item : collection) {
            add(item);
        }
    }

    public void printAll() {
        Node<E> current = head;
        while (current != null) {
            System.out.print(current.data);
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println();
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    public int size() {
        return size;
    }

    public E peekTail() {
        final Node<E> t = tail;
        return (t == null) ? null : tail.data;
    }

    public E peekHead() {
        final Node<E> h = head;
        return (h == null) ? null : h.data;
    }

}
