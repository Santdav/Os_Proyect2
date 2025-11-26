/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import LogicalStrucures.Process;
/**
 *
 * @author santi
 */
/**
 * Pruebas unitarias para la clase Process
 */
public class ProcessTest {
    
    
    public static void testProcessCreation() {
        System.out.println("1. PRUEBA: CREACIÓN DE PROCESOS");
        
        // Probar diferentes constructores
        Process fileProcess = new Process(1, "admin", Process.IOOperation.CREATE_FILE, 
                                         "/documents", "test.txt", 3);
        Process dirProcess = new Process(2, "admin", Process.IOOperation.CREATE_DIR, 
                                        "/", "new_folder");
        Process readProcess = new Process(3, "user1", Process.IOOperation.READ_FILE, 
                                         "/documents/test.txt");
        
        System.out.println("   - Proceso archivo creado: " + (fileProcess != null));
        System.out.println("   - Proceso directorio creado: " + (dirProcess != null));
        System.out.println("   - Proceso lectura creado: " + (readProcess != null));
        
        // Verificar estado inicial
        System.out.println("   - Estado inicial (NEW): " + 
                          (fileProcess.getState() == Process.ProcessState.NEW));
        System.out.println("   - ID correcto: " + (fileProcess.getProcessId() == 1));
        System.out.println("   - Propietario correcto: " + 
                          fileProcess.getOwner().equals("admin"));
        
        System.out.println("Prueba de creación completada\n");
    }
    
    public static void testProcessStates() {
        System.out.println("2. PRUEBA: TRANSICIONES DE ESTADO");
        
        Process process = new Process(4, "admin", Process.IOOperation.READ_FILE, 
                                     "/documents/file.txt");
        
        // Probar transiciones de estado
        process.setState(Process.ProcessState.READY);
        System.out.println("   - NEW → READY: " + 
                          (process.getState() == Process.ProcessState.READY));
        
        process.setState(Process.ProcessState.RUNNING);
        System.out.println("   - READY → RUNNING: " + 
                          (process.getState() == Process.ProcessState.RUNNING));
        
        process.setState(Process.ProcessState.BLOCKED);
        System.out.println("   - RUNNING → BLOCKED: " + 
                          (process.getState() == Process.ProcessState.BLOCKED));
        
        process.setState(Process.ProcessState.EXIT);
        System.out.println("   - BLOCKED → TERMINATED: " + 
                          (process.getState() == Process.ProcessState.EXIT));
        
        // Verificar métodos de estado
        System.out.println("   - isFinished(): " + process.isFinished());
        System.out.println("   - canExecute(): " + process.canExecute());
        
        System.out.println("Prueba de estados completada\n");
    }
    
    public static void testProcessOperations() {
        System.out.println("3. PRUEBA: OPERACIONES Y MÉTODOS");
        
        Process process = new Process(5, "user1", Process.IOOperation.CREATE_FILE, 
                                     "/documents", "data.txt", 5);
        
        // Probar descripción de operación
        String description = process.getOperationDescription();
        System.out.println("   - Descripción operación: " + description);
        System.out.println("   - Contiene nombre archivo: " + description.contains("data.txt"));
        
        // Probar tipo de operación
        System.out.println("   - Es operación de archivo: " + process.isFileOperation());
        System.out.println("   - Es operación de directorio: " + process.isDirectoryOperation());
        
        // Probar setters de resultado
        process.setOperationSuccess(true);
        process.setErrorMessage("");
        System.out.println("   - Operación exitosa: " + process.isOperationSuccess());
        System.out.println("   - Sin mensaje error: " + process.getErrorMessage().isEmpty());
        
        System.out.println("Prueba de operaciones completada\n");
    }
    
    public static void testProcessTiming() {
        System.out.println("4. PRUEBA: GESTIÓN DE TIEMPOS");
        
        Process process = new Process(6, "admin", Process.IOOperation.DELETE_FILE, 
                                     "/documents/old.txt");
        
        // Pequeña pausa para tener tiempo medible
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Verificar que el tiempo de ejecución es mayor que 0
        long executionTime = process.getExecutionTime();
        System.out.println("   - Tiempo ejecución > 0: " + (executionTime > 0));
        System.out.println("   - Tiempo ejecución: " + executionTime + " ms");
        
        // Probar bloqueo
        process.setState(Process.ProcessState.BLOCKED);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long blockedDuration = process.getBlockedDuration();
        System.out.println("   - Duración bloqueo > 0: " + (blockedDuration > 0));
        
        // Terminar proceso y verificar tiempo final
        process.setState(Process.ProcessState.EXIT);
        System.out.println("   - Tiempo fin establecido: " + (process.getEndTime() > 0));
        
        System.out.println("Prueba de tiempos completada\n");
    }
}