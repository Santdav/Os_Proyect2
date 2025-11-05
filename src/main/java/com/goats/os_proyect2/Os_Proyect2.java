/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.goats.os_proyect2;
import DataStructures.LinkedList;
/**
 *
 * @author santi
 */
public class Os_Proyect2 {

    public static void main(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        
        
        // Easy insertion methods
        list.add("Apple");        // Add to end
        list.addFirst("Orange");  // Add to beginning
        list.addLast("Banana");   // Add to end
        list.insert(1, "Grape");  // Insert at specific position
        
        System.out.println("List: " + list); // [Orange, Grape, Apple, Banana]
        
        // Easy search methods
        System.out.println("First: " + list.getFirst()); // Orange
        System.out.println("Last: " + list.getLast());   // Banana
        System.out.println("At index 2: " + list.get(2)); // Apple
        System.out.println("Contains 'Apple': " + list.contains("Apple")); // true
        System.out.println("Position of 'Grape': " + list.find("Grape")); // 1
        
        // Easy deletion methods
        System.out.println("Removed first: " + list.removeFirst()); // Orange
        System.out.println("Removed last: " + list.removeLast());   // Banana
        System.out.println("Removed at index 1: " + list.removeAt(1)); // Apple
        
        // Iteration with for-each
        for (String fruit : list) {
            System.out.println("Fruit: " + fruit);
        }
        
        // Array-like access
        list.add("Mango");
        list.add("Pineapple");
        System.out.println("Element at index 1: " + list.getAt(1)); // Pineapple
    }
}
