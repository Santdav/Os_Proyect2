/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import LogicalStrucures.Directory;
import LogicalStrucures.File;
import Managers.FileSystemManager;
import DataStructures.*;
import LogicalStrucures.*;
/**
 *
 * @author santi
 */
/**
 * Clase dedicada para pruebas del sistema de archivos
 * Contiene métodos organizados para probar cada funcionalidad
 */
import java.util.Scanner;

/**
 * Clase de pruebas integrales para el Sistema de Archivos
 */
public class FileSystemTest {
    private FileSystemManager fs;
    private UserSession session;
    private Scanner scanner;
    
    public FileSystemTest() {
        this.fs = new FileSystemManager(100); // Disco de 100 bloques
        this.session = UserSession.getInstance();
        this.scanner = new Scanner(System.in);
    }
    
    public void runAllTests() {
        System.out.println(" INICIANDO PRUEBAS DEL SISTEMA DE ARCHIVOS\n");
        
        testUserSessions();
        testFileOperations();
        testDirectoryOperations();
        testPermissionSystem();
        testDiskOperations();
        testFileTable();
        
        System.out.println("\n TODAS LAS PRUEBAS COMPLETADAS");
    }
    
    // ==================== PRUEBAS DE SESIÓN DE USUARIOS ====================
    private void testUserSessions() {
        System.out.println("=== PRUEBAS DE SESIÓN DE USUARIOS ===");
        
        // Test 1: Sesión por defecto
        System.out.println("1. Sesión por defecto: " + session.getSessionInfo());
        
        // Test 2: Login como administrador
        session.loginAsAdmin();
        System.out.println("2. Login como admin: " + session.getSessionInfo());
        System.out.println("   ¿Es admin? " + session.isAdminMode());
        
        // Test 3: Cambio a modo usuario
        session.switchToUserMode();
        System.out.println("3. Cambio a modo usuario: " + session.getSessionInfo());
        
        // Test 4: Login como usuario normal
        session.loginAsUser("juan");
        System.out.println("4. Login como usuario 'juan': " + session.getSessionInfo());
        
        // Test 5: Volver a admin
        session.loginAsAdmin();
        System.out.println("5. Volver a admin: " + session.getSessionInfo());
        
        System.out.println(" Pruebas de sesión completadas\n");
    }
    
    // ==================== PRUEBAS DE OPERACIONES DE ARCHIVOS ====================
    private void testFileOperations() {
        System.out.println("=== PRUEBAS DE OPERACIONES DE ARCHIVOS ===");
        
        session.loginAsAdmin();
        
        // Test 1: Crear directorio home
        boolean dirCreated = fs.createDirectory("/", "home");
        System.out.println("1. Crear directorio /home: " + (dirCreated ? "ÉXITO" : "FALLO"));
        
        // Test 2: Crear archivo en /home
        boolean file1Created = fs.createFile("/home", "documento.txt", 3);
        System.out.println("2. Crear archivo documento.txt (3 bloques): " + (file1Created ? "ÉXITO" : "FALLO"));
        
        // Test 3: Crear otro archivo
        boolean file2Created = fs.createFile("/home", "datos.dat", 2);
        System.out.println("3. Crear archivo datos.dat (2 bloques): " + (file2Created ? "ÉXITO" : "FALLO"));
        
        // Test 4: Leer archivo existente
        File documento = fs.readFile("/home", "documento.txt");
        System.out.println("4. Leer archivo documento.txt: " + (documento != null ? "ÉXITO" : "FALLO"));
        
        // Test 5: Intentar leer archivo inexistente
        File inexistente = fs.readFile("/home", "noexiste.txt");
        System.out.println("5. Leer archivo inexistente: " + (inexistente == null ? "CORRECTO" : "ERROR"));
        
        // Test 6: Actualizar contenido de archivo
        boolean updated = fs.updateFile("/home", "documento.txt", "Contenido de prueba");
        System.out.println("6. Actualizar contenido: " + (updated ? "ÉXITO" : "FALLO"));
        if (documento != null) {
            System.out.println("   Contenido: '" + documento.getContent() + "'");
        }
        
        System.out.println("Pruebas de archivos completadas\n");
    }
    
    // ==================== PRUEBAS DE OPERACIONES DE DIRECTORIOS ====================
    private void testDirectoryOperations() {
        System.out.println("=== PRUEBAS DE OPERACIONES DE DIRECTORIOS ===");
        
        session.loginAsAdmin();
        
        // Test 1: Crear estructura de directorios
        boolean dir1 = fs.createDirectory("/home", "usuario1");
        boolean dir2 = fs.createDirectory("/home", "usuario2");
        boolean subdir = fs.createDirectory("/home/usuario1", "documentos");
        
        System.out.println("1. Crear /home/usuario1: " + (dir1 ? "ÉXITO" : "FALLO"));
        System.out.println("2. Crear /home/usuario2: " + (dir2 ? "ÉXITO" : "FALLO"));
        System.out.println("3. Crear /home/usuario1/documentos: " + (subdir ? "ÉXITO" : "FALLO"));
        
        // Test 2: Crear archivos en subdirectorios
        boolean fileInSubdir = fs.createFile("/home/usuario1/documentos", "trabajo.doc", 1);
        System.out.println("4. Crear archivo en subdirectorio: " + (fileInSubdir ? "ÉXITO" : "FALLO"));
        
        // Test 3: Buscar directorio existente
        Directory encontrado = fs.findDirectory("/home/usuario1/documentos");
        System.out.println("5. Buscar directorio existente: " + (encontrado != null ? "ÉXITO" : "FALLO"));
        
        // Test 4: Buscar directorio inexistente
        Directory noEncontrado = fs.findDirectory("/home/inexistente");
        System.out.println("6. Buscar directorio inexistente: " + (noEncontrado == null ? "CORRECTO" : "ERROR"));
        
        System.out.println(" Pruebas de directorios completadas\n");
    }
    
    // ==================== PRUEBAS DEL SISTEMA DE PERMISOS ====================
    private void testPermissionSystem() {
        System.out.println("=== PRUEBAS DEL SISTEMA DE PERMISOS ===");
        
        // Preparación: crear archivos de prueba
        session.loginAsAdmin();
        fs.createDirectory("/", "test_permisos");
        
        // **CORRECCIÓN: Hacer el directorio público para que otros usuarios puedan acceder**
        Directory dirTest = fs.findDirectory("/test_permisos");
        if (dirTest != null) {
            dirTest.getPermissions().setPublicReadOnly();
        }
        
        fs.createFile("/test_permisos", "publico.txt", 1);
        fs.createFile("/test_permisos", "privado.txt", 1);
        
        // Configurar permisos de los archivos
        File publico = fs.readFile("/test_permisos", "publico.txt");
        File privado = fs.readFile("/test_permisos", "privado.txt");
        
        if (publico != null) {
            publico.getPermissions().setPublicReadOnly();
        }
        if (privado != null) {
            privado.getPermissions().setPrivatePermissions();
        }
        
        // Test 1: Usuario normal intenta leer archivo público
        session.loginAsUser("maria");
        File leePublico = fs.readFile("/test_permisos", "publico.txt");
        System.out.println("1. Usuario normal lee archivo público: " + (leePublico != null ? "ÉXITO" : "FALLO"));
        
        // Test 2: Usuario normal intenta leer archivo privado
        File leePrivado = fs.readFile("/test_permisos", "privado.txt");
        System.out.println("2. Usuario normal lee archivo privado: " + (leePrivado == null ? "CORRECTO (denegado)" : "ERROR (debería ser denegado)"));
        
        // Test 3: Usuario normal intenta escribir archivo público
        boolean escribePublico = fs.updateFile("/test_permisos", "publico.txt", "modificado");
        System.out.println("3. Usuario normal escribe archivo público: " + (!escribePublico ? "CORRECTO (denegado)" : "ERROR (debería ser denegado)"));
        
        // Test 4: Admin puede hacer todo
        session.loginAsAdmin();
        File adminLee = fs.readFile("/test_permisos", "privado.txt");
        boolean adminEscribe = fs.updateFile("/test_permisos", "privado.txt", "admin modifica");
        System.out.println("4. Admin lee archivo privado: " + (adminLee != null ? "ÉXITO" : "FALLO"));
        System.out.println("5. Admin escribe archivo privado: " + (adminEscribe ? "ÉXITO" : "FALLO"));
        
        System.out.println(" Pruebas de permisos completadas\n");
    }
    
    // ==================== PRUEBAS DE OPERACIONES DE DISCO ====================
    private void testDiskOperations() {
        System.out.println("=== PRUEBAS DE OPERACIONES DE DISCO ===");
        
        session.loginAsAdmin();
        
        // Test 1: Estado inicial del disco
        StorageDisk disk = fs.getDisk();
        System.out.println("1. Estado inicial del disco: " + disk.getDiskStatus());
        
        // Test 2: Crear archivos y ver uso de disco
        fs.createDirectory("/", "test_disco");
        fs.createFile("/test_disco", "archivo1.txt", 5);
        fs.createFile("/test_disco", "archivo2.txt", 3);
        
        System.out.println("2. Después de crear archivos: " + disk.getDiskStatus());
        
        // Test 3: Eliminar archivo y ver liberación
        boolean eliminado = fs.deleteFile("/test_disco", "archivo1.txt");
        System.out.println("3. Eliminar archivo1.txt: " + (eliminado ? "ÉXITO" : "FALLO"));
        System.out.println("4. Después de eliminar: " + disk.getDiskStatus());
        
        // Test 4: Intentar crear archivo muy grande
        boolean archivoGrande = fs.createFile("/test_disco", "enorme.dat", 200);
        System.out.println("5. Crear archivo muy grande (200 bloques): " + (!archivoGrande ? "CORRECTO (rechazado)" : "ERROR (debería ser rechazado)"));
        
        // Test 5: Ver bloques ocupados
        LinkedList<Block> ocupados = disk.getOccupiedBlocks();
        System.out.println("6. Bloques ocupados: " + ocupados.size());
        
        System.out.println("Pruebas de disco completadas\n");
    }
    
    // ==================== PRUEBAS DE TABLA DE ARCHIVOS ====================
    private void testFileTable() {
        System.out.println("=== PRUEBAS DE TABLA DE ARCHIVOS ===");
        
        session.loginAsAdmin();
        
        // Crear algunos archivos para la prueba
        fs.createDirectory("/", "test_tabla");
        fs.createFile("/test_tabla", "tabla1.txt", 2);
        fs.createFile("/test_tabla", "tabla2.txt", 1);
        
        // Test 1: Verificar entradas en tabla
        LinkedList<FileSystemManager.FileEntry> tabla = fs.getFileTable();
        System.out.println("1. Entradas en tabla de archivos: " + tabla.size());
        
        // Test 2: Mostrar información de entradas
        for (int i = 0; i < tabla.size(); i++) {
            FileSystemManager.FileEntry entry = tabla.get(i);
            System.out.println("   - " + entry.getFileName() + 
                             " | Bloques: " + entry.getBlockCount() +
                             " | Primer bloque: " + entry.getFirstBlockAddress() +
                             " | Ruta: " + entry.getFilePath());
        }
        
        // **CORRECCIÓN: Buscar usando la ruta correcta**
        // Encontrar la ruta real usada en la tabla
        String rutaReal = null;
        for (int i = 0; i < tabla.size(); i++) {
            FileSystemManager.FileEntry entry = tabla.get(i);
            if (entry.getFileName().equals("tabla1.txt")) {
                rutaReal = entry.getFilePath();
                break;
            }
        }
        
        if (rutaReal != null) {
            FileSystemManager.FileEntry encontrada = fs.getFileEntry(rutaReal);
            System.out.println("2. Buscar entrada específica (" + rutaReal + "): " + (encontrada != null ? "ÉXITO" : "FALLO"));
        } else {
            System.out.println("2. Buscar entrada específica: No se encontró tabla1.txt en la tabla");
        }
        
        // Test 4: Eliminar archivo y verificar que se elimina de la tabla
        fs.deleteFile("/test_tabla", "tabla1.txt");
        FileSystemManager.FileEntry eliminada = null;
        if (rutaReal != null) {
            eliminada = fs.getFileEntry(rutaReal);
        }
        System.out.println("3. Entrada eliminada de tabla: " + (eliminada == null ? "CORRECTO" : "ERROR"));
        System.out.println("4. Total entradas después de eliminar: " + fs.getFileTable().size());
        
        System.out.println("Pruebas de tabla de archivos completadas\n");
    }
    
}
