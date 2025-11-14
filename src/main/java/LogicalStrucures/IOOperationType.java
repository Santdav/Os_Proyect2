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
 * Tipos de operaciones de E/S soportadas por el sistema
 */
public enum IOOperationType {
    READ("Lectura", "Leer datos de un archivo"),
    WRITE("Escritura", "Escribir datos en un archivo"),
    CREATE("Creación", "Crear nuevo archivo"),
    DELETE("Eliminación", "Eliminar archivo existente"),
    UPDATE("Actualización", "Actualizar contenido de archivo");
    
    private final String displayName;
    private final String description;
    
    IOOperationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}