/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

/**
 *
 * @author santi
 */
public class FileSystemManager {
    private final StorageDisk disk;
    private final Directory root;
    private final LinkedList<FileEntry> fileTable;
    private String currentUser;
    private boolean isAdminMode;
    
    // Clase interna para la tabla de archivos
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
        this.currentUser = "admin";
        this.isAdminMode = true;
    }
    
    // ==================== GESTIÓN DE USUARIOS Y PERMISOS ====================
    public void setCurrentUser(String user, boolean isAdmin) {
        this.currentUser = user;
        this.isAdminMode = isAdmin;
    }
    
    public boolean hasPermission(FileSystemElement element, boolean writeOperation) {
        if (isAdminMode) return true;
        if (writeOperation) return false; // Usuarios normales no pueden escribir
        
        // Usuarios normales solo pueden leer archivos propios o públicos
        return element.getOwner().equals(currentUser) || 
               element.getPermissions().isPublic();
    }
    
    // ==================== OPERACIONES DE ARCHIVOS ====================
    public boolean createFile(String path, String fileName, int sizeInBlocks) {
        if (!isAdminMode) return false; // Solo admin puede crear archivos
        
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        if (parent.getChild(fileName) != null) {
            return false; // Ya existe un elemento con ese nombre
        }
        
        if (!disk.hasEnoughSpace(sizeInBlocks)) {
            return false; // No hay espacio suficiente
        }
        
        // Encontrar bloques libres
        LinkedList<Integer> freeBlocks = disk.findFreeBlocks(sizeInBlocks);
        if (freeBlocks.size() < sizeInBlocks) {
            return false; // No hay bloques consecutivos suficientes
        }
        
        // Crear el archivo
        File newFile = new File(fileName, currentUser, sizeInBlocks, parent);
        
        // Asignar bloques en cadena
        Block firstBlock = disk.getBlock(freeBlocks.get(0));
        firstBlock.setFree(false);
        firstBlock.setOwnerFile(fileName);
        
        Block currentBlock = firstBlock;
        for (int i = 1; i < sizeInBlocks; i++) {
            Block nextBlock = disk.getBlock(freeBlocks.get(i));
            nextBlock.setFree(false);
            nextBlock.setOwnerFile(fileName);
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
        if (!isAdminMode) return false; // Solo admin puede eliminar archivos
        
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        FileSystemElement element = parent.getChild(fileName);
        if (element == null || element.isDirectory()) {
            return false; // No existe o es un directorio
        }
        
        File file = (File) element;
        
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
        
        // Verificar permisos de lectura
        if (!hasPermission(file, false)) {
            return null;
        }
        
        return file;
    }
    
    // ==================== OPERACIONES DE DIRECTORIOS ====================
    public boolean createDirectory(String path, String dirName) {
        if (!isAdminMode) return false;
        
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        if (parent.getChild(dirName) != null) {
            return false; // Ya existe
        }
        
        Directory newDir = new Directory(dirName, currentUser, parent);
        parent.addChild(newDir);
        return true;
    }
    
    public boolean deleteDirectory(String path, String dirName) {
        if (!isAdminMode) return false;
        
        Directory parent = findDirectory(path);
        if (parent == null) return false;
        
        FileSystemElement element = parent.getChild(dirName);
        if (element == null || !element.isDirectory()) {
            return false;
        }
        
        Directory dir = (Directory) element;
        
        // Eliminar recursivamente todos los archivos y subdirectorios
        deleteDirectoryRecursive(dir);
        
        // Eliminar el directorio mismo
        return parent.removeChild(dir);
    }
    
    private void deleteDirectoryRecursive(Directory dir) {
        LinkedList<FileSystemElement> children = dir.getChildren();
        for (int i = 0; i < children.size(); i++) {
            FileSystemElement child = children.get(i);
            if (child.isDirectory()) {
                deleteDirectoryRecursive((Directory) child);
            } else {
                File file = (File) child;
                // Liberar bloques del archivo
                if (file.getFirstBlock() != null) {
                    disk.freeBlockChain(file.getFirstBlock());
                }
                // Eliminar de la tabla de archivos
                removeFromFileTable(file.getPath());
            }
        }
    }
    
    // ==================== BÚSQUEDA Y NAVEGACIÓN ====================
    public Directory findDirectory(String path) {
        if (path.equals("/") || path.isEmpty()) {
            return root;
        }
        
        return (Directory) root.findElement(path);
    }
    
    public FileSystemElement findElement(String path) {
        return root.findElement(path);
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
        return currentUser;
    }
    
    public boolean isAdminMode() {
        return isAdminMode;
    }
    
    // ==================== ESTADO DEL SISTEMA ====================
    public String getSystemStatus() {
        return String.format("User: %s, Mode: %s, %s", 
                           currentUser, 
                           isAdminMode ? "Admin" : "User",
                           disk.getDiskStatus());
    }
}
