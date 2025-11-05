/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;
import java.util.Iterator;
import java.util.NoSuchElementException;
/**
 *
 * @author santi
 */


/**
 * A custom implementation of a FIFO (First-In-First-Out) queue using generics.
 * This implementation provides standard queue operations and supports iteration.
 *
 * @param <E> the type of elements held in this queue
 */
public class Queue<E> implements Iterable<E> {
    
    // Node class representing individual elements in the queue
    private static class Node<E> {
        E data;
        Node<E> next;

        /**
         * Constructs a new node with the given data.
         *
         * @param data the element to store in this node
         */
        Node(E data) {
            this.data = data;
        }
    }

    private Node<E> front;
    private Node<E> rear;
    private int size;

    // ==================== CONSTRUCTORS ====================

    /**
     * Constructs an empty queue.
     */
    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Constructs a queue containing the elements of the specified array.
     *
     * @param elements the array whose elements are to be placed into this queue
     */
    @SafeVarargs
    public Queue(E... elements) {
        this();
        for (E element : elements) {
            enqueue(element);
        }
    }

    // ==================== BASIC QUEUE OPERATIONS ====================

    /**
     * Inserts the specified element into this queue.
     *
     * @param element the element to add
     */
    public void enqueue(E element) {
        Node<E> newNode = new Node<>(element);
        if (isEmpty()) {
            front = newNode;
        } else {
            rear.next = newNode;
        }
        rear = newNode;
        size++;
    }

    /**
     * Retrieves and removes the head of this queue.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        
        E removedData = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return removedData;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }

    /**
     * Retrieves, but does not remove, the head of this queue.
     * This method is an alias for peek().
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    public E front() {
        return peek();
    }

    // ==================== UTILITY METHODS ====================

    /**
     * Returns the number of elements in this queue.
     *
     * @return the number of elements in this queue
     */
    public int size() {
        return size;
    }

    /**
     * Tests if this queue is empty.
     *
     * @return true if this queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from this queue.
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }

    /**
     * Tests if this queue contains the specified element.
     *
     * @param element the element whose presence in this queue is to be tested
     * @return true if this queue contains the specified element
     */
    public boolean contains(E element) {
        Node<E> current = front;
        while (current != null) {
            if (current.data.equals(element)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * Returns the position of the first occurrence of the specified element in this queue.
     * The front of the queue is at position 0.
     *
     * @param element the element to search for
     * @return the position of the element, or -1 if not found
     */
    public int positionOf(E element) {
        Node<E> current = front;
        int position = 0;
        while (current != null) {
            if (current.data.equals(element)) {
                return position;
            }
            current = current.next;
            position++;
        }
        return -1;
    }

    // ==================== ITERABLE IMPLEMENTATION ====================

    /**
     * Returns an iterator over elements in this queue in proper sequence.
     * The elements are returned from front to rear.
     *
     * @return an iterator over elements in this queue in proper sequence
     */
    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<E> {
        private Node<E> current = front;

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

    // ==================== CONVERSION METHODS ====================

    /**
     * Returns an array containing all of the elements in this queue in proper sequence.
     *
     * @return an array containing all of the elements in this queue
     */
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<E> current = front;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    /**
     * Returns an array containing all of the elements in this queue in proper sequence.
     * The runtime type of the returned array is that of the specified array.
     *
     * @param array the array into which the elements of the queue are to be stored
     * @return an array containing the elements of the queue
     * @throws ArrayStoreException if the runtime type of the specified array is not a supertype of E
     * @throws NullPointerException if the specified array is null
     */
    @SuppressWarnings("unchecked")
    public E[] toArray(E[] array) {
        if (array.length < size) {
            array = (E[]) java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
        }
        
        Node<E> current = front;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        
        if (array.length > size) {
            array[size] = null;
        }
        
        return array;
    }

    // ==================== OBJECT METHODS ====================

    /**
     * Returns a string representation of this queue.
     * The string representation consists of a list of the queue's elements
     * in the order they would be dequeued (front to rear), enclosed in square brackets ("[]").
     *
     * @return a string representation of this queue
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = front;
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

    /**
     * Compares the specified object with this queue for equality.
     * Returns true if both queues contain the same elements in the same order.
     *
     * @param obj the object to be compared for equality with this queue
     * @return true if the specified object is equal to this queue
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Queue<?> other = (Queue<?>) obj;
        if (this.size != other.size) return false;
        
        Node<E> currentThis = this.front;
        Node<?> currentOther = other.front;
        
        while (currentThis != null && currentOther != null) {
            if (!currentThis.data.equals(currentOther.data)) {
                return false;
            }
            currentThis = currentThis.next;
            currentOther = currentOther.next;
        }
        
        return true;
    }


    // ==================== QUEUE-SPECIFIC UTILITIES ====================

    /**
     * Creates and returns a copy of this queue.
     *
     * @return a copy of this queue
     */
    public Queue<E> copy() {
        Queue<E> copy = new Queue<>();
        Node<E> current = front;
        while (current != null) {
            copy.enqueue(current.data);
            current = current.next;
        }
        return copy;
    }

    /**
     * Reverses the order of elements in this queue.
     * After calling this method, the element at the front becomes the rear and vice versa.
     */
    public void reverse() {
        if (size <= 1) return;
        
        Node<E> prev = null;
        Node<E> current = front;
        Node<E> next = null;
        
        // Swap front and rear
        Node<E> temp = front;
        front = rear;
        rear = temp;
        
        // Reverse the links
        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
    }

}
