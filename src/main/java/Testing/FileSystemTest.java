/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Testing;
import DataStructures.*;
/**
 *
 * @author santi
 */
public class FileSystemTest {
    public static void mainTest() {
        System.out.println("=== PRUEBA CORREGIDA ===\n");
        
        FileSystemManager fs = new FileSystemManager(20);
        System.out.println("Estado inicial: " + fs.getSystemStatus());
        
        // Crear estructura
        fs.createDirectory("/", "documents");
        fs.createDirectory("/", "images");
        fs.createDirectory("/documents", "projects");
        
        // Crear archivos
        System.out.println("\n--- Creando archivos ---");
        boolean success;
        
        success = fs.createFile("/documents", "readme.txt", 2);
        System.out.println("readme.txt (2 bloques): " + success + 
                          " - Bloques usados: " + fs.getDisk().getUsedBlocks());
        
        success = fs.createFile("/documents/projects", "proyecto.java", 3);
        System.out.println("proyecto.java (3 bloques): " + success + 
                          " - Bloques usados: " + fs.getDisk().getUsedBlocks());
        
        success = fs.createFile("/images", "foto.png", 4);
        System.out.println("foto.png (4 bloques): " + success + 
                          " - Bloques usados: " + fs.getDisk().getUsedBlocks());
        
        // Intentar archivo grande (debería fallar)
        success = fs.createFile("/", "grande.avi", 15);
        System.out.println("grande.avi (15 bloques): " + success + 
                          " - Esperado: false - Bloques usados: " + fs.getDisk().getUsedBlocks());
        
        // Estado final
        System.out.println("\n--- Estado final ---");
        System.out.println(fs.getSystemStatus());
        System.out.println("Total bloques usados: " + fs.getDisk().getUsedBlocks());
        System.out.println("Archivos en tabla: " + fs.getFileTable().size());
        
        // Verificar estructura
        System.out.println("\n--- Estructura ---");
        printStructure(fs.getRoot(), 0);
    }
    
    public static void simpleTest(){
        System.out.println("=== DEBUG SIMPLIFICADO ===\n");
        
        FileSystemManager fs = new FileSystemManager(20);
        
        // 1. Probar solo crear directorio raíz
        System.out.println("1. Directorio raíz existe: " + (fs.getRoot() != null));
        System.out.println("   Nombre raíz: " + fs.getRoot().getName());
        
        // 2. Probar crear UN directorio simple
        System.out.println("\n2. Creando directorio 'documents' en raíz:");
        boolean success = fs.createDirectory("/", "documents");
        System.out.println("   Resultado: " + success);
        
        // 3. Verificar si realmente se creó
        Directory documents = fs.findDirectory("/documents");
        System.out.println("   Directorio encontrado: " + (documents != null));
        if (documents != null) {
            System.out.println("   Nombre del directorio: " + documents.getName());
        }
        
        // 4. Probar crear UN archivo simple
        System.out.println("\n3. Creando archivo 'test.txt' en /documents:");
        success = fs.createFile("/documents", "test.txt", 2);
        System.out.println("   Resultado: " + success);
        
        // 5. Estado final
        System.out.println("\n4. Estado final:");
        System.out.println("   " + fs.getSystemStatus());
    }
    
    private static void printStructure(Directory dir, int depth) {
        String indent = "  ".repeat(depth);
        System.out.println(indent + "📁 " + dir.getName() + " (" + dir.getChildren().size() + " elementos)");
        
        for (int i = 0; i < dir.getChildren().size(); i++) {
            FileSystemElement child = dir.getChildren().get(i);
            if (child.isDirectory()) {
                printStructure((Directory) child, depth + 1);
            } else {
                File file = (File) child;
                System.out.println(indent + "  📄 " + file.getName() + 
                                 " (" + file.getSize() + " bloques)" +
                                 " - Bloques asignados: " + file.hasBlocksAssigned());
            }
        }
    }
    
}
