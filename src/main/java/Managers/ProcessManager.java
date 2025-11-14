/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Managers;
import LogicalStrucures.File;
import LogicalStrucures.Process;
import Schedulers.DiskScheduler;
import Schedulers.FIFOScheduler;
import DataStructures.LinkedList;
/**
 *
 * @author santi
 */
/**
 * Gestiona todos los procesos del sistema, incluyendo colas y transiciones de estado.
 */
/**
 * Gestiona todos los procesos del sistema, incluyendo colas y transiciones de estado.
 */

/**
 * Gestiona los procesos y la cola de solicitudes de E/S del sistema
 */


/**
 * Gestiona los procesos y la cola de solicitudes de E/S del sistema
 * Versión corregida para trabajar con tu implementación de Process
 */
public class ProcessManager {
    private LinkedList<Process> allProcesses;
    private LinkedList<Process> readyQueue;
    private LinkedList<Process> blockedQueue;
    private LinkedList<Process> terminatedProcesses;
    
    private LinkedList<Process> ioRequestQueue; // Cambiado a Process en lugar de IORequest
    private DiskScheduler diskScheduler;
    private FileSystemManager fileSystem;
    
    // Contador para IDs de proceso
    private int nextProcessId;
    
    // Métricas del sistema
    private int totalRequestsProcessed;
    private long totalWaitTime;
    private long totalProcessingTime;
    
    public ProcessManager(FileSystemManager fileSystem) {
        this.fileSystem = fileSystem;
        this.allProcesses = new LinkedList<>();
        this.readyQueue = new LinkedList<>();
        this.blockedQueue = new LinkedList<>();
        this.terminatedProcesses = new LinkedList<>();
        this.ioRequestQueue = new LinkedList<>();
        
        // Por defecto usamos FIFO
        this.diskScheduler = new FIFOScheduler();
        
        this.nextProcessId = 1;
        this.totalRequestsProcessed = 0;
        this.totalWaitTime = 0;
        this.totalProcessingTime = 0;
    }
    
    // ==================== GESTIÓN DE PROCESOS ====================
    
    /**
     * Crea un proceso usando TU implementación de Process
     */
    public Process createProcess(String owner, Process.IOOperation operation, 
                                String filePath, String fileName, int fileSize) {
        Process process = new Process(nextProcessId++, owner, operation, filePath, fileName, fileSize);
        allProcesses.add(process);
        moveToReady(process);
        return process;
    }
    
    /**
     * Crea un proceso para operaciones con directorios
     */
    public Process createProcess(String owner, Process.IOOperation operation, 
                                String dirPath, String dirName) {
        Process process = new Process(nextProcessId++, owner, operation, dirPath, dirName);
        allProcesses.add(process);
        moveToReady(process);
        return process;
    }
    
    /**
     * Crea un proceso para operaciones de lectura/actualización
     */
    public Process createProcess(String owner, Process.IOOperation operation, String fullPath) {
        Process process = new Process(nextProcessId++, owner, operation, fullPath);
        allProcesses.add(process);
        moveToReady(process);
        return process;
    }
    
    public void moveToReady(Process process) {
        if (blockedQueue.contains(process)) {
            blockedQueue.remove(process);
        }
        if (!readyQueue.contains(process)) {
            readyQueue.add(process);
        }
        process.setState(Process.ProcessState.READY);
    }
    
    public void moveToBlocked(Process process) {
        if (readyQueue.contains(process)) {
            readyQueue.remove(process);
        }
        if (!blockedQueue.contains(process)) {
            blockedQueue.add(process);
        }
        process.setState(Process.ProcessState.BLOCKED);
    }
    
    public void terminateProcess(Process process) {
        if (readyQueue.contains(process)) {
            readyQueue.remove(process);
        }
        if (blockedQueue.contains(process)) {
            blockedQueue.remove(process);
        }
        if (!terminatedProcesses.contains(process)) {
            terminatedProcesses.add(process);
        }
        process.setState(Process.ProcessState.EXIT);
    }
    
    // ==================== GESTIÓN DE SOLICITUDES E/S ====================
    
    /**
     * Envía un proceso a la cola de E/S para ser procesado
     */
    public void submitIORequest(Process process) {
        if (!ioRequestQueue.contains(process)) {
            ioRequestQueue.add(process);
        }
        
        // El proceso se bloquea esperando la E/S
        moveToBlocked(process);
        
        System.out.println("DEBUG ProcessManager - Proceso enviado a cola E/S: " + process.getOperationDescription());
    }
    
    /**
     * Procesa la siguiente solicitud en la cola de E/S
     */
    public void processNextIORequest() {
        if (ioRequestQueue.isEmpty()) {
            System.out.println("DEBUG - No hay procesos en cola E/S");
            return;
        }
        
        System.out.println("DEBUG - Procesando siguiente solicitud E/S. Cola: " + ioRequestQueue.size());
        
        try {
            // El planificador decide cuál proceso atender
            Process nextProcess = diskScheduler.selectNextProcess(ioRequestQueue);
            
            if (nextProcess != null) {
                // Remover de la cola
                ioRequestQueue.remove(nextProcess);
                
                // Procesar la operación del proceso
                processIOOperation(nextProcess);
            }
        } catch (Exception e) {
            System.out.println("ERROR en processNextIORequest: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Ejecuta la operación de E/S del proceso
     */
    private void processIOOperation(Process process) {
        process.setState(Process.ProcessState.RUNNING);
        long startTime = System.currentTimeMillis();
        
        System.out.println("DEBUG ProcessManager - Procesando: " + process.getOperationDescription());
        
        boolean success = false;
        String errorMessage = "";
        
        try {
            switch (process.getOperation()) {
                case CREATE_FILE:
                    success = fileSystem.createFile(
                        process.getFilePath(), 
                        process.getFileName(), 
                        process.getFileSize()
                    );
                    break;
                    
                case READ_FILE:
                    File file = fileSystem.readFile(process.getFilePath(), process.getFileName());
                    if (file != null) {
                        process.setTargetFile(file);
                        success = true;
                    } else {
                        errorMessage = "Archivo no encontrado o sin permisos";
                    }
                    break;
                    
                case UPDATE_FILE:
                    // Para UPDATE necesitaríamos contenido, pero Process no lo tiene
                    // Por ahora simulamos éxito
                    success = fileSystem.updateFile(
                        process.getFilePath(), 
                        process.getFileName(), 
                        "Contenido actualizado por proceso"
                    );
                    break;
                    
                case DELETE_FILE:
                    success = fileSystem.deleteFile(process.getFilePath(), process.getFileName());
                    break;
                    
                case CREATE_DIR:
                    success = fileSystem.createDirectory(process.getFilePath(), process.getFileName());
                    break;
                    
                case DELETE_DIR:
                    success = fileSystem.deleteDirectory(process.getFilePath(), process.getFileName());
                    break;
            }
            
        } catch (Exception e) {
            errorMessage = e.getMessage();
            success = false;
        }
        
        long endTime = System.currentTimeMillis();
        
        // Actualizar el proceso
        process.setOperationSuccess(success);
        if (!success && !errorMessage.isEmpty()) {
            process.setErrorMessage(errorMessage);
        }
        
        // Actualizar métricas
        totalRequestsProcessed++;
        totalProcessingTime += (endTime - startTime);
        
        if (success) {
            System.out.println("DEBUG ProcessManager - Operación completada: " + process.getOperationDescription());
            terminateProcess(process);
        } else {
            System.out.println("DEBUG ProcessManager - Operación falló: " + process.getOperationDescription());
            // Podríamos reintentar o terminar con error
            terminateProcess(process);
        }
    }
    
    // ==================== CONFIGURACIÓN ====================
    
    public void setDiskScheduler(DiskScheduler scheduler) {
        this.diskScheduler = scheduler;
        System.out.println("DEBUG ProcessManager - Planificador cambiado a: " + scheduler.getAlgorithmName());
    }
    
    // ==================== MÉTRICAS ====================
    
    public int getTotalRequestsProcessed() {
        return totalRequestsProcessed;
    }
    
    public double getAverageProcessingTime() {
        return totalRequestsProcessed > 0 ? (double) totalProcessingTime / totalRequestsProcessed : 0;
    }
    
    public int getPendingRequestsCount() {
        return ioRequestQueue.size();
    }
    
    public int getReadyProcessesCount() {
        return readyQueue.size();
    }
    
    public int getBlockedProcessesCount() {
        return blockedQueue.size();
    }
    
    public int getTerminatedProcessesCount() {
        return terminatedProcesses.size();
    }
    
    // ==================== GETTERS PARA UI ====================
    
    public LinkedList<Process> getAllProcesses() {
        return allProcesses;
    }
    
    public LinkedList<Process> getReadyQueue() {
        return readyQueue;
    }
    
    public LinkedList<Process> getBlockedQueue() {
        return blockedQueue;
    }
    
    public LinkedList<Process> getTerminatedProcesses() {
        return terminatedProcesses;
    }
    
    public LinkedList<Process> getIORequestQueue() {
        return ioRequestQueue;
    }
    
    public DiskScheduler getDiskScheduler() {
        return diskScheduler;
    }
    
    public String getManagerStatus() {
        return String.format(
            "Procesos: %d Listos, %d Bloqueados, %d Terminados | Solicitudes E/S: %d Pendientes",
            readyQueue.size(), blockedQueue.size(), terminatedProcesses.size(), ioRequestQueue.size()
        );
    }
    
    /**
     * Método de conveniencia para crear procesos desde la GUI
     */
    public Process createFileProcess(String filePath, String fileName, int fileSize) {
        String owner = fileSystem.getCurrentUser();
        return createProcess(owner, Process.IOOperation.CREATE_FILE, filePath, fileName, fileSize);
    }
    
    public Process createReadProcess(String filePath, String fileName) {
        String owner = fileSystem.getCurrentUser();
        return createProcess(owner, Process.IOOperation.READ_FILE, filePath, fileName, 0);
    }
    
    public Process createDeleteFileProcess(String filePath, String fileName) {
        String owner = fileSystem.getCurrentUser();
        return createProcess(owner, Process.IOOperation.DELETE_FILE, filePath, fileName, 0);
    }
    
    public Process createDirectoryProcess(String dirPath, String dirName) {
        String owner = fileSystem.getCurrentUser();
        return createProcess(owner, Process.IOOperation.CREATE_DIR, dirPath, dirName);
    }
}