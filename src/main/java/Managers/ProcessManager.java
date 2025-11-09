/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Managers;
import DataStructures.Queue;
import LogicalStrucures.Process;
import Schedulers.DiskScheduler;
import Schedulers.FIFOScheduler;
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
public class ProcessManager {
    
    // ==================== COLAS DE PROCESOS ====================
    
    private Queue<Process> newQueue;        // Procesos nuevos
    private Queue<Process> readyQueue;      // Procesos listos para ejecutar
    private Queue<Process> runningQueue;    // Procesos en ejecución
    private Queue<Process> blockedQueue;    // Procesos bloqueados por E/S
    private Queue<Process> exitQueue; // Procesos terminados
    
    private int nextProcessId;
    private DiskScheduler diskScheduler;
    
    // ==================== CONSTRUCTOR ====================
    
    public ProcessManager() {
    this.newQueue = new Queue<>();
    this.readyQueue = new Queue<>();
    this.runningQueue = new Queue<>();
    this.blockedQueue = new Queue<>();
    this.exitQueue = new Queue<>();
    this.nextProcessId = 1;
    
    // Usar FIFOScheduler por defecto
    this.diskScheduler = new FIFOScheduler();
}
    
    // ==================== CREACIÓN DE PROCESOS ====================
    
    /**
     * Crea un nuevo proceso para operación con archivo
     */
    public Process createFileProcess(String owner, Process.IOOperation operation, 
                                    String filePath, String fileName, int fileSize) {
        Process process = new Process(nextProcessId++, owner, operation, filePath, fileName, fileSize);
        newQueue.enqueue(process);
        return process;
    }
    
    /**
     * Crea un nuevo proceso para operación con directorio
     */
    public Process createDirectoryProcess(String owner, Process.IOOperation operation, 
                                        String dirPath, String dirName) {
        Process process = new Process(nextProcessId++, owner, operation, dirPath, dirName);
        newQueue.enqueue(process);
        return process;
    }
    
    /**
     * Crea un nuevo proceso para operación de lectura/actualización
     */
    public Process createFileAccessProcess(String owner, Process.IOOperation operation, 
                                         String fullPath) {
        Process process = new Process(nextProcessId++, owner, operation, fullPath);
        newQueue.enqueue(process);
        return process;
    }
    
    // ==================== TRANSICIONES DE ESTADO ====================
    
    /**
     * Mueve procesos nuevos a la cola de listos
     */
    public void admitNewProcesses() {
        while (!newQueue.isEmpty()) {
            Process process = newQueue.dequeue();
            process.setState(Process.ProcessState.READY);
            readyQueue.enqueue(process);
        }
    }
    
    /**
     * Selecciona el próximo proceso a ejecutar (FIFO básico por ahora)
     */
    public Process scheduleNextProcess() {
        if (readyQueue.isEmpty()) {
            return null;
        }

        Process nextProcess = diskScheduler.selectNextProcess(readyQueue);
        if (nextProcess != null) {
            nextProcess.setState(Process.ProcessState.RUNNING);
            runningQueue.enqueue(nextProcess);
        }
        return nextProcess;
}
    
    /**
     * Bloquea un proceso (cuando necesita E/S)
     */
    public void blockProcess(Process process) {
        if (runningQueue.remove(process)) {
            process.setState(Process.ProcessState.BLOCKED);
            blockedQueue.enqueue(process);
        }
    }
    
    /**
     * Desbloquea un proceso (cuando termina E/S)
     */
    public void unblockProcess(Process process) {
        if (blockedQueue.remove(process)) {
            process.setState(Process.ProcessState.READY);
            readyQueue.enqueue(process);
        }
    }
    
    /**
     * Termina un proceso
     */
    public void terminateProcess(Process process) {
        if (runningQueue.remove(process)) {
            process.setState(Process.ProcessState.EXIT);
            exitQueue.enqueue(process);
        }
    }
    
    /**
     * Termina un proceso por ID
     */
    public boolean terminateProcess(int processId) {
        // Buscar en runningQueue
        for (int i = 0; i < runningQueue.size(); i++) {
            Process process = runningQueue.get(i);
            if (process.getProcessId() == processId) {
                terminateProcess(process);
                return true;
            }
        }
        return false;
    }
    
    public void setDiskScheduler(DiskScheduler scheduler) {
        this.diskScheduler = scheduler;
    }
    
    public DiskScheduler getDiskScheduler() {
        return diskScheduler;
    }
    
    
    
    // ==================== MÉTODOS DE CONSULTA ====================
    
    public Queue<Process> getNewProcesses() {
        return newQueue;
    }
    
    public Queue<Process> getReadyProcesses() {
        return readyQueue;
    }
    
    public Queue<Process> getRunningProcesses() {
        return runningQueue;
    }
    
    public Queue<Process> getBlockedProcesses() {
        return blockedQueue;
    }
    
    public Queue<Process> getTerminatedProcesses() {
        return exitQueue;
    }
    
    public int getTotalProcesses() {
        return newQueue.size() + readyQueue.size() + runningQueue.size() + 
               blockedQueue.size() + exitQueue.size();
    }
    
    public Process getProcessById(int processId) {
        // Buscar en todas las colas
        for (int i = 0; i < newQueue.size(); i++) {
            if (newQueue.get(i).getProcessId() == processId) return newQueue.get(i);
        }
        for (int i = 0; i < readyQueue.size(); i++) {
            if (readyQueue.get(i).getProcessId() == processId) return readyQueue.get(i);
        }
        for (int i = 0; i < runningQueue.size(); i++) {
            if (runningQueue.get(i).getProcessId() == processId) return runningQueue.get(i);
        }
        for (int i = 0; i < blockedQueue.size(); i++) {
            if (blockedQueue.get(i).getProcessId() == processId) return blockedQueue.get(i);
        }
        for (int i = 0; i < exitQueue.size(); i++) {
            if (exitQueue.get(i).getProcessId() == processId) return exitQueue.get(i);
        }
        return null;
    }
    
    // ==================== MÉTODOS DE SIMULACIÓN ====================
    
    /**
     * Ejecuta un ciclo completo del planificador
     */
    public void runSchedulerCycle() {
        // 1. Admitir nuevos procesos
        admitNewProcesses();
        
        // 2. Si no hay procesos en ejecución, planificar uno
        if (runningQueue.isEmpty()) {
            scheduleNextProcess();
        }
        
        // 3. Verificar procesos bloqueados (simular finalización de E/S)
        checkBlockedProcesses();
    }
    
    /**
     * Simula la finalización de operaciones E/S para algunos procesos bloqueados
     */
    private void checkBlockedProcesses() {
        // En una simulación real, aquí verificarías qué procesos han terminado E/S
        // Por ahora, simulamos desbloqueando aleatoriamente algunos procesos
        if (!blockedQueue.isEmpty()) {
            // Desbloquear el primer proceso como simulación
            Process process = blockedQueue.dequeue();
            unblockProcess(process);
        }
    }
    
    // ==================== MÉTODOS DE INFORMACIÓN ====================
    
    public String getQueueStatus() {
        return String.format("Procesos: NEW(%d) READY(%d) RUNNING(%d) BLOCKED(%d) TERMINATED(%d)",
            newQueue.size(), readyQueue.size(), runningQueue.size(), 
            blockedQueue.size(), exitQueue.size()
        );
    }
    
    @Override
    public String toString() {
        return getQueueStatus();
    }
    
    // ==================== MÉTODOS DE LIMPIEZA ====================
    
    /**
     * Limpia los procesos terminados (para liberar memoria)
     */
    public void clearTerminatedProcesses() {
        exitQueue.clear();
    }
    
    /**
     * Reinicia el ProcessManager
     */
    public void reset() {
        newQueue.clear();
        readyQueue.clear();
        runningQueue.clear();
        blockedQueue.clear();
        exitQueue.clear();
        nextProcessId = 1;
    }
}