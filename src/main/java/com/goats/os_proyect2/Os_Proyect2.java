/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.goats.os_proyect2;
import DataStructures.*;
import Testing.*;
/**
 *
 * @author santi
 */
public class Os_Proyect2 {

    public static void main(String[] args) {
        
        /* PRUEBAS DE FILE SYSTEM
        FileSystemTest tester = new FileSystemTest();
        
        tester.diagnosticTest(); //Pueba en caso de Modificar cosas en el core de archivos
        
        tester.quickTest(); //una version mas ligera
        
        tester.runAllTests(); // Prueba COMPLETA con todo y todo
        */
        
        /*
        //PRUEBAS DE PROCESS
        ProcessTest.testProcessCreation();
        ProcessTest.testProcessStates();
        ProcessTest.testProcessOperations();
        ProcessTest.testProcessTiming();
        */
        
        //ProcessManagerTest.runAllTests();
        
        DiskSchedulerTest.runAllTest();
        
    }
}
