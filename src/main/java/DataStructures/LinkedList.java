/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A custom implementation of a singly linked list using generics.
 * @param <E> the type of elements held in this list
 */
public class LinkedList<E> implements Iterable<E> {
    
    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> head;
    private Node<E> tail;
    private int size;

    // ==================== CONSTRUCTORS ====================

    /**
     * Constructs an empty linked list.
     */
    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // ==================== BASIC OPERATIONS ====================

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * Tests if this list is empty.
     *
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from this list.
     */
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // ==================== INSERTION METHODS ====================

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element the element to be appended
     */
    public void add(E element) {
        addLast(element);
    }
    
    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param element the element to be inserted at the beginning
     */
    public void addFirst(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element the element to be appended to the end
     */
    public void addLast(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     *
     * @param index   the index at which the specified element is to be inserted
     * @param element the element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size())
     */
    public void insert(int index, E element) {
        checkPositionIndex(index);
        
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            addLast(element);
        } else {
            Node<E> newNode = new Node<>(element);
            Node<E> prev = getNode(index - 1);
            newNode.next = prev.next;
            prev.next = newNode;
            size++;
        }
    }

    // ==================== DELETION METHODS ====================

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }
        
        E removedData = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return removedData;
    }

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if this list is empty
     */
    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot remove from empty list");
        }
        
        if (size == 1) {
            return removeFirst();
        }
        
        Node<E> prev = getNode(size - 2);
        E removedData = tail.data;
        tail = prev;
        tail.next = null;
        size--;
        return removedData;
    }

    /**
     * Removes the element at the specified position in this list.
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E removeAt(int index) {
        checkElementIndex(index);
        
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            Node<E> prev = getNode(index - 1);
            E removedData = prev.next.data;
            prev.next = prev.next.next;
            size--;
            return removedData;
        }
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present.
     *
     * @param element the element to be removed from this list, if present
     * @return true if this list contained the specified element
     */
    public boolean remove(E element) {
        if (isEmpty()) {
            return false;
        }
        
        // Handle removal of first element
        if (head.data.equals(element)) {
            removeFirst();
            return true;
        }

        // Search for element to remove
        Node<E> current = head;
        while (current.next != null) {
            if (current.next.data.equals(element)) {
                current.next = current.next.next;
                if (current.next == null) {
                    tail = current;
                }
                size--;
                return true;
            }
            current = current.next;
        }
        
        return false;
    }

    /**
     * Removes all elements from this list that are equal to the specified element.
     *
     * @param element the element to be completely removed from this list
     * @return true if at least one element was removed
     */
    public boolean removeAll(E element) {
        boolean removed = false;
        while (remove(element)) {
            removed = true;
        }
        return removed;
    }

    // ==================== SEARCH METHODS ====================

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E get(int index) {
        return getNode(index).data;
    }

    /**
     * Returns the element at the specified position in this list.
     * This method provides array-like access syntax.
     *
     * @param index the index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E getAt(int index) {
        return get(index);
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index   the index of the element to replace
     * @param element the element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size())
     */
    public E set(int index, E element) {
        Node<E> node = getNode(index);
        E oldValue = node.data;
        node.data = element;
        return oldValue;
    }

    /**
     * Tests if this list contains the specified element.
     *
     * @param element the element whose presence in this list is to be tested
     * @return true if this list contains the specified element
     */
    public boolean contains(E element) {
        return find(element) != -1;
    }

    /**
     * Finds the index of the first occurrence of the specified element in this list.
     *
     * @param element the element to search for
     * @return the index of the first occurrence of the element, or -1 if not found
     */
    public int find(E element) {
        Node<E> current = head;
        int index = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /**
     * Finds the index of the first occurrence of the specified element in this list.
     * This is an alias for the find method.
     *
     * @param element the element to search for
     * @return the index of the first occurrence of the element, or -1 if not found
     */
    public int indexOf(E element) {
        return find(element);
    }

    // ==================== ITERABLE IMPLEMENTATION ====================

    /**
     * Returns an iterator over elements of type E.
     *
     * @return an iterator for this linked list
     */
    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }

    private class LinkedListIterator implements Iterator<E> {
        private Node<E> current = head;

        /**
         * Returns true if the iteration has more elements.
         *
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E data = current.data;
            current = current.next;
            return data;
        }
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Returns an array containing all of the elements in this list in proper sequence.
     *
     * @return an array containing all of the elements in this list
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = head;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    /**
     * Returns a string representation of this list.
     * The string representation consists of a list of the list's elements
     * in the order they are returned by its iterator, enclosed in square brackets ("[]").
     *
     * @return a string representation of this list
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }

    // ==================== PRIVATE HELPER METHODS ====================

    /**
     * Returns the node at the specified position in this list.
     *
     * @param index the index of the node to return
     * @return the node at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private Node<E> getNode(int index) {
        checkElementIndex(index);
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current;
    }

    /**
     * Checks if the given index is a valid index for element access.
     *
     * @param index the index to check
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    /**
     * Checks if the given index is a valid position for insertion.
     *
     * @param index the index to check
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void checkPositionIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
}