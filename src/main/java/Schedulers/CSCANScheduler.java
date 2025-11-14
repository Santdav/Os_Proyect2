/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Schedulers;

/**
 *
 * @author santi
 */
import LogicalStrucures.Process;
import DataStructures.LinkedList;

public class CSCANScheduler implements DiskScheduler {
    private int currentHeadPosition;
    private final int diskSize;
    
    public CSCANScheduler(int diskSize) {
        this(0, diskSize);
    }
    
    public CSCANScheduler(int startPosition, int diskSize) {
        this.currentHeadPosition = startPosition;
        this.diskSize = diskSize;
    }
    
    @Override
    public Process selectNextProcess(LinkedList<Process> requestQueue) {
        if (requestQueue.isEmpty()) {
            return null;
        }
        
        // Buscar la solicitud más cercana hacia la derecha
        Process nextProcess = findClosestRight(requestQueue);
        
        if (nextProcess == null) {
            // Si no hay solicitudes a la derecha, volver al inicio
            System.out.println("DEBUG C-SCAN - Volviendo al inicio del disco");
            nextProcess = findSmallestBlock(requestQueue);
        }
        
        if (nextProcess != null) {
            currentHeadPosition = simulateBlockNumber(nextProcess);
            System.out.println("DEBUG C-SCAN - Cabezal movido a: " + currentHeadPosition);
        }
        
        return nextProcess;
    }
    
    private Process findClosestRight(LinkedList<Process> requests) {
        Process closest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Process process : requests) {
            int block = simulateBlockNumber(process);
            if (block >= currentHeadPosition) {
                int distance = block - currentHeadPosition;
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = process;
                }
            }
        }
        
        return closest;
    }
    
    private Process findSmallestBlock(LinkedList<Process> requests) {
        Process smallest = null;
        int minBlock = Integer.MAX_VALUE;
        
        for (Process process : requests) {
            int block = simulateBlockNumber(process);
            if (block < minBlock) {
                minBlock = block;
                smallest = process;
            }
        }
        
        return smallest;
    }
    
    /**
     * Simula un número de bloque para el proceso
     */
    private int simulateBlockNumber(Process process) {
        String targetName = process.getFileName() != null ? process.getFileName() : "default";
        int hash = Math.abs(targetName.hashCode());
        return hash % diskSize;
    }
    
    @Override
    public String getAlgorithmName() {
        return "C-SCAN (Circular SCAN)";
    }
    
    @Override
    public String getAlgorithmDescription() {
        return "Versión circular de SCAN - vuelve al inicio después de llegar al final";
    }
    
    public int getCurrentHeadPosition() {
        return currentHeadPosition;
    }
}