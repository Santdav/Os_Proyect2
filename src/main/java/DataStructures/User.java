/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

/**
 *
 * @author santi
 */
/**
 * Representa un usuario del sistema de archivos
 */
public class User {
    private String username;
    private boolean isAdmin;
    private String homeDirectoryPath;
    
    public User(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.homeDirectoryPath = "/home/" + username;
    }
    
    public User(String username, boolean isAdmin, String homeDirectoryPath) {
        this.username = username;
        this.isAdmin = isAdmin;
        this.homeDirectoryPath = homeDirectoryPath;
    }
    
    // ==================== GETTERS ====================
    public String getUsername() {
        return username;
    }
    
    public boolean isAdmin() {
        return isAdmin;
    }
    
    public String getHomeDirectoryPath() {
        return homeDirectoryPath;
    }
    
    // ==================== SETTERS ====================
    public void setAdmin(boolean admin) {
        this.isAdmin = admin;
    }
    
    public void setHomeDirectoryPath(String homeDirectoryPath) {
        this.homeDirectoryPath = homeDirectoryPath;
    }
    
    // ==================== MÉTODOS DE UTILIDAD ====================
    public boolean canPerformAdminOperations() {
        return isAdmin;
    }
    
    public boolean isOwnerOf(FileSystemElement element) {
        return element != null && username.equals(element.getOwner());
    }
    
    @Override
    public String toString() {
        return username + (isAdmin ? " (Admin)" : " (User)");
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return username.equals(user.username);
    }
    
    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
