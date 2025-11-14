/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicalStrucures;

/**
 *
 * @author santi
 */
/**
 * Representa una solicitud de E/S para el sistema de archivos
 */

import DataStructures.FileSystemElement;

public class IORequest {
    private Process process;
    private IOOperationType operation;
    private FileSystemElement targetElement;
    private int targetBlock;
    private long arrivalTime;
    private long startTime;
    private long completionTime;
    private String additionalData; // Para contenido en escrituras, etc.
    
    // Estados de la solicitud
    public enum RequestState {
        PENDING,    // En cola esperando
        PROCESSING, // Siendo atendida
        COMPLETED,  // Finalizada exitosamente
        FAILED      // Falló
    }
    
    private RequestState state;
    
    public IORequest(Process process, IOOperationType operation, FileSystemElement targetElement) {
        this.process = process;
        this.operation = operation;
        this.targetElement = targetElement;
        this.arrivalTime = System.currentTimeMillis();
        this.state = RequestState.PENDING;
        this.targetBlock = -1; // -1 indica que no hay bloque específico
    }
    
    public IORequest(Process process, IOOperationType operation, FileSystemElement targetElement, int targetBlock) {
        this(process, operation, targetElement);
        this.targetBlock = targetBlock;
    }
    
    public IORequest(Process process, IOOperationType operation, FileSystemElement targetElement, String additionalData) {
        this(process, operation, targetElement);
        this.additionalData = additionalData;
    }
    
    // ==================== MÉTODOS DE ESTADO ====================
    
    public void startProcessing() {
        this.startTime = System.currentTimeMillis();
        this.state = RequestState.PROCESSING;
    }
    
    public void complete() {
        this.completionTime = System.currentTimeMillis();
        this.state = RequestState.COMPLETED;
    }
    
    public void fail() {
        this.completionTime = System.currentTimeMillis();
        this.state = RequestState.FAILED;
    }
    
    // ==================== CÁLCULOS DE TIEMPO ====================
    
    public long getWaitTime() {
        if (startTime == 0) return 0;
        return startTime - arrivalTime;
    }
    
    public long getProcessingTime() {
        if (completionTime == 0 || startTime == 0) return 0;
        return completionTime - startTime;
    }
    
    public long getTotalTime() {
        if (completionTime == 0) return 0;
        return completionTime - arrivalTime;
    }
    
    public boolean isCompleted() {
        return state == RequestState.COMPLETED;
    }
    
    public boolean isPending() {
        return state == RequestState.PENDING;
    }
    
    // ==================== GETTERS ====================
    
    public Process getProcess() {
        return process;
    }
    
    public IOOperationType getOperation() {
        return operation;
    }
    
    public FileSystemElement getTargetElement() {
        return targetElement;
    }
    
    public int getTargetBlock() {
        return targetBlock;
    }
    
    public long getArrivalTime() {
        return arrivalTime;
    }
    
    public RequestState getState() {
        return state;
    }
    
    public String getAdditionalData() {
        return additionalData;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getCompletionTime() {
        return completionTime;
    }
    
    // ==================== SETTERS ====================
    
    public void setTargetBlock(int targetBlock) {
        this.targetBlock = targetBlock;
    }
    
    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
    
    // ==================== REPRESENTACIÓN ====================
    
    @Override
    public String toString() {
        return String.format("IORequest[%s -> %s on %s | State: %s]", 
                           process != null ? process.getFileName(): "NULL",
                           operation,
                           targetElement != null ? targetElement.getName() : "NULL",
                           state);
    }
    
    public String getDetailedInfo() {
        return String.format(
            "Solicitud: %s\n" +
            "Proceso: %s\n" +
            "Operación: %s\n" +
            "Objetivo: %s\n" +
            "Estado: %s\n" +
            "Tiempo espera: %dms\n" +
            "Tiempo procesamiento: %dms",
            operation,
            process != null ? process.getFileName(): "NULL",
            operation,
            targetElement != null ? targetElement.getPath() : "NULL",
            state,
            getWaitTime(),
            getProcessingTime()
        );
    }
}
