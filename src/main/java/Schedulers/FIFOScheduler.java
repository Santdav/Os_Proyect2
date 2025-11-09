/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Schedulers;
import DataStructures.Queue;
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
    public Process selectNextProcess(Queue<Process> readyQueue) {
        if (readyQueue == null || readyQueue.isEmpty()) {
            return null;
        }
        
        // FIFO simplemente toma el primer proceso de la cola
        return readyQueue.dequeue();
    }
    
    @Override
    public String getAlgorithmName() {
        return "FIFO (First-In, First-Out)";
    }
    
    @Override
    public String getAlgorithmDescription() {
        return "Selecciona procesos en el orden de llegada. " +
               "El primer proceso en entrar a la cola es el primero en ser ejecutado.";
    }
    
    @Override
    public String toString() {
        return getAlgorithmName();
    }
}