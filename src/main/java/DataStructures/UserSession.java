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
/**
 * Gestiona la sesión del usuario actual y el modo de operación
 * Implementa patrón Singleton para acceso global
 */
public class UserSession {
    private static UserSession instance;
    private User currentUser;
    private boolean adminMode;
    
    // Usuarios predefinidos del sistema
    public static final User ADMIN_USER = new User("admin", true, "/");
    public static final User DEFAULT_USER = new User("user", false, "/home/user");
    
    private UserSession() {
        // Por defecto empezamos en modo admin
        this.currentUser = DEFAULT_USER;
        this.adminMode = true;
    }
    
    // ==================== MÉTODOS SINGLETON ====================
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }
    
    // ==================== MÉTODOS DE SESIÓN ====================
    public void loginAsAdmin() {
        this.currentUser = ADMIN_USER;
        this.adminMode = true;
        System.out.println("Sesión iniciada como: " + currentUser);
    }
    
    public void loginAsUser(String username) {
        this.currentUser = new User(username, false);
        this.adminMode = false;
        System.out.println("Sesión iniciada como: " + currentUser);
    }
    
    public void login(User user) {
        this.currentUser = user;
        this.adminMode = user.isAdmin();
        System.out.println("Sesión iniciada como: " + currentUser);
    }
    
    public void logout() {
        this.currentUser = DEFAULT_USER;
        this.adminMode = false;
        System.out.println("Sesión cerrada. Usuario por defecto: " + currentUser);
    }
    
    public void switchToAdminMode() {
        if (currentUser.isAdmin()) {
            this.adminMode = true;
            System.out.println("Modo cambiado a Administrador");
        } else {
            System.out.println("Error: Usuario no tiene permisos de administrador");
        }
    }
    
    public void switchToUserMode() {
        this.adminMode = false;
        System.out.println("Modo cambiado a Usuario");
    }
    
    public void toggleMode() {
        if (currentUser.isAdmin()) {
            this.adminMode = !this.adminMode;
            System.out.println("Modo cambiado a: " + (adminMode ? "Administrador" : "Usuario"));
        } else {
            System.out.println("Error: Usuario no tiene permisos de administrador");
        }
    }
    
    // ==================== VERIFICACIONES DE PERMISOS ====================
    public boolean canRead(FileSystemElement element) {
        if (adminMode) return true;
        if (element == null) return false;
        
        Permissions perm = element.getPermissions();
        User currentUser = getCurrentUser();
        
        // Usuario dueño
        if (currentUser.isOwnerOf(element)) {
            return perm.isOwnerRead();
        }
        // Otro usuario
        return perm.isPublicRead();
    }
    
    public boolean canWrite(FileSystemElement element) {
        if (adminMode) return true;
        if (element == null) return false;
        
        Permissions perm = element.getPermissions();
        User currentUser = getCurrentUser();
        
        // Usuario dueño
        if (currentUser.isOwnerOf(element)) {
            return perm.isOwnerWrite();
        }
        // Otro usuario
        return perm.isPublicWrite();
    }
    
    public boolean canExecute(FileSystemElement element) {
        if (adminMode) return true;
        if (element == null) return false;
        
        Permissions perm = element.getPermissions();
        User currentUser = getCurrentUser();
        
        // Usuario dueño
        if (currentUser.isOwnerOf(element)) {
            return perm.isOwnerExecute();
        }
        // Otro usuario
        return perm.isPublicExecute();
    }
    
    public boolean canDelete(FileSystemElement element) {
        if (adminMode) return true;
        return currentUser.isOwnerOf(element);
    }
    
    public boolean canModifyPermissions(FileSystemElement element) {
        return adminMode || currentUser.isOwnerOf(element);
    }
    
    public boolean isPrivate(FileSystemElement element) {
        if (element == null) return true;
        Permissions perm = element.getPermissions();
        return !perm.isPublicRead() && !perm.isPublicWrite() && !perm.isPublicExecute();
    }
    
    public boolean isPublic(FileSystemElement element) {
        if (element == null) return false;
        return element.getPermissions().isPublicRead();
    }
    
    // ==================== GETTERS ====================
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isAdminMode() {
        return adminMode;
    }
    
    public boolean isUserMode() {
        return !adminMode;
    }
    
    public String getCurrentUsername() {
        return currentUser.getUsername();
    }
    
    public boolean isLoggedIn() {
        return currentUser != null && !currentUser.equals(DEFAULT_USER);
    }
    
    // ==================== INFORMACIÓN DE SESIÓN ====================
    public String getSessionInfo() {
        return String.format("Usuario: %s | Modo: %s", 
                           currentUser.getUsername(), 
                           adminMode ? "Administrador" : "Usuario");
    }
    
    @Override
    public String toString() {
        return getSessionInfo();
    }
}