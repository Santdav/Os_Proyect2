/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UIComponents;

/**
 *
 * @author santi
 */
import DataStructures.FileSystemElement;
import DataStructures.LinkedList;
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
        JButton refreshBtn = findButton("Actualizar");
        if (refreshBtn != null) {
            refreshBtn.addActionListener(e -> updateDisplay());
        }
        
        JButton createFileBtn = findButton("Crear Archivo");
        if (createFileBtn != null) {
            createFileBtn.addActionListener(e -> showCreateFileDialog()); // Llama al método existente
        }

        JButton createDirBtn = findButton("Crear Directorio");
        if (createDirBtn != null) {
            // Llama al nuevo método de diálogo que acabamos de añadir
            createDirBtn.addActionListener(e -> showCreateDirDialog()); 
        }
        
        JButton deleteBtn = findButton("Eliminar"); // Usa tu método findButton(String text)
        if (deleteBtn != null) {
            deleteBtn.addActionListener(e -> showDeleteElementDialog());
        }
        
    }
    
    private JButton findButton(String text) {
    // Buscar el botón con el texto dado en el panel de controles (asume que el panel de controles es el primer componente NORTH)
    Component[] components = ((JPanel)getContentPane().getComponent(0)).getComponents();
    for (Component comp : components) {
        if (comp instanceof JButton && text.equals(((JButton)comp).getText())) {
            return (JButton) comp;
        }
    }
    return null;
}
    
    private DefaultMutableTreeNode buildTreeModel(FileSystemElement element) {

        // El nodo se llama con el nombre del elemento
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(element.getName());

        // Si el elemento es un directorio, lo recorremos recursivamente
        if (element.isDirectory()) {
            // Hacemos un casting seguro al tipo Directory (asumiendo que existe)
            LogicalStrucures.Directory dir = (LogicalStrucures.Directory) element;

            // Iterar sobre los hijos del directorio
            for (FileSystemElement child : dir.getChildren()) { // Asumo que getChildren() devuelve la lista de elementos
                // Por cada hijo, construimos un sub-árbol recursivamente
                node.add(buildTreeModel(child));
            }
        }

        // Puedes diferenciar visualmente archivos de directorios si es necesario aquí
        return node;
    }
    
    private DefaultTreeModel createFileSystemTreeModel() {

        // Obtener el elemento raíz de tu sistema de archivos
        FileSystemElement realRoot = fileSystem.getRoot();

        // Construir el árbol a partir de la raíz real
        DefaultMutableTreeNode rootNode = buildTreeModel(realRoot);

        return new DefaultTreeModel(rootNode);
    }
    
    private javax.swing.table.TableModel createAllocationTableModel() {
        // Modelo de tabla básico - será reemplazado
        String[] columnNames = {"Archivo", "Ruta", "Bloques", "Primer Bloque", "Dueño", "Tamaño (B)"};

        // Obtener la tabla de archivos real del FSM
        LinkedList<FileSystemManager.FileEntry> fileEntries = fileSystem.getFileTable();

        // Crear un array 2D para la tabla
        Object[][] data = new Object[fileEntries.size()][columnNames.length];

        for (int i = 0; i < fileEntries.size(); i++) {
            FileSystemManager.FileEntry entry = fileEntries.get(i);
            data[i] = new Object[]{
                entry.getFileName(),
                entry.getFilePath(),
                entry.getBlockCount(),
                entry.getFirstBlockAddress(),
                entry.getOwner(),
                entry.getFileSize()
            };
        }
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
    
    public void showCreateDirDialog() {
        // Campos de entrada
        JTextField nameField = new JTextField();
        JTextField pathField = new JTextField("/root"); // Ruta de creación por defecto

        Object[] message = {
            "Nombre del Directorio:", nameField,
            "Ruta Padre:", pathField
        };

        // Mostrar el diálogo
        int option = JOptionPane.showConfirmDialog(this, message,
                "Crear Directorio", JOptionPane.OK_CANCEL_OPTION);

        // Procesar la respuesta
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String path = pathField.getText().trim();

            if (name.isEmpty() || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre y la ruta no pueden estar vacíos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Llamar a la lógica del FileSystemManager
            boolean success = fileSystem.createDirectory(path, name);

            // Mostrar resultado y actualizar UI
            if (success) {
                JOptionPane.showMessageDialog(this,
                        String.format("Directorio '%s' creado exitosamente en %s", name, path));
                updateDisplay(); // Refrescar el árbol de archivos
            } else {
                // El error puede ser falta de permisos (verificas en FSM) o directorio padre no existe.
                JOptionPane.showMessageDialog(this,
                        "Error al crear el directorio. Verifique permisos o ruta.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Método para demostración - será expandido
    public void showCreateFileDialog() {
        JTextField nameField = new JTextField();
        JTextField sizeField = new JTextField();
        JTextField pathField = new JTextField("/root");
        
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
    
    public void showDeleteElementDialog() {
        // Usamos /root como valor predeterminado
        JTextField pathField = new JTextField("/root");
        JTextField nameField = new JTextField();

        Object[] message = {
            "Nombre del Elemento (Archivo/Directorio):", nameField,
            "Ruta Padre (donde se encuentra):", pathField
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Eliminar Elemento (¡Advertencia!)", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String path = pathField.getText().trim();
            String name = nameField.getText().trim();

            if (name.isEmpty() || path.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe especificar la ruta y el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Intentar encontrar el elemento completo
            // Nota: findElement necesita la RUTA COMPLETA, por eso concatenamos.
            FileSystemElement elementToDelete = fileSystem.findElement(path + "/" + name);

            if (elementToDelete == null) {
                JOptionPane.showMessageDialog(this, "Elemento no encontrado en esa ruta o sin permisos de lectura.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = false;

            // Llamar a la función de eliminación adecuada en FileSystemManager
            if (elementToDelete.isDirectory()) {
                success = fileSystem.deleteDirectory(path, name);
            } else {
                success = fileSystem.deleteFile(path, name);
            }

            // Mostrar resultado y actualizar UI
            if (success) {
                JOptionPane.showMessageDialog(this, name + " eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                updateDisplay(); // Refrescar el árbol de archivos
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar. Verifique si el directorio es /root, permisos (debe ser dueño o Admin), o si el directorio padre existe.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}