/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Schedulers;

import DataStructures.LinkedList;
import LogicalStrucures.Process;

/**
 *
 * @author santi
 */
public class SSTFScheduler implements DiskScheduler {
    private int currentHeadPosition;
    
    public SSTFScheduler() {
        this.currentHeadPosition = 0; // Empezar en bloque 0
    }
    
    public SSTFScheduler(int startPosition) {
        this.currentHeadPosition = startPosition;
    }
    
    /**
     *
     * @param requestQueue
     * @return
     */
    @Override
    public Process selectNextProcess(LinkedList<Process> requestQueue) {
        if (requestQueue.isEmpty()) {
            return null;
        }
        
        // Para SSTF necesitamos números de bloque. Como Process no los tiene,
        // vamos a simularlos basándonos en el ID del proceso y el tipo de operación
        Process closestProcess = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Process process : requestQueue) {
            // Simular un número de bloque basado en el hash del nombre del archivo
            int simulatedBlock = simulateBlockNumber(process);
            int distance = Math.abs(simulatedBlock - currentHeadPosition);
            
            if (distance < minDistance) {
                minDistance = distance;
                closestProcess = process;
            }
        }
        
        if (closestProcess != null) {
            // Actualizar posición del cabezal
            currentHeadPosition = simulateBlockNumber(closestProcess);
        }
        
        return closestProcess;
    }
    
    /**
     * Simula un número de bloque para el proceso basado en su operación y objetivo
     */
    private int simulateBlockNumber(Process process) {
        // Usar hash del nombre del archivo/directorio para simular posición
        String targetName = process.getFileName() != null ? process.getFileName() : "default";
        int hash = Math.abs(targetName.hashCode());
        return hash % 100; // Asumiendo disco de 100 bloques
    }
    
    @Override
    public String getAlgorithmName() {
        return "SSTF (Shortest Seek Time First)";
    }
    
    @Override
    public String getAlgorithmDescription() {
        return "Selecciona la solicitud más cercana a la posición actual del cabezal";
    }
    
    public int getCurrentHeadPosition() {
        return currentHeadPosition;
    }
    
    public void setCurrentHeadPosition(int position) {
        this.currentHeadPosition = position;
    }
}