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
    
    public FileSystemElement(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.permissions = new Permissions();
    }
    
    // Métodos abstractos que deben implementar las clases hijas
    public abstract boolean isDirectory();
    public abstract boolean isFile();
    public abstract int getSize();
    public abstract String getPath();
    
    // Getters y setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getOwner() {
        return owner;
    }
    
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    public Permissions getPermissions() {
        return permissions;
    }
    
    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }
}
