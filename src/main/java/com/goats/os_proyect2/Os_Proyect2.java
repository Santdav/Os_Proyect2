/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.goats.os_proyect2;
import DataStructures.*;
import Managers.FileSystemManager;
import Testing.*;
import UIComponents.MainFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/**
 *
 * @author santi
 */
public class Os_Proyect2 {

    public static void main(String[] args) {
        
        ProcessManagerTest.runTests();
        
        //var fsTest = new FileSystemTest();
        //fsTest.runAllTests();

        startGUI(); //ACTUALIZAR BOTON ES TEMPORAL
    }
    
    public static void startGUI(){
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Crear el sistema de archivos
        FileSystemManager fileSystem = new FileSystemManager(256); // 100 bloques
        
        // Ejecutar en el Event Dispatch Thread (requerido para Swing)
        SwingUtilities.invokeLater(() -> {
            // Crear y mostrar la ventana principal
            MainFrame mainFrame = new MainFrame(fileSystem);
            mainFrame.setVisible(true);
            
            // Mostrar mensaje de bienvenida
            JOptionPane.showMessageDialog(mainFrame,
                "Sistema de Archivos - Simulador\n\n" +
                "Modo: Administrador\n" +
                "Disco: 256 bloques disponibles\n" +
                "Estado: Listo para operaciones",
                "Bienvenido",
                JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
