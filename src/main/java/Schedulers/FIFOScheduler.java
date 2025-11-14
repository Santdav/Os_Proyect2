/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Schedulers;
import DataStructures.LinkedList;
import DataStructures.Queue;
import LogicalStrucures.IORequest;
import LogicalStrucures.Process;
/**
 *
 * @author santi
 */
/**
 * Implementación del algoritmo de planificación FIFO (First-In, First-Out).
 * También conocido como FCFS (First-Come, First-Served).
 * 
 * Selecciona los procesos en el orden en que llegaron a la cola.
 */
public class FIFOScheduler implements DiskScheduler {
    
    @Override
    public Process selectNextProcess(LinkedList<Process> requestQueue) {
        if (requestQueue.isEmpty()) {
            return null;
        }
        
        // FIFO: siempre toma la primera solicitud que llegó
        Process nextProcess = requestQueue.get(0);
        System.out.println("DEBUG FIFO - Procesando: " + nextProcess.getProcessId() + 
                          " - " + nextProcess.getOperationDescription());
        
        return nextProcess;
    }
    
    @Override
    public String getAlgorithmName() {
        return "FIFO (First-In, First-Out)";
    }
    
    @Override
    public String getAlgorithmDescription() {
        return "Atiende las solicitudes en el orden exacto de llegada";
    }
}