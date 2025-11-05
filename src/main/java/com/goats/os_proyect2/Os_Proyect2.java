/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.goats.os_proyect2;
import DataStructures.*;
/**
 *
 * @author santi
 */
public class Os_Proyect2 {

    public static void main(String[] args) {
        // Create a queue
        Queue<String> queue = new Queue<>();
        
        // Basic operations
        queue.enqueue("Alice");
        queue.enqueue("Bob");
        queue.enqueue("Charlie");
        
        System.out.println("Queue: " + queue); // [Alice, Bob, Charlie]
        System.out.println("Size: " + queue.size()); // 3
        System.out.println("Front: " + queue.peek()); // Alice
        
        // Dequeue operations
        System.out.println("Dequeued: " + queue.dequeue()); // Alice
        System.out.println("Queue after dequeue: " + queue); // [Bob, Charlie]
        
        // Search operations
        System.out.println("Position of 'Eve': " + queue.positionOf("Eve")); // 3
        System.out.println("Contains 'David': " + queue.contains("David")); // true
        
        // Iteration
        System.out.print("Queue elements: ");
        for (String person : queue) {
            System.out.print(person + " "); // Bob Charlie David Eve Frank
        }
        System.out.println();
        
        // Utility operations
        Queue<String> copy = queue.copy();
        System.out.println("Copy: " + copy); // [Bob, Charlie, David, Eve, Frank]
        
        
        queue.reverse();
        System.out.println("After reverse: " + queue); // [Bob, Frank, Eve, David, Charlie]
        
        // Array conversion
        Object[] array = queue.toArray();
        System.out.print("Array: ");
        for (Object obj : array) {
            System.out.print(obj + " "); // Bob Frank Eve David Charlie
        }
        System.out.println();
        
        // Clear queue
        queue.clear();
        System.out.println("Queue after clear: " + queue); // []
        System.out.println("Is empty: " + queue.isEmpty()); // true
    }
}
