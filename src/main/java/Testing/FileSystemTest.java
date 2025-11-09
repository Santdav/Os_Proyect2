/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import LogicalStrucures.Directory;
import LogicalStrucures.File;
import Managers.FileSystemManager;
import DataStructures.*;
/**
 *
 * @author santi
 */
/**
 * Clase dedicada para pruebas del sistema de archivos
 * Contiene métodos organizados para probar cada funcionalidad
 */
public class FileSystemTest {
    private FileSystemManager fs;
    
    public static void main(String[] args) {
        FileSystemTest tester = new FileSystemTest();
        tester.runAllTests();
    }
    
    public FileSystemTest() {
        this.fs = new FileSystemManager(20);
    }
    
    /**
     * Ejecuta todas las pruebas del sistema
     */
    public void runAllTests() {
        System.out.println("=== INICIO DE PRUEBAS COMPLETAS DEL SISTEMA DE ARCHIVOS ===\n");
        
        testSistemaInicial();
        testCreacionDirectorios();
        testCreacionArchivos();
        testGestionEspacio();
        testOperacionesLectura();
        testOperacionesEliminacion();
        testModosUsuario();
        testEsquemaPermisos();
        
        System.out.println("=== FIN DE PRUEBAS ===");
    }
    
    /**
     * Prueba 1: Estado inicial del sistema
     */
    private void testSistemaInicial() {
        System.out.println("1. PRUEBA: ESTADO INICIAL DEL SISTEMA");
        System.out.println("   - Directorio raíz existe: " + (fs.getRoot() != null));
        System.out.println("   - Nombre raíz: " + fs.getRoot().getName());
        System.out.println("   - Modo inicial: " + (fs.isAdminMode() ? "Admin" : "Usuario"));
        System.out.println("   - Usuario inicial: " + fs.getCurrentUser());
        System.out.println("   - Estado disco: " + fs.getDisk().getDiskStatus());
        System.out.println("   Estado inicial correcto\n");
    }
    
    /**
     * Prueba 2: Creación de directorios
     */
    private void testCreacionDirectorios() {
        System.out.println("2. PRUEBA: CREACIÓN DE DIRECTORIOS");
        
        // Crear directorios básicos
        boolean success1 = fs.createDirectory("/", "documents");
        boolean success2 = fs.createDirectory("/", "images");
        boolean success3 = fs.createDirectory("/documents", "projects");
        boolean success4 = fs.createDirectory("/documents", "work");
        
        System.out.println("   - Directorio 'documents' creado: " + success1);
        System.out.println("   - Directorio 'images' creado: " + success2);
        System.out.println("   - Directorio 'projects' creado: " + success3);
        System.out.println("   - Directorio 'work' creado: " + success4);
        
        // Verificar que existen
        Directory documents = fs.findDirectory("/documents");
        Directory projects = fs.findDirectory("/documents/projects");
        Directory work = fs.findDirectory("/documents/work");
        
        System.out.println("   - Directorio /documents encontrado: " + (documents != null));
        System.out.println("   - Directorio /documents/projects encontrado: " + (projects != null));
        System.out.println("   - Directorio /documents/work encontrado: " + (work != null));
        
        // Verificar estructura
        if (documents != null) {
            System.out.println("   - /documents tiene " + documents.getChildren().size() + " hijos");
        }
        
        System.out.println("    Prueba de directorios completada\n");
    }
    
    /**
     * Prueba 3: Creación de archivos con diferentes tamaños
     */
    private void testCreacionArchivos() {
        System.out.println("3. PRUEBA: CREACIÓN DE ARCHIVOS");
        
        int bloquesIniciales = fs.getDisk().getUsedBlocks();
        
        // Crear archivos de diferentes tamaños
        boolean success1 = fs.createFile("/documents", "readme.txt", 2);
        boolean success2 = fs.createFile("/documents/projects", "proyecto.java", 3);
        boolean success3 = fs.createFile("/documents/work", "informe.doc", 1);
        boolean success4 = fs.createFile("/images", "foto.png", 4);
        
        System.out.println("   - Archivo 'readme.txt' (2 bloques): " + success1);
        System.out.println("   - Archivo 'proyecto.java' (3 bloques): " + success2);
        System.out.println("   - Archivo 'informe.doc' (1 bloque): " + success3);
        System.out.println("   - Archivo 'foto.png' (4 bloques): " + success4);
        
        // Verificar que los archivos existen
        File readme = fs.readFile("/documents", "readme.txt");
        File proyecto = fs.readFile("/documents/projects", "proyecto.java");
        File informe = fs.readFile("/documents/work", "informe.doc");
        File foto = fs.readFile("/images", "foto.png");
        
        System.out.println("   - Archivo 'readme.txt' encontrado: " + (readme != null));
        System.out.println("   - Archivo 'proyecto.java' encontrado: " + (proyecto != null));
        System.out.println("   - Archivo 'informe.doc' encontrado: " + (informe != null));
        System.out.println("   - Archivo 'foto.png' encontrado: " + (foto != null));
        
        // Verificar bloques asignados
        if (readme != null) {
            System.out.println("   - 'readme.txt' tiene bloques asignados: " + readme.hasBlocksAssigned());
        }
        
        int bloquesFinales = fs.getDisk().getUsedBlocks();
        int bloquesUsados = bloquesFinales - bloquesIniciales;
        
        System.out.println("   - Bloques usados en esta prueba: " + bloquesUsados + " (esperado: 10)");
        System.out.println("   - Estado disco: " + fs.getDisk().getDiskStatus());
        System.out.println("    Prueba de archivos completada\n");
    }
    
    /**
     * Prueba 4: Gestión de espacio y límites
     */
    private void testGestionEspacio() {
        System.out.println("4. PRUEBA: GESTIÓN DE ESPACIO");
        
        int bloquesAntes = fs.getDisk().getUsedBlocks();
        int espacioLibreAntes = fs.getDisk().getFreeBlocks();
        
        System.out.println("   - Espacio antes: " + bloquesAntes + " bloques usados, " + 
                          espacioLibreAntes + " bloques libres");
        
        // Intentar crear archivo que cabe
        boolean success1 = fs.createFile("/", "pequeno.txt", 2);
        System.out.println("   - Archivo 'pequeno.txt' (2 bloques) creado: " + success1);
        
        // Intentar crear archivo demasiado grande
        boolean success2 = fs.createFile("/", "enorme.iso", 15);
        System.out.println("   - Archivo 'enorme.iso' (15 bloques) creado: " + success2 + " (esperado: false)");
        
        // Verificar que el archivo grande no se creó
        File enorme = fs.readFile("/", "enorme.iso");
        System.out.println("   - Archivo 'enorme.iso' existe: " + (enorme != null) + " (esperado: false)");
        
        int bloquesDespues = fs.getDisk().getUsedBlocks();
        System.out.println("   - Bloques usados después: " + bloquesDespues);
        System.out.println("   - Estado disco: " + fs.getDisk().getDiskStatus());
        System.out.println("    Prueba de gestión de espacio completada\n");
    }
    
    /**
     * Prueba 5: Operaciones de lectura y búsqueda
     */
    private void testOperacionesLectura() {
        System.out.println("5. PRUEBA: OPERACIONES DE LECTURA Y BÚSQUEDA");
        
        // Buscar elementos existentes
        FileSystemElement elem1 = fs.findElement("/documents/readme.txt");
        FileSystemElement elem2 = fs.findElement("/documents/projects/proyecto.java");
        FileSystemElement elem3 = fs.findElement("/images/foto.png");
        
        System.out.println("   - Elemento /documents/readme.txt encontrado: " + (elem1 != null));
        System.out.println("   - Elemento /documents/projects/proyecto.java encontrado: " + (elem2 != null));
        System.out.println("   - Elemento /images/foto.png encontrado: " + (elem3 != null));
        
        // Buscar elemento no existente
        FileSystemElement elem4 = fs.findElement("/no/existe.txt");
        System.out.println("   - Elemento /no/existe.txt encontrado: " + (elem4 != null) + " (esperado: false)");
        
        // Leer archivos específicos
        File readme = fs.readFile("/documents", "readme.txt");
        if (readme != null) {
            System.out.println("   - Lectura 'readme.txt': ÉXITO");
            System.out.println("     * Nombre: " + readme.getName());
            System.out.println("     * Tamaño: " + readme.getSize() + " bloques");
            System.out.println("     * Propietario: " + readme.getOwner());
            System.out.println("     * Ruta completa: " + readme.getPath());
        } else {
            System.out.println("   - Lectura 'readme.txt': FALLÓ");
        }
        
        System.out.println("    Prueba de lectura completada\n");
    }
    
    /**
     * Prueba 6: Operaciones de eliminación
     */
    private void testOperacionesEliminacion() {
        System.out.println("6. PRUEBA: OPERACIONES DE ELIMINACIÓN");
        
        int bloquesAntes = fs.getDisk().getUsedBlocks();
        
        // Eliminar archivo existente
        boolean success1 = fs.deleteFile("/documents", "readme.txt");
        System.out.println("   - Archivo 'readme.txt' eliminado: " + success1);
        
        // Verificar que ya no existe
        File readme = fs.readFile("/documents", "readme.txt");
        System.out.println("   - Archivo 'readme.txt' todavía existe: " + (readme != null) + " (esperado: false)");
        
        // Eliminar directorio con contenido
        boolean success2 = fs.deleteDirectory("/documents", "projects");
        System.out.println("   - Directorio 'projects' eliminado: " + success2);
        
        // Verificar que el directorio ya no existe
        Directory projects = fs.findDirectory("/documents/projects");
        System.out.println("   - Directorio 'projects' todavía existe: " + (projects != null) + " (esperado: false)");
        
        int bloquesDespues = fs.getDisk().getUsedBlocks();
        int bloquesLiberados = bloquesAntes - bloquesDespues;
        
        System.out.println("   - Bloques liberados: " + bloquesLiberados + " (esperado: al menos 5)");
        System.out.println("   - Estado disco después de eliminaciones: " + fs.getDisk().getDiskStatus());
        System.out.println("    Prueba de eliminación completada\n");
    }
    
    /**
     * Prueba 7: Modos de usuario (Admin vs Usuario normal)
     */
    private void testModosUsuario() {
        System.out.println("7. PRUEBA: MODOS DE USUARIO");
        
        // Probar como administrador (debería tener todos los permisos)
        System.out.println("   --- MODO ADMINISTRADOR ---");
        fs.setCurrentUser("admin", true);
        System.out.println("   - Usuario actual: " + fs.getCurrentUser());
        System.out.println("   - Es administrador: " + fs.isAdminMode());
        
        boolean adminPuedeCrear = fs.createDirectory("/", "admin_dir");
        boolean adminPuedeEliminar = fs.deleteDirectory("/", "admin_dir");
        
        System.out.println("   - Admin puede crear directorio: " + adminPuedeCrear + " (esperado: true)");
        System.out.println("   - Admin puede eliminar directorio: " + adminPuedeEliminar + " (esperado: true)");
        
        // Probar como usuario normal (permisos limitados)
        System.out.println("   --- MODO USUARIO NORMAL ---");
        fs.setCurrentUser("usuario1", false);
        System.out.println("   - Usuario actual: " + fs.getCurrentUser());
        System.out.println("   - Es administrador: " + fs.isAdminMode());
        
        boolean usuarioPuedeCrear = fs.createDirectory("/", "usuario_dir");
        boolean usuarioPuedeEliminar = fs.deleteDirectory("/documents", "work");
        
        System.out.println("   - Usuario puede crear directorio: " + usuarioPuedeCrear + " (esperado: false)");
        System.out.println("   - Usuario puede eliminar directorio: " + usuarioPuedeEliminar + " (esperado: false)");
        
        // Restaurar modo admin
        fs.setCurrentUser("admin", true);
        System.out.println("    Prueba de modos de usuario completada\n");
    }
    
    /**
     * Prueba 8: Esquema de permisos y tabla de archivos
     */
    private void testEsquemaPermisos() {
        System.out.println("8. PRUEBA: ESQUEMA DE PERMISOS Y TABLA DE ARCHIVOS");
        
        // Verificar tabla de archivos
        LinkedList<FileSystemManager.FileEntry> tabla = fs.getFileTable();
        System.out.println("   - Archivos en tabla del sistema: " + tabla.size());
        
        // Mostrar información de la tabla
        for (int i = 0; i < tabla.size(); i++) {
            FileSystemManager.FileEntry entry = tabla.get(i);
            System.out.println("     " + (i + 1) + ". " + entry.getFileName() + 
                             " | Bloques: " + entry.getBlockCount() +
                             " | Primer bloque: " + entry.getFirstBlockAddress() +
                             " | Propietario: " + entry.getOwner());
        }
        
        // Verificar estructura completa
        System.out.println("\n   --- ESTRUCTURA COMPLETA DEL SISTEMA ---");
        imprimirEstructuraCompleta(fs.getRoot(), 0);
        
        System.out.println("    Prueba de permisos y estructura completada\n");
    }
    
    /**
     * Método auxiliar para imprimir estructura completa
     */
    private void imprimirEstructuraCompleta(Directory dir, int nivel) {
        String sangria = "   " + "  ".repeat(nivel);
        
        System.out.println(sangria + " " + dir.getName() + 
                         " (propietario: " + dir.getOwner() + 
                         ", elementos: " + dir.getChildren().size() + ")");
        
        LinkedList<FileSystemElement> hijos = dir.getChildren();
        for (int i = 0; i < hijos.size(); i++) {
            FileSystemElement elemento = hijos.get(i);
            
            if (elemento.isDirectory()) {
                imprimirEstructuraCompleta((Directory) elemento, nivel + 1);
            } else {
                File archivo = (File) elemento;
                System.out.println(sangria + "  " + archivo.getName() +
                                 " (" + archivo.getSize() + " bloques, " +
                                 "propietario: " + archivo.getOwner() + ")");
            }
        }
    }

    public void quickTest(){
        System.out.println("=== PRUEBA RÁPIDA DE CORRECCIONES ===\n");
        
        FileSystemManager fs = new FileSystemManager(20);
        
        // Crear estructura de prueba
        fs.createDirectory("/", "test");
        fs.createFile("/test", "archivo.txt", 3);
        
        // Probar búsqueda corregida
        System.out.println("1. Prueba de búsqueda por path:");
        FileSystemElement encontrado = fs.findElement("/test/archivo.txt");
        System.out.println("   - Elemento encontrado: " + (encontrado != null));
        System.out.println("   - Nombre: " + (encontrado != null ? encontrado.getName() : "N/A"));
        
        // Probar eliminación recursiva
        System.out.println("\n2. Prueba de eliminación recursiva:");
        int bloquesAntes = fs.getDisk().getUsedBlocks();
        System.out.println("   - Bloques antes: " + bloquesAntes);
        
        boolean eliminado = fs.deleteDirectory("/", "test");
        System.out.println("   - Directorio eliminado: " + eliminado);
        
        int bloquesDespues = fs.getDisk().getUsedBlocks();
        System.out.println("   - Bloques después: " + bloquesDespues);
        System.out.println("   - Bloques liberados: " + (bloquesAntes - bloquesDespues));
        
        // Verificar que ya no existe
        FileSystemElement verificar = fs.findElement("/test");
        System.out.println("   - Directorio todavía existe: " + (verificar != null));
        
        System.out.println("\n=== PRUEBA COMPLETADA ===");
    }
    
    public void diagnosticTest(){
        System.out.println("=== PRUEBA FINAL CORREGIDA ===\n");
        
        FileSystemManager fs = new FileSystemManager(20);
        
        // Crear estructura
        System.out.println("1. Creando estructura...");
        fs.createDirectory("/", "test");
        fs.createFile("/test", "archivo1.txt", 2);
        fs.createFile("/test", "archivo2.txt", 1);
        
        System.out.println("   - Bloques iniciales: " + fs.getDisk().getUsedBlocks());
        
        // Verificar cadena de bloques ANTES de eliminar
        System.out.println("\n2. Verificando cadenas de bloques:");
        File archivo1 = fs.readFile("/test", "archivo1.txt");
        if (archivo1 != null && archivo1.getFirstBlock() != null) {
            System.out.println("   - archivo1.txt cadena:");
            Block block = archivo1.getFirstBlock();
            while (block != null) {
                System.out.println("     Bloque " + block.getBlockNumber() + 
                                 " -> Siguiente: " + (block.getNextBlock() != null ? 
                                 block.getNextBlock().getBlockNumber() : "null"));
                block = block.getNextBlock();
            }
        }
        
        // Eliminar
        System.out.println("\n3. Eliminando directorio...");
        int bloquesAntes = fs.getDisk().getUsedBlocks();
        boolean resultado = fs.deleteDirectory("/", "test");
        
        System.out.println("\n4. Resultados:");
        System.out.println("   - Eliminación exitosa: " + resultado);
        System.out.println("   - Bloques antes: " + bloquesAntes);
        System.out.println("   - Bloques después: " + fs.getDisk().getUsedBlocks());
        System.out.println("   - Bloques liberados: " + (bloquesAntes - fs.getDisk().getUsedBlocks()));
        System.out.println("   - Directorio existe: " + (fs.findElement("/test") != null));
        System.out.println("   - Estado final: " + fs.getDisk().getDiskStatus());
        
        System.out.println("\n=== PRUEBA COMPLETADA ===");
    }
}
