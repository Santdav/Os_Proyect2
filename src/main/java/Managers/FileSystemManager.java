/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Managers;

import DataStructures.Block;
import LogicalStrucures.Directory;
import LogicalStrucures.File;
import DataStructures.FileSystemElement;
import DataStructures.LinkedList;
import LogicalStrucures.StorageDisk;
import DataStructures.UserSession;

/**
 *
 * @author santi
 */
public class FileSystemManager {
    private final StorageDisk disk;
    private final Directory root;
    private final LinkedList<FileEntry> fileTable;
    private final UserSession userSession;
    
    // Clase interna para la tabla de archivos (se mantiene igual)
    public static class FileEntry {
        private final String fileName;
        private final String filePath;
        private final String owner;
        private final int blockCount;
        private final int firstBlockAddress;
        private final long fileSize;
        
        public FileEntry(String fileName, String filePath, String owner, int blockCount, int firstBlockAddress) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.owner = owner;
            this.blockCount = blockCount;
            this.firstBlockAddress = firstBlockAddress;
            this.fileSize = blockCount * 512; // Asumiendo 512 bytes por bloque
        }
        
        // Getters
        public String getFileName() { return fileName; }
        public String getFilePath() { return filePath; }
        public String getOwner() { return owner; }
        public int getBlockCount() { return blockCount; }
        public int getFirstBlockAddress() { return firstBlockAddress; }
        public long getFileSize() { return fileSize; }
    }
    
    public FileSystemManager(int diskSize) {
        this.disk = new StorageDisk(diskSize);
        this.root = new Directory("root", "admin", null);
        this.fileTable = new LinkedList<>();
        this.userSession = UserSession.getInstance();
        
        // Configurar permisos del root como sistema (solo admin)
        root.getPermissions().setSystemPermissions();
        
        Directory homeDir = new Directory("home", "admin", root);

        // Añadir 'home' como hijo del 'root'
        root.addChild(homeDir);

        // ==========================================================
        // Opcional, crear /home/user para el usuario por defecto:
        Directory userDir = new Directory("user", "user", homeDir);
        homeDir.addChild(userDir);
    }
    
    // ==================== GESTIÓN DE USUARIOS Y PERMISOS ====================
    
    /**
     * Verifica permisos para una operación
     */
    public boolean hasPermission(FileSystemElement element, boolean writeOperation) {
        if (userSession.isAdminMode()) return true;
        
        if (writeOperation) {
            return userSession.canWrite(element);
        } else {
            return userSession.canRead(element);
        }
    }
    
    /**
     * Verifica si el usuario actual puede eliminar el elemento
     */
    public boolean canDelete(FileSystemElement element) {
        return userSession.canDelete(element);
    }
    
    // ==================== OPERACIONES DE ARCHIVOS ====================
    public boolean createFile(String path, String fileName, int sizeInBlocks) {
        // Solo administradores pueden crear archivos
        if (!userSession.isAdminMode()) {
            System.out.println("DEBUG: Usuario normal no puede crear archivos");
            return false;
        }

        Directory parent = findDirectory(path);
        System.out.println("path" + parent);
        if (parent == null) return false;

        if (parent.getChild(fileName) != null) {
                    System.out.println("as");

            return false; // Ya existe
        }

        if (!disk.hasEnoughSpace(sizeInBlocks)) {
                    System.out.println("block");

            return false;
        }

        LinkedList<Integer> freeBlocks = disk.findFreeBlocks(sizeInBlocks);
        if (freeBlocks.size() < sizeInBlocks) {
                    System.out.println("bo");

            return false;
        }

        // CORRECCIÓN: Asignar los bloques al disco ANTES de usarlos
        if (!disk.allocateBlocks(freeBlocks, fileName)) {
                    System.out.println("tin");

            return false;
        }

        // Crear el archivo con el usuario actual como dueño
        String currentUser = userSession.getCurrentUsername();
        File newFile = new File(fileName, currentUser, sizeInBlocks, parent);

        // Asignar bloques en cadena - USAR LOS BLOQUES QUE YA RESERVAMOS
        Block firstBlock = disk.getBlock(freeBlocks.get(0));
        Block currentBlock = firstBlock;

        for (int i = 1; i < sizeInBlocks; i++) {
            Block nextBlock = disk.getBlock(freeBlocks.get(i));
            currentBlock.setNextBlock(nextBlock);
            currentBlock = nextBlock;
        }

        newFile.setFirstBlock(firstBlock);
        parent.addChild(newFile);

        // Actualizar tabla de archivos
        FileEntry entry = new FileEntry(fileName, parent.getPath() + "/" + fileName, 
                                      currentUser, sizeInBlocks, freeBlocks.get(0));
        fileTable.add(entry);

        return true;
    }
    
    public boolean deleteFile(String path, String fileName) {
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        FileSystemElement element = parent.getChild(fileName);
        if (element == null || element.isDirectory()) {
            return false; // No existe o es un directorio
        }
        
        File file = (File) element;
        
        // Verificar permisos de eliminación
        if (!canDelete(file)) {
            System.out.println("DEBUG: Usuario no tiene permisos para eliminar: " + fileName);
            return false;
        }
        
        // Liberar bloques
        if (file.getFirstBlock() != null) {
            disk.freeBlockChain(file.getFirstBlock());
        }
        
        // Eliminar de la tabla de archivos
        removeFromFileTable(parent.getPath() + "/" + fileName);
        
        // Eliminar del directorio padre
        return parent.removeChild(file);
    }
    
    public File readFile(String path, String fileName) {
        Directory parent = findDirectory(path);
        if (parent == null) return null;
        
        FileSystemElement element = parent.getChild(fileName);
        if (element == null || element.isDirectory()) {
            return null;
        }
        
        File file = (File) element;
        
        // Verificar permisos de lectura usando UserSession
        if (!userSession.canRead(file)) {
            System.out.println("DEBUG: Usuario no tiene permisos de lectura: " + fileName);
            return null;
        }
        
        return file;
    }
    
    /**
     * Actualizar contenido de archivo (requiere permisos de escritura)
     */
    public boolean updateFile(String path, String fileName, String newContent) {
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        FileSystemElement element = parent.getChild(fileName);
        if (element == null || element.isDirectory()) {
            return false;
        }
        
        File file = (File) element;
        
        // Verificar permisos de escritura
        if (!userSession.canWrite(file)) {
            System.out.println("DEBUG: Usuario no tiene permisos de escritura: " + fileName);
            return false;
        }
        
        file.setContent(newContent);
        return true;
    }
    
    // ==================== OPERACIONES DE DIRECTORIOS ====================
    public boolean createDirectory(String path, String dirName) {
        // Solo administradores pueden crear directorios
        if (!userSession.isAdminMode()) {
            System.out.println("DEBUG: Usuario normal no puede crear directorios");
            return false;
        }
        
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        if (parent.getChild(dirName) != null) {
            return false; // Ya existe
        }
        
        // Crear directorio con el usuario actual como dueño
        String currentUser = userSession.getCurrentUsername();
        Directory newDir = new Directory(dirName, currentUser, parent);
        parent.addChild(newDir);
        return true;
    }
    
    public boolean deleteDirectory(String path, String dirName) {
        Directory parent = findDirectory(path);
        if (parent == null) return false;

        FileSystemElement element = parent.getChild(dirName);
        if (element == null || !element.isDirectory()) {
            return false;
        }

        Directory dir = (Directory) element;

        // Verificar permisos de eliminación
        if (!canDelete(dir)) {
            System.out.println("DEBUG: Usuario no tiene permisos para eliminar directorio: " + dirName);
            return false;
        }

        // PRIMERO: Eliminar recursivamente todo el contenido
        deleteDirectoryRecursive(dir);

        // LUEGO: Eliminar el directorio mismo del padre
        return parent.removeChild(dir);
    }
    
    private void deleteDirectoryRecursive(Directory dir) {
        System.out.println("DEBUG: Eliminando recursivamente directorio: " + dir.getPath());

        // Usar while en lugar de for para evitar problemas de índice
        while (!dir.getChildren().isEmpty()) {
            FileSystemElement child = dir.getChildren().get(0); // Siempre tomar el primero

            if (child.isDirectory()) {
                deleteDirectoryRecursive((Directory) child);
            } else {
                File file = (File) child;
                System.out.println("DEBUG: Eliminando archivo: " + file.getName() + " con " + file.getSize() + " bloques");

                // Liberar bloques del archivo
                if (file.getFirstBlock() != null) {
                    System.out.println("DEBUG: Liberando cadena de bloques para: " + file.getName());
                    disk.freeBlockChain(file.getFirstBlock());
                }

                // Eliminar de la tabla de archivos
                removeFromFileTable(file.getPath());

                // Eliminar del directorio
                dir.removeChild(file);
                System.out.println("DEBUG: Archivo eliminado: " + file.getName());
            }
        }
        System.out.println("DEBUG: Directorio vacío: " + dir.getPath());
    }

    // ==================== BÚSQUEDA Y NAVEGACIÓN ====================
    public Directory findDirectory(String path) {
        if (path.equals("/") || path.isEmpty() || path.equals("/root")) {
            return root;
        }
        
        // Limpiar el path
        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        if (cleanPath.endsWith("/")) {
            cleanPath = cleanPath.substring(0, cleanPath.length() - 1);
        }
        
        String[] parts = cleanPath.split("/");
        Directory current = root;
        
        for (String part : parts) {
            if (part.isEmpty()) continue;
            
            FileSystemElement element = current.getChild(part);
            if (element == null || !element.isDirectory()) {
            System.out.println("caso 1 " + current + element);
                return null;
            }
            
            // Verificar permisos de lectura para el directorio
            if (!userSession.canRead(element)) {
                System.out.println("DEBUG: Sin permisos de lectura para directorio: " + part);
                return null;
            }
            
            current = (Directory) element;
        }
        
        return current;
    }
    
    public FileSystemElement findElement(String path) {
        FileSystemElement element = root.findElement(path);
        if (element != null && !userSession.canRead(element)) {
            return null; // Sin permisos de lectura
        }
        return element;
    }
    
    // ==================== GESTIÓN DE TABLA DE ARCHIVOS ====================
    private void removeFromFileTable(String filePath) {
        for (int i = 0; i < fileTable.size(); i++) {
            FileEntry entry = fileTable.get(i);
            if (entry.getFilePath().equals(filePath)) {
                fileTable.removeAt(i);
                return;
            }
        }
    }
    
    public FileEntry getFileEntry(String filePath) {
        for (int i = 0; i < fileTable.size(); i++) {
            FileEntry entry = fileTable.get(i);
            if (entry.getFilePath().equals(filePath)) {
                return entry;
            }
        }
        return null;
    }
    
    // ==================== GETTERS PARA UI ====================
    public Directory getRoot() {
        return root;
    }
    
    public StorageDisk getDisk() {
        return disk;
    }
    
    public LinkedList<FileEntry> getFileTable() {
        return fileTable;
    }
    
    public String getCurrentUser() {
        return userSession.getCurrentUsername();
    }
    
    public boolean isAdminMode() {
        return userSession.isAdminMode();
    }
    
    public UserSession getUserSession() {
        return userSession;
    }
    
    // ==================== ESTADO DEL SISTEMA ====================
    public String getSystemStatus() {
        return String.format("%s, %s", 
                           userSession.getSessionInfo(),
                           disk.getDiskStatus());
    }
}