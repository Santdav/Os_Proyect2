/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import Managers.ProcessManager;
import LogicalStrucures.Process;
import Managers.FileSystemManager;
/**
 *
 * @author santi
 */
/**
 * Pruebas para la clase ProcessManager
 */
/**
 * Clase de pruebas para ProcessManager
 */
public class ProcessManagerTest {
    
    public static void runTests() {
        System.out.println("🧪 ===== INICIANDO PRUEBAS DE PROCESS MANAGER =====");
        
        // Crear FileSystemManager de prueba
        FileSystemManager fs = new FileSystemManager(256);
        ProcessManager pm = new ProcessManager(fs);
        
        testProcessCreation(pm);
        testIOQueue(pm);
        testProcessStates(pm);
        testMultipleProcesses(pm);
        testMetrics(pm);
        
        System.out.println("✅ ===== PRUEBAS DE PROCESS MANAGER COMPLETADAS =====");
    }
    
    private static void testProcessCreation(ProcessManager pm) {
        System.out.println("\n📋 1. Probando creación de procesos...");
        
        // Crear diferentes tipos de procesos
        Process fileProcess = pm.createFileProcess("/root", "test_file.txt", 2);
        Process dirProcess = pm.createDirectoryProcess("/root", "test_dir");
        Process readProcess = pm.createReadProcess("/root", "test_file.txt");
        
        System.out.println("   ✅ Proceso de archivo creado: " + fileProcess.getOperationDescription());
        System.out.println("   ✅ Proceso de directorio creado: " + dirProcess.getOperationDescription());
        System.out.println("   ✅ Proceso de lectura creado: " + readProcess.getOperationDescription());
        
        // Verificar estados iniciales
        assert fileProcess.getState() == Process.ProcessState.READY : "Estado inicial debería ser READY";
        assert pm.getReadyProcessesCount() == 3 : "Debería haber 3 procesos listos";
        
        System.out.println("   ✅ Estados iniciales correctos");
    }
    
    private static void testIOQueue(ProcessManager pm) {
        System.out.println("\n📋 2. Probando cola de E/S...");
        
        // Enviar procesos a la cola de E/S
        Process p1 = pm.createFileProcess("/root", "queue_test1.txt", 1);
        Process p2 = pm.createFileProcess("/root", "queue_test2.txt", 1);
        
        pm.submitIORequest(p1);
        pm.submitIORequest(p2);
        
        // Verificar que están en la cola y bloqueados
        assert pm.getPendingRequestsCount() == 2 : "Debería haber 2 solicitudes pendientes";
        assert pm.getBlockedProcessesCount() == 2 : "Debería haber 2 procesos bloqueados";
        assert p1.isBlocked() : "Proceso 1 debería estar bloqueado";
        assert p2.isBlocked() : "Proceso 2 debería estar bloqueado";
        
        System.out.println("   ✅ Procesos correctamente encolados y bloqueados");
        
        // Procesar una solicitud
        pm.processNextIORequest();
        assert pm.getPendingRequestsCount() == 1 : "Debería quedar 1 solicitud pendiente";
        assert pm.getTerminatedProcessesCount() == 1 : "Debería haber 1 proceso terminado";
        
        System.out.println("   ✅ Procesamiento de E/S funciona correctamente");
    }
    
    private static void testProcessStates(ProcessManager pm) {
        System.out.println("\n📋 3. Probando transiciones de estado...");
        
        Process process = pm.createFileProcess("/root", "state_test.txt", 1);
        
        // Verificar estado READY inicial
        assert process.getState() == Process.ProcessState.READY : "Estado inicial debería ser READY";
        
        // Mover a BLOCKED
        pm.submitIORequest(process);
        assert process.getState() == Process.ProcessState.BLOCKED : "Debería estar BLOCKED después de submit";
        
        // Procesar y verificar que termina
        pm.processNextIORequest();
        assert process.getState() == Process.ProcessState.EXIT : "Debería estar EXIT después de procesar";
        assert process.isOperationSuccess() : "Operación debería ser exitosa";
        
        System.out.println("   ✅ Transiciones de estado funcionan correctamente");
    }
    
    private static void testMultipleProcesses(ProcessManager pm) {
        System.out.println("\n📋 4. Probando múltiples procesos...");
        
        // Crear varios procesos
        for (int i = 0; i < 5; i++) {
            Process p = pm.createFileProcess("/root", "multi_test" + i + ".txt", 1);
            pm.submitIORequest(p);
        }
        
        System.out.println("   ✅ Creados 5 procesos en cola E/S");
        
        // Procesar todos
        int processed = 0;
        while (pm.getPendingRequestsCount() > 0) {
            pm.processNextIORequest();
            processed++;
        }
        
        assert processed == 5 : "Deberían haberse procesado 5 solicitudes";
        assert pm.getTerminatedProcessesCount() >= 5 : "Debería haber al menos 5 procesos terminados";
        
        System.out.println("   ✅ Todos los procesos procesados correctamente");
    }
    
    private static void testMetrics(ProcessManager pm) {
        System.out.println("\n📋 5. Probando métricas...");
        
        // Las operaciones anteriores ya generaron métricas
        int totalProcessed = pm.getTotalRequestsProcessed();
        double avgTime = pm.getAverageProcessingTime();
        
        System.out.println("   📊 Total de solicitudes procesadas: " + totalProcessed);
        System.out.println("   📊 Tiempo promedio de procesamiento: " + avgTime + "ms");
        System.out.println("   📊 Procesos listos: " + pm.getReadyProcessesCount());
        System.out.println("   📊 Procesos bloqueados: " + pm.getBlockedProcessesCount());
        System.out.println("   📊 Procesos terminados: " + pm.getTerminatedProcessesCount());
        
        assert totalProcessed > 0 : "Debería haber procesado al menos una solicitud";
        assert avgTime >= 0 : "El tiempo promedio debería ser no negativo";
        
        System.out.println("   ✅ Métricas calculadas correctamente");
    }
}