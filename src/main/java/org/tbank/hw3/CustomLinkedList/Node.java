package org.tbank.hw3.CustomLinkedList;

public class Node<E> {
    public E data;
    public Node<E> next;
    public Node<E> prev;

    public Node(E item) {
        this.data = item;
        this.next = null;
        this.prev = null;
    }
}
