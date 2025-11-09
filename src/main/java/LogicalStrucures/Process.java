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
 * Clase que representa un proceso en el sistema de archivos
 * Cada operación de E/S es ejecutada por un proceso
 */
public class Process {
    
    // ==================== ENUMS ====================
    
    /**
     * Estados posibles de un proceso en el sistema
     */
    public enum ProcessState {
        NEW,        // Recién creado
        READY,      // Listo para ejecutar
        RUNNING,    // En ejecución
        BLOCKED,    // Bloqueado por E/S
        EXIT  // Finalizado
    }
    
    /**
     * Operaciones de E/S que puede realizar un proceso
     */
    public enum IOOperation {
        CREATE_FILE,    // Crear archivo
        READ_FILE,      // Leer archivo  
        UPDATE_FILE,    // Actualizar archivo
        DELETE_FILE,    // Eliminar archivo
        CREATE_DIR,     // Crear directorio
        DELETE_DIR      // Eliminar directorio
    }
    
    // ==================== ATRIBUTOS ====================
    
    private int processId;
    private String owner;
    private ProcessState state;
    private IOOperation operation;
    
    // Targets de la operación (pueden ser null dependiendo de la operación)
    private File targetFile;
    private Directory targetDirectory;
    private String filePath;
    private String fileName;
    private int fileSize; // Solo para operaciones CREATE_FILE
    
    // Tiempos del proceso
    private long startTime;
    private long endTime;
    private long blockedTime;
    
    // Resultado de la operación
    private boolean operationSuccess;
    private String errorMessage;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor para operaciones con archivos
     */
    public Process(int processId, String owner, IOOperation operation, 
                   String filePath, String fileName, int fileSize) {
        this.processId = processId;
        this.owner = owner;
        this.operation = operation;
        this.filePath = filePath;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.state = ProcessState.NEW;
        this.startTime = System.currentTimeMillis();
        this.operationSuccess = false;
        this.errorMessage = "";
    }
    
    /**
     * Constructor para operaciones con directorios
     */
    public Process(int processId, String owner, IOOperation operation, 
                   String dirPath, String dirName) {
        this.processId = processId;
        this.owner = owner;
        this.operation = operation;
        this.filePath = dirPath;
        this.fileName = dirName;
        this.fileSize = 0;
        this.state = ProcessState.NEW;
        this.startTime = System.currentTimeMillis();
        this.operationSuccess = false;
        this.errorMessage = "";
    }
    
    /**
     * Constructor para operaciones de lectura/actualización
     */
    public Process(int processId, String owner, IOOperation operation, 
                   String fullPath) {
        this.processId = processId;
        this.owner = owner;
        this.operation = operation;
        
        // Extraer path y nombre del archivo del path completo
        int lastSlash = fullPath.lastIndexOf("/");
        if (lastSlash != -1) {
            this.filePath = fullPath.substring(0, lastSlash);
            this.fileName = fullPath.substring(lastSlash + 1);
        } else {
            this.filePath = "/";
            this.fileName = fullPath;
        }
        
        this.fileSize = 0;
        this.state = ProcessState.NEW;
        this.startTime = System.currentTimeMillis();
        this.operationSuccess = false;
        this.errorMessage = "";
    }
    
    // ==================== MÉTODOS DE ESTADO ====================
    
    public void setState(ProcessState newState) {
        this.state = newState;
        
        // Registrar tiempos cuando cambia el estado
        if (newState == ProcessState.EXIT) {
            this.endTime = System.currentTimeMillis();
        } else if (newState == ProcessState.BLOCKED) {
            this.blockedTime = System.currentTimeMillis();
        }
    }
    
    public boolean isFinished() {
        return state == ProcessState.EXIT;
    }
    
    public boolean isBlocked() {
        return state == ProcessState.BLOCKED;
    }
    
    public boolean canExecute() {
        return state == ProcessState.READY || state == ProcessState.RUNNING;
    }
    
    // ==================== MÉTODOS DE OPERACIÓN ====================
    
    public String getOperationDescription() {
        switch (operation) {
            case CREATE_FILE:
                return "Crear archivo: " + filePath + "/" + fileName + " (" + fileSize + " bloques)";
            case READ_FILE:
                return "Leer archivo: " + filePath + "/" + fileName;
            case UPDATE_FILE:
                return "Actualizar archivo: " + filePath + "/" + fileName;
            case DELETE_FILE:
                return "Eliminar archivo: " + filePath + "/" + fileName;
            case CREATE_DIR:
                return "Crear directorio: " + filePath + "/" + fileName;
            case DELETE_DIR:
                return "Eliminar directorio: " + filePath + "/" + fileName;
            default:
                return "Operación desconocida";
        }
    }
    
    public boolean isFileOperation() {
        return operation == IOOperation.CREATE_FILE || 
               operation == IOOperation.READ_FILE ||
               operation == IOOperation.UPDATE_FILE ||
               operation == IOOperation.DELETE_FILE;
    }
    
    public boolean isDirectoryOperation() {
        return operation == IOOperation.CREATE_DIR || 
               operation == IOOperation.DELETE_DIR;
    }
    
    // ==================== MÉTODOS DE TIEMPO ====================
    
    public long getExecutionTime() {
        if (endTime == 0) {
            return System.currentTimeMillis() - startTime;
        }
        return endTime - startTime;
    }
    
    public long getBlockedDuration() {
        if (state == ProcessState.BLOCKED) {
            return System.currentTimeMillis() - blockedTime;
        }
        return 0;
    }
    
    // ==================== GETTERS Y SETTERS ====================
    
    public int getProcessId() {
        return processId;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public ProcessState getState() {
        return state;
    }
    
    public IOOperation getOperation() {
        return operation;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public int getFileSize() {
        return fileSize;
    }
    
    public File getTargetFile() {
        return targetFile;
    }
    
    public void setTargetFile(File targetFile) {
        this.targetFile = targetFile;
    }
    
    public Directory getTargetDirectory() {
        return targetDirectory;
    }
    
    public void setTargetDirectory(Directory targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
    
    public boolean isOperationSuccess() {
        return operationSuccess;
    }
    
    public void setOperationSuccess(boolean success) {
        this.operationSuccess = success;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public long getEndTime() {
        return endTime;
    }
    
    // ==================== MÉTODOS DE DEBUG ====================
    
    @Override
    public String toString() {
        return String.format("Process[%d] - %s - %s - %s", 
            processId, 
            owner, 
            state, 
            getOperationDescription());
    }
    
    public String getDetailedInfo() {
        return String.format(
            "Process ID: %d\n" +
            "Owner: %s\n" +
            "State: %s\n" +
            "Operation: %s\n" +
            "Target: %s/%s\n" +
            "File Size: %d blocks\n" +
            "Success: %s\n" +
            "Error: %s\n" +
            "Execution Time: %d ms",
            processId, owner, state, operation, filePath, fileName, fileSize,
            operationSuccess, errorMessage, getExecutionTime()
        );
    }
}