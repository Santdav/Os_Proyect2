/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UIComponents;

/**
 *
 * @author santi
 */
import LogicalStrucures.Process.ProcessState;
import Managers.ProcessManager;
import javax.swing.*;
import java.awt.*;
import LogicalStrucures.Process;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;

public class ProcessPanel extends JPanel {
    private ProcessManager processManager;
    private JTable processTable;
    private JTextArea queueTextArea;
    private JButton createProcessBtn;
    private JButton processNextBtn;
    private JButton clearCompletedBtn;
    private JLabel statsLabel;
    private Timer autoUpdateTimer;
    
    public ProcessPanel(ProcessManager processManager) {
        this.processManager = processManager;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        startAutoUpdate();
        updateDisplay();
    }
    
    private void startAutoUpdate() {
        autoUpdateTimer = new Timer(2000, e -> { // Actualizar cada 2 segundo
            updateDisplay();
        });
        autoUpdateTimer.start();
    }
    
    private void initializeComponents() {
        setBorder(BorderFactory.createTitledBorder("Gestión de Procesos"));
        
        // Tabla de procesos
        String[] columnNames = {"ID", "Dueño", "Operación", "Estado", "Éxito", "Tiempo"};
        processTable = new JTable(new DefaultTableModel(columnNames, 0));
        processTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Área de texto para cola E/S
        queueTextArea = new JTextArea(8, 30);
        queueTextArea.setEditable(false);
        queueTextArea.setFont(new Font("Monospaced", Font.PLAIN, 11));
        queueTextArea.setBackground(new Color(240, 240, 240));
        
        // Botones
        createProcessBtn = new JButton("Crear Proceso");
        processNextBtn = new JButton("Procesar Siguiente E/S");
        clearCompletedBtn = new JButton("Limpiar Completados");
        
        // Etiqueta de estadísticas
        statsLabel = new JLabel("", JLabel.CENTER);
        statsLabel.setFont(new Font("Arial", Font.BOLD, 12));
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(5, 5));
        
        // Panel superior con botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(createProcessBtn);
        buttonPanel.add(processNextBtn);
        buttonPanel.add(clearCompletedBtn);
        
        JButton randomProcessesBtn = new JButton("Crear 10 Procesos Aleatorios");
        randomProcessesBtn.addActionListener(e -> createRandomProcesses());
        buttonPanel.add(randomProcessesBtn);

        // Panel de estadísticas
        JPanel statsPanel = new JPanel(new FlowLayout());
        statsPanel.add(statsLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(statsPanel, BorderLayout.CENTER);

        // Panel principal dividido
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Todos los Procesos:", JLabel.CENTER), BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(processTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(new JLabel("Cola de Solicitudes E/S:", JLabel.CENTER), BorderLayout.NORTH);
        rightPanel.add(new JScrollPane(queueTextArea), BorderLayout.CENTER);

        JSplitPane centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        centerSplit.setResizeWeight(0.6);

        add(topPanel, BorderLayout.NORTH);
        add(centerSplit, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        createProcessBtn.addActionListener(this::showCreateProcessDialog);
        processNextBtn.addActionListener(e -> {
            processManager.processNextIORequest();
            updateDisplay();
        });
        clearCompletedBtn.addActionListener(e -> {
            clearCompletedProcesses();
            updateDisplay();
        });
    }

    private void showCreateProcessDialog(ActionEvent e) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Crear Nuevo Proceso", true);
        dialog.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Pestaña para crear archivo
        tabbedPane.addTab("Crear Archivo", createFileProcessPanel(dialog));

        // Pestaña para crear directorio
        tabbedPane.addTab("Crear Directorio", createDirectoryProcessPanel(dialog));

        // Pestaña para eliminar
        tabbedPane.addTab("Eliminar", createDeleteProcessPanel(dialog));

        dialog.add(tabbedPane, BorderLayout.CENTER);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private JPanel createFileProcessPanel(JDialog dialog) {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        JTextField pathField = new JTextField("/root");
        JTextField nameField = new JTextField();
        JTextField sizeField = new JTextField("1");
        JButton createBtn = new JButton("Crear Proceso");

        panel.add(new JLabel("Ruta:"));
        panel.add(pathField);
        panel.add(new JLabel("Nombre archivo:"));
        panel.add(nameField);
        panel.add(new JLabel("Tamaño (bloques):"));
        panel.add(sizeField);
        panel.add(new JLabel());
        panel.add(createBtn);

        createBtn.addActionListener(e -> {
            try {
                String path = pathField.getText().trim();
                String name = nameField.getText().trim();
                int size = Integer.parseInt(sizeField.getText().trim());

                if (!name.isEmpty()) {
                    Process process = processManager.createFileProcess(path, name, size);
                    processManager.submitIORequest(process);
                    dialog.dispose();
                    updateDisplay();
                    JOptionPane.showMessageDialog(this,
                            "Proceso creado y encolado para crear: " + name,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Tamaño debe ser un número", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createDirectoryProcessPanel(JDialog dialog) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField pathField = new JTextField("/root");
        JTextField nameField = new JTextField();
        JButton createBtn = new JButton("Crear Proceso");

        panel.add(new JLabel("Ruta:"));
        panel.add(pathField);
        panel.add(new JLabel("Nombre directorio:"));
        panel.add(nameField);
        panel.add(new JLabel());
        panel.add(createBtn);

        createBtn.addActionListener(e -> {
            String path = pathField.getText().trim();
            String name = nameField.getText().trim();

            if (!name.isEmpty()) {
                Process process = processManager.createDirectoryProcess(path, name);
                processManager.submitIORequest(process);
                dialog.dispose();
                updateDisplay();
                JOptionPane.showMessageDialog(this,
                        "Proceso creado y encolado para crear directorio: " + name,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    private JPanel createDeleteProcessPanel(JDialog dialog) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));

        JTextField pathField = new JTextField("/root");
        JTextField nameField = new JTextField();
        JButton createBtn = new JButton("Crear Proceso");

        panel.add(new JLabel("Ruta:"));
        panel.add(pathField);
        panel.add(new JLabel("Nombre a eliminar:"));
        panel.add(nameField);
        panel.add(new JLabel());
        panel.add(createBtn);

        createBtn.addActionListener(e -> {
            String path = pathField.getText().trim();
            String name = nameField.getText().trim();

            if (!name.isEmpty()) {
                Process process = processManager.createDeleteFileProcess(path, name);
                processManager.submitIORequest(process);
                dialog.dispose();
                updateDisplay();
                JOptionPane.showMessageDialog(this,
                        "Proceso creado y encolado para eliminar: " + name,
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panel;
    }

    private void clearCompletedProcesses() {
        // Los procesos terminados ya están gestionados por ProcessManager
        // Este método es principalmente para limpiar la visualización
        JOptionPane.showMessageDialog(this,
                "Procesos completados: " + processManager.getTerminatedProcessesCount(),
                "Estadísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    public void updateDisplay() {
        updateProcessTable();
        updateQueueDisplay();
        updateStats();
        revalidate();
        repaint();
    }

    private void updateProcessTable() {
        DefaultTableModel model = (DefaultTableModel) processTable.getModel();
        model.setRowCount(0);

        for (Process process : processManager.getAllProcesses()) {
            model.addRow(new Object[]{
                process.getProcessId(),
                process.getOwner(),
                process.getOperation(),
                process.getState(),
                process.isOperationSuccess() ? "Sí" : "No",
                process.getExecutionTime() + "ms"
            });
        }
    }

    private void updateQueueDisplay() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== COLA DE SOLICITUDES E/S ===\n\n");

        int position = 1;
        for (Process process : processManager.getIORequestQueue()) {
            sb.append(position).append(". ")
                    .append(process.getOperationDescription())
                    .append("\n   Estado: ").append(process.getState())
                    .append("\n   Tiempo espera: ").append(process.getExecutionTime()).append("ms\n\n");
            position++;
        }

        if (processManager.getIORequestQueue().isEmpty()) {
            sb.append("(Cola vacía)");
        }

        queueTextArea.setText(sb.toString());
    }

    private void updateStats() {
        String stats = String.format(
                "Procesos: %d Total | %d Listos | %d Bloqueados | %d Terminados | %d en Cola E/S",
                processManager.getAllProcesses().size(),
                processManager.getReadyProcessesCount(),
                processManager.getBlockedProcessesCount(),
                processManager.getTerminatedProcessesCount(),
                processManager.getPendingRequestsCount()
        );
        statsLabel.setText(stats);
    }

    private void createRandomProcesses() {
        String[] fileNames = {
            "documento", "imagen", "archivo", "reporte", "datos",
            "backup", "config", "log", "temp", "usuario", "sistema",
            "aplicacion", "test", "ejemplo", "proyecto"
        };

        String[] extensions = {".txt", ".pdf", ".jpg", ".dat", ".log", ".cfg", ".java", ".xml"};
        String[] paths = {"/root", "/root/home", "/root/documents", "/root/temp", "/root/projects"};

        int processesCreated = 0;

        for (int i = 0; i < 10; i++) {
            String randomPath = paths[(int) (Math.random() * paths.length)];
            String randomName = fileNames[(int) (Math.random() * fileNames.length)]
                    + "_" + (i + 1)
                    + extensions[(int) (Math.random() * extensions.length)];
            int randomSize = (int) (Math.random() * 5) + 1; // 1-5 bloques

            Process process = processManager.createFileProcess(randomPath, randomName, randomSize);
            processManager.submitIORequest(process);
            processesCreated++;
        }

        JOptionPane.showMessageDialog(this,
                processesCreated + " procesos de CREACIÓN aleatorios\n"
                + "?Rutas variadas •  Tamaños: 1-5 bloques\n"
                + " En cola para planificación",
                "Procesos Aleatorios Creados",
                JOptionPane.INFORMATION_MESSAGE);

        updateDisplay();
    }
}