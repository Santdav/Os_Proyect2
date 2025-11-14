/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Schedulers;

/**
 *
 * @author santi
 */
import DataStructures.LinkedList;
import LogicalStrucures.Process;

/**
 * SCAN (Elevator) - El cabezal se mueve en una dirección y atiende solicitudes en el camino
 */
public class SCANScheduler implements DiskScheduler {
    private int currentHeadPosition;
    private boolean movingRight; // true = derecha, false = izquierda
    private final int diskSize;
    
    public SCANScheduler(int diskSize) {
        this(0, diskSize);
    }
    
    public SCANScheduler(int startPosition, int diskSize) {
        this.currentHeadPosition = startPosition;
        this.diskSize = diskSize;
        this.movingRight = true;
    }
    
    @Override
    public Process selectNextProcess(LinkedList<Process> requestQueue) {
        if (requestQueue.isEmpty()) {
            return null;
        }
        
        Process nextProcess = null;
        
        if (movingRight) {
            // Buscar la solicitud más cercana hacia la derecha
            nextProcess = findClosestInDirection(requestQueue, true);
            if (nextProcess == null) {
                // Cambiar dirección si no hay solicitudes a la derecha
                movingRight = false;
                System.out.println("DEBUG SCAN - Cambiando dirección a IZQUIERDA");
                nextProcess = findClosestInDirection(requestQueue, false);
            }
        } else {
            // Buscar la solicitud más cercana hacia la izquierda
            nextProcess = findClosestInDirection(requestQueue, false);
            if (nextProcess == null) {
                // Cambiar dirección si no hay solicitudes a la izquierda
                movingRight = true;
                System.out.println("DEBUG SCAN - Cambiando dirección a DERECHA");
                nextProcess = findClosestInDirection(requestQueue, true);
            }
        }
        
        if (nextProcess != null) {
            currentHeadPosition = simulateBlockNumber(nextProcess);
            System.out.println("DEBUG SCAN - Cabezal movido a: " + currentHeadPosition);
        }
        
        return nextProcess;
    }
    
    private Process findClosestInDirection(LinkedList<Process> requests, boolean right) {
        Process closest = null;
        int minDistance = Integer.MAX_VALUE;
        
        for (Process process : requests) {
            int block = simulateBlockNumber(process);
            int distance = block - currentHeadPosition;
            
            if ((right && distance >= 0) || (!right && distance <= 0)) {
                int absDistance = Math.abs(distance);
                if (absDistance < minDistance) {
                    minDistance = absDistance;
                    closest = process;
                }
            }
        }
        
        return closest;
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
        return "SCAN (Elevator Algorithm)";
    }
    
    @Override
    public String getAlgorithmDescription() {
        return "El cabezal se mueve en una dirección atendiendo solicitudes, como un ascensor";
    }
    
    public int getCurrentHeadPosition() {
        return currentHeadPosition;
    }
    
    public boolean isMovingRight() {
        return movingRight;
    }
}