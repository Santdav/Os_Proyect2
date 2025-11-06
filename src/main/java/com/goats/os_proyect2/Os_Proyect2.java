/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.goats.os_proyect2;
import DataStructures.*;
import Testing.FileSystemTest;
/**
 *
 * @author santi
 */
public class Os_Proyect2 {

    public static void main(String[] args) {
        FileSystemTest tester = new FileSystemTest();
        
        tester.diagnosticTest(); //Pueba en caso de Modificar cosas en el core de archivos
        
        tester.quickTest(); //una version mas ligera
        
        tester.runAllTests(); // Prueba COMPLETA con todo y todo
        
        
    }
}
