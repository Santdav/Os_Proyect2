/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

import LogicalStrucures.Permissions;

/**
 *
 * @author santi
 */
public abstract class FileSystemElement {
    protected String name;
    protected String owner;
    protected Permissions permissions;
    protected long creationTime;
    protected long lastModified;
    
    public FileSystemElement(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.permissions = new Permissions(owner); // Usar nuevo constructor
        this.creationTime = System.currentTimeMillis();
        this.lastModified = System.currentTimeMillis();
    }
    
    // Métodos abstractos que deben implementar las clases hijas
    public abstract boolean isDirectory();
    public abstract boolean isFile();
    public abstract int getSize();
    public abstract String getPath();
    
    // ==================== GETTERS Y SETTERS ====================
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
        this.lastModified = System.currentTimeMillis();
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
        this.permissions.setOwner(owner); // Actualizar dueño en permisos también
        this.lastModified = System.currentTimeMillis();
    }
    
    public Permissions getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
        this.lastModified = System.currentTimeMillis();
    }
    
    public long getCreationTime() {
        return creationTime;
    }
    
    public long getLastModified() {
        return lastModified;
    }
    
    // ==================== MÉTODOS DE UTILIDAD ====================
    
    /**
     * Verifica si el usuario actual puede leer este elemento
     */
    public boolean canRead() {
        return permissions.canRead();
    }
    
    /**
     * Verifica si el usuario actual puede escribir este elemento
     */
    public boolean canWrite() {
        return permissions.canWrite();
    }
    
    /**
     * Verifica si el usuario actual puede ejecutar este elemento
     */
    public boolean canExecute() {
        return permissions.canExecute();
    }
    
    /**
     * Verifica si un usuario específico puede leer este elemento
     */
    public boolean canRead(User user) {
        return permissions.canRead(user);
    }
    
    /**
     * Verifica si un usuario específico puede escribir este elemento
     */
    public boolean canWrite(User user) {
        return permissions.canWrite(user);
    }
    
    /**
     * Verifica si un usuario específico puede ejecutar este elemento
     */
    public boolean canExecute(User user) {
        return permissions.canExecute(user);
    }
    
    /**
     * Información básica del elemento
     */
    public String getElementInfo() {
        return String.format("%s [Dueño: %s, Permisos: %s]", 
                           name, owner, permissions.toUnixString());
    }
}