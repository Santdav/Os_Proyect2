/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import Schedulers.*;
import DataStructures.Queue;
import  LogicalStrucures.Process;
import Managers.ProcessManager;


/**
 *
 * @author santi
 */
/**
 * Pruebas para los algoritmos de planificación de disco
 */
/**
 * Clase de pruebas modular para algoritmos de planificación de disco.
 * Diseñada para ser extendida con nuevos planificadores fácilmente.
 */
public class DiskSchedulerTest {
    
    public static void runAllTest() {
        System.out.println("=== PRUEBAS MODULARES DE PLANIFICADORES ===\n");
        
        // Pruebas para FIFO Scheduler
        testScheduler(new FIFOScheduler(), "FIFO");
        
        // En el futuro, agregar más planificadores aquí:
        // testScheduler(new SSTFScheduler(), "SSTF");
        // testScheduler(new SCANScheduler(), "SCAN");
        // testScheduler(new CSCANScheduler(), "C-SCAN");
        
        // Pruebas de integración con ProcessManager
        testProcessManagerIntegration();
        
        System.out.println("=== TODAS LAS PRUEBAS COMPLETADAS ===");
    }
    
    /**
     * Método modular para probar cualquier implementación de DiskScheduler
     * 
     * @param scheduler El planificador a probar
     * @param schedulerName Nombre del planificador para los mensajes
     */
    private static void testScheduler(DiskScheduler scheduler, String schedulerName) {
        System.out.println("🔧 PRUEBA: ALGORITMO " + schedulerName);
        System.out.println("=" .repeat(50));
        
        // 1. Información básica del algoritmo
        System.out.println("1. INFORMACIÓN DEL ALGORITMO:");
        System.out.println("   - Nombre: " + scheduler.getAlgorithmName());
        System.out.println("   - Descripción: " + scheduler.getAlgorithmDescription());
        
        // 2. Prueba con cola vacía
        System.out.println("\n2. PRUEBA COLA VACÍA:");
        Queue<Process> emptyQueue = new Queue<>();
        Process emptyResult = scheduler.selectNextProcess(emptyQueue);
        System.out.println("   - Cola vacía retorna null: " + (emptyResult == null));
        
        // 3. Prueba con un solo proceso
        System.out.println("\n3. PRUEBA UN SOLO PROCESO:");
        Queue<Process> singleQueue = new Queue<>();
        Process singleProcess = createTestProcess(1, "SINGLE");
        singleQueue.enqueue(singleProcess);
        
        Process singleResult = scheduler.selectNextProcess(singleQueue);
        boolean singleCorrect = singleResult == singleProcess;
        System.out.println("   - Proceso único seleccionado: " + singleCorrect);
        System.out.println("   - Cola vacía después: " + singleQueue.isEmpty());
        
        // 4. Prueba con múltiples procesos
        System.out.println("\n4. PRUEBA MÚLTIPLES PROCESOS:");
        Queue<Process> multiQueue = createTestProcessQueue(5);
        int initialSize = multiQueue.size();
        
        System.out.println("   - Procesos en cola inicial: " + initialSize);
        
        // Seleccionar todos los procesos y verificar comportamiento
        Process[] selectedProcesses = new Process[initialSize];
        for (int i = 0; i < initialSize; i++) {
            selectedProcesses[i] = scheduler.selectNextProcess(multiQueue);
            System.out.println("   - Seleccionado PID " + selectedProcesses[i].getProcessId() + 
                             " - Proceso " + (i + 1) + "/" + initialSize);
        }
        
        // Verificar que todos los procesos fueron seleccionados
        boolean allSelected = true;
        for (Process p : selectedProcesses) {
            if (p == null) {
                allSelected = false;
                break;
            }
        }
        System.out.println("   - Todos los procesos seleccionados: " + allSelected);
        System.out.println("   - Cola vacía al final: " + multiQueue.isEmpty());
        
        // 5. Pruebas específicas del algoritmo
        System.out.println("\n5. PRUEBAS ESPECÍFICAS DEL ALGORITMO:");
        performAlgorithmSpecificTests(scheduler, schedulerName);
        
        System.out.println("\n✅ PRUEBA " + schedulerName + " COMPLETADA\n");
    }
    
    /**
     * Pruebas específicas para cada algoritmo de planificación
     */
    private static void performAlgorithmSpecificTests(DiskScheduler scheduler, String schedulerName) {
        switch (schedulerName) {
            case "FIFO":
                testFIFOSpecific(scheduler);
                break;
            case "SSTF":
                // testSSTFSpecific(scheduler); // Para implementar en el futuro
                break;
            case "SCAN":
                // testSCANSpecific(scheduler); // Para implementar en el futuro
                break;
            case "C-SCAN":
                // testCSCANSpecific(scheduler); // Para implementar en el futuro
                break;
            default:
                System.out.println("   - No hay pruebas específicas para: " + schedulerName);
        }
    }
    
    /**
     * Pruebas específicas para FIFO Scheduler
     */
    private static void testFIFOSpecific(DiskScheduler scheduler) {
        System.out.println("   - Probando orden FIFO...");
        
        Queue<Process> testQueue = new Queue<>();
        Process[] testProcesses = new Process[3];
        
        // Crear procesos con IDs específicos
        for (int i = 0; i < 3; i++) {
            testProcesses[i] = createTestProcess(i + 1, "TEST" + (i + 1));
            testQueue.enqueue(testProcesses[i]);
        }
        
        // FIFO debe retornar en el mismo orden de inserción
        boolean fifoOrderCorrect = true;
        for (int i = 0; i < 3; i++) {
            Process selected = scheduler.selectNextProcess(testQueue);
            if (selected != testProcesses[i]) {
                fifoOrderCorrect = false;
                break;
            }
        }
        
        System.out.println("   - Orden FIFO correcto: " + fifoOrderCorrect);
        System.out.println("   - Complejidad: O(1) - tiempo constante");
    }
    
    /**
     * Pruebas de integración con ProcessManager
     */
    private static void testProcessManagerIntegration() {
        System.out.println("🔗 PRUEBA: INTEGRACIÓN CON PROCESS MANAGER");
        System.out.println("=" .repeat(50));
        
        // 1. Configuración básica
        System.out.println("1. CONFIGURACIÓN BÁSICA:");
        ProcessManager pm = new ProcessManager();
        DiskScheduler originalScheduler = pm.getDiskScheduler();
        System.out.println("   - Planificador por defecto: " + originalScheduler.getAlgorithmName());
        
        // 2. Cambio de planificador
        System.out.println("\n2. CAMBIO DE PLANIFICADOR:");
        FIFOScheduler newScheduler = new FIFOScheduler();
        pm.setDiskScheduler(newScheduler);
        System.out.println("   - Nuevo planificador: " + pm.getDiskScheduler().getAlgorithmName());
        System.out.println("   - Cambio exitoso: " + (pm.getDiskScheduler() == newScheduler));
        
        // 3. Prueba de funcionamiento con el nuevo planificador
        System.out.println("\n3. FUNCIONAMIENTO CON NUEVO PLANIFICADOR:");
        
        // Crear múltiples procesos
        for (int i = 0; i < 3; i++) {
            pm.createFileProcess("user" + i, Process.IOOperation.CREATE_FILE, 
                               "/test", "file" + i + ".txt", i + 1);
        }
        
        pm.admitNewProcesses();
        System.out.println("   - Procesos en cola READY: " + pm.getReadyProcesses().size());
        
        // Planificar procesos
        Process p1 = pm.scheduleNextProcess();
        Process p2 = pm.scheduleNextProcess();
        Process p3 = pm.scheduleNextProcess();
        
        System.out.println("   - Procesos planificados: " + 
                          ((p1 != null) && (p2 != null) && (p3 != null)));
        System.out.println("   - Estados correctos: " + 
                          (p1.getState() == Process.ProcessState.RUNNING &&
                           p2.getState() == Process.ProcessState.RUNNING &&
                           p3.getState() == Process.ProcessState.RUNNING));
        
        // 4. Restaurar planificador original
        System.out.println("\n4. RESTAURACIÓN PLANIFICADOR ORIGINAL:");
        pm.setDiskScheduler(originalScheduler);
        System.out.println("   - Planificador restaurado: " + 
                          (pm.getDiskScheduler() == originalScheduler));
        
        System.out.println("\n✅ PRUEBA DE INTEGRACIÓN COMPLETADA\n");
    }
    
    /**
     * Crea una cola de prueba con procesos de ejemplo
     */
    private static Queue<Process> createTestProcessQueue(int count) {
        Queue<Process> queue = new Queue<>();
        for (int i = 0; i < count; i++) {
            queue.enqueue(createTestProcess(i + 1, "TestProcess" + (i + 1)));
        }
        return queue;
    }
    
    /**
     * Crea un proceso de prueba con configuración básica
     */
    private static Process createTestProcess(int id, String description) {
        return new Process(id, "testUser", Process.IOOperation.READ_FILE, 
                          "/test/path", "file" + id + ".txt", 0);
    }
    
    /**
     * Método auxiliar para imprimir información de una cola de procesos
     */
    private static void printQueueInfo(String queueName, Queue<Process> queue) {
        System.out.println("   " + queueName + " (" + queue.size() + " procesos):");
        for (int i = 0; i < queue.size(); i++) {
            Process p = queue.get(i);
            System.out.println("     - PID " + p.getProcessId() + ": " + p.getOperationDescription());
        }
    }
}
