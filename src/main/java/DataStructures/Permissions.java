/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

/**
 *
 * @author santi
 */
public class Permissions {
    private boolean canRead;
    private boolean canWrite;
    private boolean canExecute;
    private boolean isPublic;
    
    public Permissions() {
        this.canRead = true;
        this.canWrite = true;
        this.canExecute = true;
        this.isPublic = true;
    }
    
    // Constructor para permisos específicos
    public Permissions(boolean canRead, boolean canWrite, boolean canExecute, boolean isPublic) {
        this.canRead = canRead;
        this.canWrite = canWrite;
        this.canExecute = canExecute;
        this.isPublic = isPublic;
    }
    
    // Métodos para modo administrador (todos los permisos)
    public void setAdminPermissions() {
        this.canRead = true;
        this.canWrite = true;
        this.canExecute = true;
        this.isPublic = true;
    }
    
    // Métodos para modo usuario (solo lectura)
    public void setUserPermissions() {
        this.canRead = true;
        this.canWrite = false;
        this.canExecute = false;
        this.isPublic = false;
    }
    
    // Getters
    public boolean canRead() {
        return canRead;
    }
    
    public boolean canWrite() {
        return canWrite;
    }
    
    public boolean canExecute() {
        return canExecute;
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    // Setters
    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }
    
    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }
    
    public void setCanExecute(boolean canExecute) {
        this.canExecute = canExecute;
    }
    
    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
    
    // Método para verificar permisos básicos
    public boolean hasReadPermission() {
        return canRead;
    }
    
    public boolean hasWritePermission() {
        return canWrite;
    }
}