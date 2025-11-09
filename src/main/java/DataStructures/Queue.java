/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Queue<E> implements Iterable<E> {
    private Node<E> front;
    private Node<E> rear;
    private int size;

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    public Queue() {
        front = rear = null;
        size = 0;
    }

    // Añade un elemento al final de la cola
    public void enqueue(E item) {
        Node<E> newNode = new Node<>(item);
        if (isEmpty()) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Remueve y devuelve el elemento al frente de la cola
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        E data = front.data;
        front = front.next;
        if (front == null) {
            rear = null;
        }
        size--;
        return data;
    }

    // Devuelve el elemento al frente sin removerlo
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola está vacía");
        }
        return front.data;
    }

    // Devuelve el tamaño de la cola
    public int size() {
        return size;
    }

    // Verifica si la cola está vacía
    public boolean isEmpty() {
        return size == 0;
    }

    // Obtiene un elemento por índice (0-based desde el frente)
    public E get(int index) {
        checkIndex(index);
        Node<E> current = front;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Remueve la primera ocurrencia del elemento especificado
    public boolean remove(E element) {
        if (isEmpty()) return false;

        if (front.data.equals(element)) {
            dequeue();
            return true;
        }

        Node<E> current = front;
        while (current.next != null && !current.next.data.equals(element)) {
            current = current.next;
        }

        if (current.next != null) {
            if (current.next == rear) {
                rear = current;
            }
            current.next = current.next.next;
            size--;
            return true;
        }
        return false;
    }

    // Vacía la cola completamente
    public void clear() {
        front = rear = null;
        size = 0;
    }

    // Convierte la cola a un arreglo
    @SuppressWarnings("unchecked")
    public E[] toArray() {
        E[] array = (E[]) new Object[size];
        Node<E> current = front;
        for (int i = 0; i < size; i++) {
            array[i] = current.data;
            current = current.next;
        }
        return array;
    }

    // Verifica si contiene un elemento
    public boolean contains(E element) {
        for (E item : this) {
            if (item.equals(element)) {
                return true;
            }
        }
        return false;
    }

    // Implementación de Iterable
    @Override
    public Iterator<E> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<E> {
        private Node<E> current = front;

        @Override
        public boolean hasNext() {
            return current != null;
        }

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

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice: " + index + ", Tamaño: " + size);
        }
    }

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<E> current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
