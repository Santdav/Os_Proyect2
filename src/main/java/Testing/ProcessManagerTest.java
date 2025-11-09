/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import Managers.ProcessManager;
import LogicalStrucures.Process;
/**
 *
 * @author santi
 */
/**
 * Pruebas para la clase ProcessManager
 */
public class ProcessManagerTest {
    
    public static void runAllTests() {
        System.out.println("=== PRUEBAS DE PROCESS MANAGER ===\n");
        
        testProcessCreation();
        testStateTransitions();
        testQueueManagement();
        testSchedulerCycle();
        testProcessLifecycle();
        
        System.out.println("=== TODAS LAS PRUEBAS COMPLETADAS ===");
    }
    
    private static void testProcessCreation() {
        System.out.println("1. PRUEBA: CREACIÓN DE PROCESOS");
        
        ProcessManager pm = new ProcessManager();
        
        // Crear diferentes tipos de procesos
        Process fileProcess = pm.createFileProcess("admin", Process.IOOperation.CREATE_FILE, 
                                                  "/documents", "test.txt", 3);
        Process dirProcess = pm.createDirectoryProcess("admin", Process.IOOperation.CREATE_DIR, 
                                                      "/", "new_folder");
        Process readProcess = pm.createFileAccessProcess("user1", Process.IOOperation.READ_FILE, 
                                                        "/documents/file.txt");
        
        System.out.println("   - Proceso archivo creado: " + (fileProcess != null));
        System.out.println("   - Proceso directorio creado: " + (dirProcess != null));
        System.out.println("   - Proceso lectura creado: " + (readProcess != null));
        
        // Verificar que están en la cola NEW
        System.out.println("   - Procesos en cola NEW: " + pm.getNewProcesses().size() + " (esperado: 3)");
        System.out.println("   - Estado inicial NEW: " + 
                          (fileProcess.getState() == Process.ProcessState.NEW));
        
        // Verificar IDs únicos
        boolean uniqueIds = (fileProcess.getProcessId() != dirProcess.getProcessId()) &&
                           (dirProcess.getProcessId() != readProcess.getProcessId());
        System.out.println("   - IDs únicos: " + uniqueIds);
        
        System.out.println("   ✅ Prueba de creación completada\n");
    }
    
    private static void testStateTransitions() {
        System.out.println("2. PRUEBA: TRANSICIONES DE ESTADO");
        
        ProcessManager pm = new ProcessManager();
        Process process = pm.createFileProcess("admin", Process.IOOperation.CREATE_FILE, 
                                             "/documents", "test.txt", 2);
        
        // Admitir proceso (NEW → READY)
        pm.admitNewProcesses();
        System.out.println("   - NEW → READY: " + 
                          (process.getState() == Process.ProcessState.READY));
        System.out.println("   - Cola READY tiene procesos: " + 
                          (pm.getReadyProcesses().size() == 1));
        
        // Planificar proceso (READY → RUNNING)
        Process scheduled = pm.scheduleNextProcess();
        System.out.println("   - READY → RUNNING: " + 
                          (scheduled != null && scheduled.getState() == Process.ProcessState.RUNNING));
        System.out.println("   - Proceso planificado correcto: " + 
                          (scheduled == process));
        
        // Bloquear proceso (RUNNING → BLOCKED)
        pm.blockProcess(process);
        System.out.println("   - RUNNING → BLOCKED: " + 
                          (process.getState() == Process.ProcessState.BLOCKED));
        System.out.println("   - Cola BLOCKED tiene procesos: " + 
                          (pm.getBlockedProcesses().size() == 1));
        
        // Desbloquear proceso (BLOCKED → READY)
        pm.unblockProcess(process);
        System.out.println("   - BLOCKED → READY: " + 
                          (process.getState() == Process.ProcessState.READY));
        
        // Volver a planificar y terminar
        pm.scheduleNextProcess();
        pm.terminateProcess(process);
        System.out.println("   - RUNNING → TERMINATED: " + 
                          (process.getState() == Process.ProcessState.EXIT));
        
        System.out.println("   ✅ Prueba de transiciones completada\n");
    }
    
    private static void testQueueManagement() {
        System.out.println("3. PRUEBA: GESTIÓN DE COLAS");
        
        ProcessManager pm = new ProcessManager();
        
        // Crear múltiples procesos
        for (int i = 0; i < 5; i++) {
            pm.createFileProcess("user" + i, Process.IOOperation.CREATE_FILE, 
                               "/documents", "file" + i + ".txt", 1);
        }
        
        System.out.println("   - Procesos creados: " + pm.getTotalProcesses() + " (esperado: 5)");
        System.out.println("   - Todos en cola NEW: " + (pm.getNewProcesses().size() == 5));
        
        // Admitir procesos
        pm.admitNewProcesses();
        System.out.println("   - Después de admitir - NEW: " + pm.getNewProcesses().size() + 
                          ", READY: " + pm.getReadyProcesses().size());
        
        // Planificar algunos procesos
        pm.scheduleNextProcess();
        pm.scheduleNextProcess();
        System.out.println("   - Después de planificar - READY: " + pm.getReadyProcesses().size() + 
                          ", RUNNING: " + pm.getRunningProcesses().size());
        
        // Bloquear un proceso
        if (pm.getRunningProcesses().size() > 0) {
            Process runningProcess = pm.getRunningProcesses().get(0);
            pm.blockProcess(runningProcess);
            System.out.println("   - Después de bloquear - RUNNING: " + pm.getRunningProcesses().size() + 
                              ", BLOCKED: " + pm.getBlockedProcesses().size());
        }
        
        // Verificar estado de colas
        String status = pm.getQueueStatus();
        System.out.println("   - Estado colas: " + status);
        
        System.out.println("   ✅ Prueba de colas completada\n");
    }
    
    private static void testSchedulerCycle() {
        System.out.println("4. PRUEBA: CICLO DEL PLANIFICADOR");
        
        ProcessManager pm = new ProcessManager();
        
        // Crear procesos
        pm.createFileProcess("admin", Process.IOOperation.CREATE_FILE, "/", "test1.txt", 1);
        pm.createFileProcess("admin", Process.IOOperation.CREATE_FILE, "/", "test2.txt", 1);
        
        System.out.println("   - Estado inicial: " + pm.getQueueStatus());
        
        // Ejecutar ciclo del planificador
        pm.runSchedulerCycle();
        System.out.println("   - Después de primer ciclo: " + pm.getQueueStatus());
        
        // Verificar que los procesos se movieron correctamente
        boolean newEmpty = pm.getNewProcesses().isEmpty();
        boolean hasRunning = !pm.getRunningProcesses().isEmpty();
        System.out.println("   - Cola NEW vacía: " + newEmpty);
        System.out.println("   - Proceso en ejecución: " + hasRunning);
        
        // Crear más procesos y ejecutar otro ciclo
        pm.createFileProcess("user1", Process.IOOperation.READ_FILE, "/documents", "read.txt", 0);
        pm.runSchedulerCycle();
        System.out.println("   - Después de segundo ciclo: " + pm.getQueueStatus());
        
        System.out.println("   ✅ Prueba del planificador completada\n");
    }
    
    private static void testProcessLifecycle() {
        System.out.println("5. PRUEBA: CICLO DE VIDA COMPLETO");
        
        ProcessManager pm = new ProcessManager();
        
        // Crear proceso
        Process process = pm.createFileProcess("admin", Process.IOOperation.CREATE_FILE, 
                                             "/documents", "lifecycle.txt", 2);
        int processId = process.getProcessId();
        
        System.out.println("   - Proceso creado - ID: " + processId + ", Estado: " + process.getState());
        
        // Simular ciclo completo
        pm.admitNewProcesses();
        System.out.println("   - Después de admitir - Estado: " + process.getState());
        
        pm.scheduleNextProcess();
        System.out.println("   - Después de planificar - Estado: " + process.getState());
        
        // Simular operación de E/S (bloquear)
        pm.blockProcess(process);
        System.out.println("   - Durante E/S - Estado: " + process.getState());
        
        // Simular fin de E/S (desbloquear y volver a planificar)
        pm.unblockProcess(process);
        pm.scheduleNextProcess();
        System.out.println("   - Después de E/S - Estado: " + process.getState());
        
        // Terminar proceso
        pm.terminateProcess(process);
        System.out.println("   - Finalizado - Estado: " + process.getState());
        
        // Verificar en cola de terminados
        System.out.println("   - En cola TERMINATED: " + (pm.getTerminatedProcesses().size() == 1));
        
        // Buscar proceso por ID
        Process foundProcess = pm.getProcessById(processId);
        System.out.println("   - Encontrado por ID: " + (foundProcess != null));
        System.out.println("   - Mismo proceso: " + (foundProcess == process));
        
        // Limpiar procesos terminados
        pm.clearTerminatedProcesses();
        System.out.println("   - Después de limpiar - TERMINATED: " + pm.getTerminatedProcesses().size());
        
        System.out.println("   ✅ Prueba de ciclo de vida completada\n");
    }
}
