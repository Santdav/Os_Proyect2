/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicalStrucures;
import DataStructures.User;
import DataStructures.UserSession;
/**
 *
 * @author santi
 */
/**
 * Gestiona los permisos de archivos/directorios considerando dueño y otros usuarios
 */
public class Permissions {
    private String owner;
    private boolean ownerRead;
    private boolean ownerWrite;
    private boolean ownerExecute;
    private boolean publicRead;
    private boolean publicWrite;
    private boolean publicExecute;
    
    // ==================== CONSTRUCTORES ====================
    
    /**
     * Constructor por defecto - permisos mínimos
     */
    public Permissions(String owner) {
        this.owner = owner;
        this.ownerRead = true;
        this.ownerWrite = true;
        this.ownerExecute = false;
        this.publicRead = false;
        this.publicWrite = false;
        this.publicExecute = false;
    }
    
    /**
     * Constructor completo
     */
    public Permissions(String owner, boolean ownerRead, boolean ownerWrite, boolean ownerExecute,
                      boolean publicRead, boolean publicWrite, boolean publicExecute) {
        this.owner = owner;
        this.ownerRead = ownerRead;
        this.ownerWrite = ownerWrite;
        this.ownerExecute = ownerExecute;
        this.publicRead = publicRead;
        this.publicWrite = publicWrite;
        this.publicExecute = publicExecute;
    }
    
    // ==================== MÉTODOS DE VERIFICACIÓN ====================
    
    /**
     * Verifica si un usuario puede leer este elemento
     */
    public boolean canRead(User user) {
        if (user.isAdmin()) return true;
        if (user.getUsername().equals(owner)) return ownerRead;
        return publicRead;
    }
    
    /**
     * Verifica si un usuario puede escribir este elemento
     */
    public boolean canWrite(User user) {
        if (user.isAdmin()) return true;
        if (user.getUsername().equals(owner)) return ownerWrite;
        return publicWrite;
    }
    
    /**
     * Verifica si un usuario puede ejecutar este elemento
     */
    public boolean canExecute(User user) {
        if (user.isAdmin()) return true;
        if (user.getUsername().equals(owner)) return ownerExecute;
        return publicExecute;
    }
    
    /**
     * Verifica permisos usando la sesión actual
     */
    public boolean canRead() {
        return canRead(UserSession.getInstance().getCurrentUser());
    }
    
    public boolean canWrite() {
        return canWrite(UserSession.getInstance().getCurrentUser());
    }
    
    public boolean canExecute() {
        return canExecute(UserSession.getInstance().getCurrentUser());
    }
    
    // ==================== CONFIGURACIONES PREDEFINIDAS ====================
    
    /**
     * Permisos para archivos del sistema (solo admin)
     */
    public void setSystemPermissions() {
        this.ownerRead = true;
        this.ownerWrite = true;
        this.ownerExecute = true;
        this.publicRead = false;
        this.publicWrite = false;
        this.publicExecute = false;
    }
    
    /**
     * Permisos para archivos públicos (todos pueden leer)
     */
    public void setPublicReadOnly() {
        this.ownerRead = true;
        this.ownerWrite = true;
        this.ownerExecute = false;
        this.publicRead = true;
        this.publicWrite = false;
        this.publicExecute = false;
    }
    
    /**
     * Permisos para archivos privados (solo dueño)
     */
    public void setPrivatePermissions() {
        this.ownerRead = true;
        this.ownerWrite = true;
        this.ownerExecute = false;
        this.publicRead = false;
        this.publicWrite = false;
        this.publicExecute = false;
    }
    
    /**
     * Permisos para ejecutables (dueño puede ejecutar)
     */
    public void setExecutablePermissions() {
        this.ownerRead = true;
        this.ownerWrite = true;
        this.ownerExecute = true;
        this.publicRead = true;
        this.publicWrite = false;
        this.publicExecute = false;
    }
    
    // ==================== GETTERS ====================
    public String getOwner() {
        return owner;
    }
    
    public boolean isOwnerRead() {
        return ownerRead;
    }
    
    public boolean isOwnerWrite() {
        return ownerWrite;
    }
    
    public boolean isOwnerExecute() {
        return ownerExecute;
    }
    
    public boolean isPublicRead() {
        return publicRead;
    }
    
    public boolean isPublicWrite() {
        return publicWrite;
    }
    
    public boolean isPublicExecute() {
        return publicExecute;
    }
    
    // ==================== SETTERS ====================
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public void setOwnerRead(boolean ownerRead) {
        this.ownerRead = ownerRead;
    }
    
    public void setOwnerWrite(boolean ownerWrite) {
        this.ownerWrite = ownerWrite;
    }
    
    public void setOwnerExecute(boolean ownerExecute) {
        this.ownerExecute = ownerExecute;
    }
    
    public void setPublicRead(boolean publicRead) {
        this.publicRead = publicRead;
    }
    
    public void setPublicWrite(boolean publicWrite) {
        this.publicWrite = publicWrite;
    }
    
    public void setPublicExecute(boolean publicExecute) {
        this.publicExecute = publicExecute;
    }
    
    // ==================== REPRESENTACIÓN DE PERMISOS ====================
    
    /**
     * Representación estilo UNIX (rwxrwxrwx)
     */
    public String toUnixString() {
        return (ownerRead ? "r" : "-") +
               (ownerWrite ? "w" : "-") +
               (ownerExecute ? "x" : "-") +
               (publicRead ? "r" : "-") +
               (publicWrite ? "w" : "-") +
               (publicExecute ? "x" : "-");
    }
    
    /**
     * Descripción legible de permisos
     */
    public String getPermissionDescription() {
        if (publicRead && publicWrite) return "Público (Lectura/Escritura)";
        if (publicRead) return "Público (Solo Lectura)";
        if (publicWrite) return "Público (Solo Escritura)";
        return "Privado (Solo Dueño)";
    }
    
    @Override
    public String toString() {
        return String.format("Permisos[Dueño:%s, %s]", owner, toUnixString());
    }
}