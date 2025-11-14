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
import LogicalStrucures.Directory;
import Managers.FileSystemManager;
/**
 * Ventana principal del sistema de archivos - Versión integrable Diseñada para
 * ser instanciada desde tu main principal existente
 */
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal del Sistema de Archivos Implementa la interfaz gráfica
 * requerida para el proyecto
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
        setupTreeRenderer();
        setupTreeSelectionListener();
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
        Component[] components = ((JPanel) getContentPane().getComponent(0)).getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton && text.equals(((JButton) comp).getText())) {
                return (JButton) comp;
            }
        }
        return null;
    }

    private DefaultMutableTreeNode buildTreeModel(FileSystemElement element) {
        if (element == null) {
            return new DefaultMutableTreeNode("null");
        }

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(element.getName());

        // VERIFICAR DIRECTAMENTE si es directorio usando el elemento real
        if (element.isDirectory()) {
            Directory dir = (Directory) element;
            LinkedList<FileSystemElement> children = dir.getChildren();

            // Si es directorio, procesar hijos (aunque esté vacío)
            for (int i = 0; i < children.size(); i++) {
                FileSystemElement child = children.get(i);
                node.add(buildTreeModel(child));
            }

            // **AGREGAR ESTO: Si el directorio está vacío, agregar un nodo temporal**
            // para que no aparezca como "leaf" en el árbol
            if (children.isEmpty()) {
                node.add(new DefaultMutableTreeNode("")); // Nodo vacío temporal
            }
        }
        // Si es archivo, no hacer nada más (será leaf automáticamente)

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
        String status = fileSystem.getSystemStatus() + " | "
                + "Planificador: " + schedulerComboBox.getSelectedItem();
        statusLabel.setText(status);
    }

    public void updateDisplay() {
        // Actualizar el árbol de archivos
        fileTree.setModel(createFileSystemTreeModel());

        // Actualizar la tabla de asignación
        allocationTable.setModel(createAllocationTableModel());

        // Actualizar barra de estado
        updateStatusBar();
        debugTreeStructure();
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
        String defaultPath = "/root";

        JTextField nameField = new JTextField();
        JTextField sizeField = new JTextField("1");
        JTextField pathField = new JTextField(defaultPath);

        // Mensaje más informativo
        JLabel helpLabel = new JLabel("<html>Ejemplos de rutas válidas:<br>" +
                                     "- /root (directorio raíz)<br>" +
                                     "- /root/home<br>" +
                                     "- /root/home/user</html>");

        Object[] message = {
            "Nombre del archivo:", nameField,
            "Tamaño en bloques:", sizeField,
            "Ruta del directorio padre:", pathField,
            helpLabel
        };

        int option = JOptionPane.showConfirmDialog(this, message, 
            "Crear Archivo", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                int size = Integer.parseInt(sizeField.getText().trim());
                String path = pathField.getText().trim();

                System.out.println("DEBUG - Intentando crear archivo: '" + name + "' en '" + path + "'");

                boolean success = fileSystem.createFile(path, name, size);

                if (success) {
                    JOptionPane.showMessageDialog(this, 
                        "Archivo '" + name + "' creado exitosamente en " + path,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    updateDisplay();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error al crear archivo. Verifique:\n" +
                        "- Que la ruta sea un directorio válido\n" +
                        "- Que el nombre no exista\n" +
                        "- Que haya espacio suficiente en disco\n" +
                        "- Que tenga permisos de administrador",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "El tamaño debe ser un número válido", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupTreeRenderer() {
        fileTree.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, 
                    boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

                // **LÓGICA MEJORADA:** Usar tanto la información del árbol como del filesystem
                boolean isDirectory = false;

                // Método 1: Por estructura del árbol (si tiene hijos es directorio)
                if (node.getChildCount() > 0) {
                    isDirectory = true;
                } 
                // Método 2: Consultar el filesystem real como respaldo
                else {
                    String nodePath = buildPathFromNode(node);
                    FileSystemElement element = fileSystem.findElement(nodePath);
                    if (element != null) {
                        isDirectory = element.isDirectory();
                    }
                }

                if (isDirectory) {
                    setIcon(UIManager.getIcon("FileView.directoryIcon"));
                } else {
                    setIcon(UIManager.getIcon("FileView.fileIcon"));
                }

                return this;
            }

            private String buildPathFromNode(DefaultMutableTreeNode node) {
                if (node == null) return "";

                // Reconstruir la ruta completa desde el root
                var pathParts = new java.util.ArrayList<>();
                DefaultMutableTreeNode current = node;

                while (current != null) {
                    pathParts.add(0, current.toString());
                    current = (DefaultMutableTreeNode) current.getParent();
                }

                return "/" + String.join("/", pathParts.toString());
            }
        });
    }

    /**
     *
     */
    public void showDeleteElementDialog() {
        // Obtener la selección actual del árbol para pre-llenar
        String defaultPath = getSelectedTreePath();

        // Mostrar estructura actual para referencia
        StringBuilder structureInfo = new StringBuilder("Estructura actual:\n");
        Directory rootDir = fileSystem.getRoot();

        JTextField fullPathField = new JTextField(defaultPath);

        Object[] message = {
            structureInfo.toString(),
            "Ruta COMPLETA del elemento a eliminar:",
            fullPathField
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                "Eliminar Elemento", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String fullPath = fullPathField.getText().trim();

            System.out.println("=== DEBUG ELIMINACIÓN INICIO ===");
            System.out.println("DEBUG - Ruta completa: '" + fullPath + "'");

            // CORREGIR: Normalizar la ruta - quitar /root extra si existe
            if (fullPath.startsWith("/root/root/")) {
                fullPath = fullPath.replaceFirst("/root/root/", "/root/");
                System.out.println("DEBUG - Ruta corregida: '" + fullPath + "'");
            }

            // Encontrar el elemento por ruta completa
            FileSystemElement element = fileSystem.findElement(fullPath);
            System.out.println("DEBUG - Elemento encontrado: " + (element != null));

            if (element != null) {
                System.out.println("DEBUG - Tipo: " + (element.isDirectory() ? "DIRECTORIO" : "ARCHIVO"));
                System.out.println("DEBUG - Nombre: " + element.getName());
                System.out.println("DEBUG - Dueño: " + element.getOwner());
                System.out.println("DEBUG - Usuario actual: " + fileSystem.getCurrentUser());
                System.out.println("DEBUG - Puede eliminar: " + fileSystem.canDelete(element));
            }

            if (element == null) {
                JOptionPane.showMessageDialog(this, 
                    "Elemento no encontrado: " + fullPath + "\n\n" +
                    "Ejemplos de rutas válidas:\n" +
                    "- /root/mis_docuementos/archivo_prueba.txt\n" +
                    "- /root/mis_docuementos\n" +
                    "- /root/home/user",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Extraer path y nombre para las operaciones de delete
            int lastSlash = fullPath.lastIndexOf("/");
            String parentPath = fullPath.substring(0, lastSlash);
            String elementName = fullPath.substring(lastSlash + 1);

            System.out.println("DEBUG - parentPath: '" + parentPath + "'");
            System.out.println("DEBUG - elementName: '" + elementName + "'");

            // Verificar que el directorio padre existe
            Directory parentDir = fileSystem.findDirectory(parentPath);
            System.out.println("DEBUG - Directorio padre encontrado: " + (parentDir != null));

            boolean success = false;

            if (element.isDirectory()) {
                System.out.println("DEBUG - Ejecutando deleteDirectory...");
                success = fileSystem.deleteDirectory(parentPath, elementName);
                System.out.println("DEBUG - Resultado eliminar directorio: " + success);
            } else {
                System.out.println("DEBUG - Ejecutando deleteFile...");
                success = fileSystem.deleteFile(parentPath, elementName);
                System.out.println("DEBUG - Resultado eliminar archivo: " + success);
            }

            System.out.println("=== DEBUG ELIMINACIÓN FIN ===");

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Elemento '" + elementName + "' eliminado exitosamente");
                updateDisplay();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al eliminar '" + elementName + "'\n" +
                    "Posibles causas:\n" +
                    "- Sin permisos\n- Directorio no vacío\n- Error interno",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void setupTreeSelectionListener() {
        fileTree.addTreeSelectionListener(e -> {
            TreePath selectedPath = fileTree.getSelectionPath();
            if (selectedPath != null) {
                String fullPath = buildPathFromTreePath(selectedPath);
                System.out.println("Elemento seleccionado: " + fullPath);

                // Opcional: mostrar información del elemento seleccionado
                FileSystemElement element = fileSystem.findElement(fullPath);
                if (element != null) {
                    statusLabel.setText("Seleccionado: " + element.getName()
                            + (element.isDirectory() ? " (Directorio)" : " (Archivo)"));
                }
            }
        });
    }

    private String buildPathFromTreePath(TreePath path) {
        if (path == null) {
            return "";
        }

        StringBuilder fullPath = new StringBuilder();
        Object[] components = path.getPath();

        for (Object component : components) {
            if (fullPath.length() > 0) {
                fullPath.append("/");
            }
            fullPath.append(component.toString());
        }

        return fullPath.toString();
    }

    
    private String getValidParentPath(String desiredPath) {
        // Si la ruta deseada funciona, usarla
        Directory dir = fileSystem.findDirectory(desiredPath);
        if (dir != null) {
            System.out.println("DEBUG - Ruta válida: " + desiredPath);
            return desiredPath;
        }

        // Si no funciona, probar alternativas
        System.out.println("DEBUG - Ruta '" + desiredPath + "' no válida, probando alternativas...");

        // Alternativa 1: Solo usar /root
        if (!desiredPath.equals("/root")) {
            System.out.println("DEBUG - Alternativa 1: /root");
            return "/root";
        }

        return "/root"; // Último recurso
    }
    
    private void debugTreeStructure() {
        System.out.println("=== DEBUG TREE STRUCTURE ===");
        DefaultTreeModel model = (DefaultTreeModel) fileTree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        printTreeNodes(root, 0);
        System.out.println("============================");
    }

    private void printTreeNodes(DefaultMutableTreeNode node, int depth) {
        String indent = "  ".repeat(depth);
        String nodeType = (node.getChildCount() > 0) ? "DIRECTORIO" : "ARCHIVO";
        System.out.println(indent + "- " + node + " (" + nodeType + ")");

        // Recorrer hijos
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            printTreeNodes(child, depth + 1);
        }
    }

    private String getSelectedTreePath() {
        TreePath selectionPath = fileTree.getSelectionPath();
        if (selectionPath != null) {
            StringBuilder path = new StringBuilder();
            Object[] pathComponents = selectionPath.getPath();

            for (int i = 0; i < pathComponents.length; i++) {
                if (path.length() > 0) path.append("/");
                path.append(pathComponents[i].toString());
            }

            return path.toString();
        }

        return "/root"; // Ruta por defecto
    }

}
