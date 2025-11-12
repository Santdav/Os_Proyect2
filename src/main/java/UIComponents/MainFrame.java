/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UIComponents;

/**
 *
 * @author santi
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.tree.*;
import DataStructures.UserSession;
import Managers.FileSystemManager;
/**
 * Ventana principal del sistema de archivos - Versión integrable
 * Diseñada para ser instanciada desde tu main principal existente
 */
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal del Sistema de Archivos
 * Implementa la interfaz gráfica requerida para el proyecto
 */
public class MainFrame extends JFrame {
    private FileSystemManager fileSystem;
    private UserSession userSession;
    
    // Componentes de la interfaz
    private JTree fileTree;
    private JTable allocationTable;
    private JPanel diskPanel;
    private JPanel processPanel;
    private JLabel statusLabel;
    private JComboBox<String> schedulerComboBox;
    private JComboBox<String> userModeComboBox;
    
    // Paneles principales
    private JSplitPane mainSplitPane;
    private JSplitPane rightSplitPane;
    
    public MainFrame(FileSystemManager fileSystem) {
        this.fileSystem = fileSystem;
        this.userSession = UserSession.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        updateDisplay();
        
        setTitle("Sistema de Archivos - Simulador");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null); // Centrar en pantalla
    }
    
    private void initializeComponents() {
        // Árbol de archivos
        fileTree = new JTree();
        fileTree.setModel(createFileSystemTreeModel());
        JScrollPane treeScrollPane = new JScrollPane(fileTree);
        treeScrollPane.setBorder(BorderFactory.createTitledBorder("Estructura de Archivos"));
        
        // Tabla de asignación
        allocationTable = new JTable();
        allocationTable.setModel(createAllocationTableModel());
        JScrollPane tableScrollPane = new JScrollPane(allocationTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Tabla de Asignación de Archivos"));
        
        // Panel de disco (simplificado por ahora)
        diskPanel = new JPanel();
        diskPanel.setBorder(BorderFactory.createTitledBorder("Simulación de Disco"));
        diskPanel.setLayout(new BorderLayout());
        diskPanel.add(new JLabel("Visualización de bloques del disco - En desarrollo", JLabel.CENTER), BorderLayout.CENTER);
        
        // Panel de procesos
        processPanel = new JPanel();
        processPanel.setBorder(BorderFactory.createTitledBorder("Cola de Procesos"));
        processPanel.setLayout(new BorderLayout());
        processPanel.add(new JLabel("Gestión de procesos - En desarrollo", JLabel.CENTER), BorderLayout.CENTER);
        
        // Selector de planificador
        schedulerComboBox = new JComboBox<>(new String[]{
            "FIFO", "SSTF", "SCAN", "C-SCAN"
        });
        
        // Selector de modo de usuario
        userModeComboBox = new JComboBox<>(new String[]{
            "Modo Administrador", "Modo Usuario"
        });
        userModeComboBox.setSelectedIndex(0); // Admin por defecto
        
        // Barra de estado
        statusLabel = new JLabel();
        updateStatusBar();
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Panel superior con controles
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("Planificador:"));
        controlPanel.add(schedulerComboBox);
        controlPanel.add(new JLabel("Modo:"));
        controlPanel.add(userModeComboBox);
        
        // Botones de operaciones
        JButton createFileBtn = new JButton("Crear Archivo");
        JButton createDirBtn = new JButton("Crear Directorio");
        JButton deleteBtn = new JButton("Eliminar");
        JButton refreshBtn = new JButton("Actualizar");
        
        controlPanel.add(createFileBtn);
        controlPanel.add(createDirBtn);
        controlPanel.add(deleteBtn);
        controlPanel.add(refreshBtn);
        
        add(controlPanel, BorderLayout.NORTH);
        
        // Panel principal dividido
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JScrollPane(fileTree), BorderLayout.CENTER);
        
        JPanel rightUpperPanel = new JPanel(new BorderLayout());
        rightUpperPanel.add(new JScrollPane(allocationTable), BorderLayout.CENTER);
        
        JPanel rightLowerPanel = new JPanel(new GridLayout(1, 2));
        rightLowerPanel.add(diskPanel);
        rightLowerPanel.add(processPanel);
        
        rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, rightUpperPanel, rightLowerPanel);
        rightSplitPane.setResizeWeight(0.5);
        
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightSplitPane);
        mainSplitPane.setResizeWeight(0.3);
        
        add(mainSplitPane, BorderLayout.CENTER);
        
        // Barra de estado
        add(statusLabel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        userModeComboBox.addActionListener(e -> {
            boolean isAdmin = userModeComboBox.getSelectedIndex() == 0;
            if (isAdmin) {
                userSession.loginAsAdmin();
            } else {
                userSession.loginAsUser("usuario");
            }
            updateStatusBar();
            updateDisplay();
        });
        
        // Listeners para los botones (implementación básica)
        JButton refreshBtn = findRefreshButton();
        if (refreshBtn != null) {
            refreshBtn.addActionListener(e -> updateDisplay());
        }
    }
    
    private JButton findRefreshButton() {
        // Buscar el botón de actualizar en el panel de controles
        Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton && "Actualizar".equals(((JButton)comp).getText())) {
                return (JButton) comp;
            }
        }
        return null;
    }
    
    private DefaultTreeModel createFileSystemTreeModel() {
        // Crear un modelo de árbol básico para la estructura de archivos
        // Esto será reemplazado por una implementación más robusta
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");
        
        // Ejemplo básico - en la implementación real se recorrería la estructura real
        DefaultMutableTreeNode homeNode = new DefaultMutableTreeNode("home");
        root.add(homeNode);
        
        DefaultMutableTreeNode docNode = new DefaultMutableTreeNode("documentos");
        homeNode.add(docNode);
        
        return new DefaultTreeModel(root);
    }
    
    private javax.swing.table.TableModel createAllocationTableModel() {
        // Modelo de tabla básico - será reemplazado
        String[] columnNames = {"Archivo", "Bloques", "Primer Bloque", "Dueño", "Tamaño"};
        Object[][] data = {
            {"ejemplo.txt", 3, 0, "admin", "1536 B"},
            {"ejemplo.dat", 2, 3, "admin", "1024 B"}
        };
        
        return new javax.swing.table.DefaultTableModel(data, columnNames);
    }
    
    private void updateStatusBar() {
        String status = fileSystem.getSystemStatus() + " | " + 
                       "Planificador: " + schedulerComboBox.getSelectedItem();
        statusLabel.setText(status);
    }
    
    public void updateDisplay() {
        // Actualizar el árbol de archivos
        fileTree.setModel(createFileSystemTreeModel());
        
        // Actualizar la tabla de asignación
        allocationTable.setModel(createAllocationTableModel());
        
        // Actualizar barra de estado
        updateStatusBar();
        
        // TODO: Actualizar visualización del disco y procesos
        repaint();
    }
    
    // Método para demostración - será expandido
    public void showCreateFileDialog() {
        JTextField nameField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField pathField = new JTextField("/home");
        
        Object[] message = {
            "Nombre del archivo:", nameField,
            "Tamaño en bloques:", sizeField,
            "Ruta:", pathField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, 
            "Crear Archivo", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                int size = Integer.parseInt(sizeField.getText());
                String path = pathField.getText();
                
                boolean success = fileSystem.createFile(path, name, size);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Archivo creado exitosamente");
                    updateDisplay();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear archivo", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Tamaño debe ser un número", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}