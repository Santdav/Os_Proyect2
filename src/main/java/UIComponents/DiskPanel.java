/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UIComponents;

/**
 *
 * @author santi
 */
import DataStructures.Block;
import LogicalStrucures.StorageDisk;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel para visualizar el estado del disco - Muestra bloques ocupados y libres
 */
public final class DiskPanel extends JPanel {
    private final StorageDisk disk;
    private JPanel blocksPanel;
    private JLabel statusLabel;
    private Map<Integer, JLabel> blockLabels;
    private static final int BLOCKS_PER_ROW = 10;
    
    // Colores para diferentes estados
    private static final Color FREE_COLOR = new Color(240, 240, 240);    // Gris claro
    private static final Color OCCUPIED_COLOR = new Color(70, 130, 180);  // Azul acero
    private static final Color SELECTED_COLOR = new Color(255, 215, 0);   // Amarillo oro
    private static final Color SYSTEM_COLOR = new Color(178, 34, 34);     // Rojo oscuro
    
    public DiskPanel(StorageDisk disk) {
        this.disk = disk;
        this.blockLabels = new HashMap<>();
        initializeComponents();
        setupLayout();
        updateDisplay();
    }
    
    private void initializeComponents() {
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 2), 
            "Simulación de Disco"
        ));
        
        // Panel de estado
        statusLabel = new JLabel("", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(Color.DARK_GRAY);
        
        // Panel para los bloques
        blocksPanel = new JPanel();
        int totalBlocks = disk.getTotalBlocks();
        int rows = (int) Math.ceil((double) totalBlocks / BLOCKS_PER_ROW);
        blocksPanel.setLayout(new GridLayout(rows, BLOCKS_PER_ROW, 2, 2));
        
        // Crear labels para cada bloque
        for (int i = 0; i < totalBlocks; i++) {
            JLabel blockLabel = createBlockLabel(i);
            blockLabels.put(i, blockLabel);
            blocksPanel.add(blockLabel);
        }
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(5, 5));
        
        // Panel superior con información
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(statusLabel);
        
        // Panel de leyenda
        JPanel legendPanel = createLegendPanel();
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(infoPanel, BorderLayout.NORTH);
        topPanel.add(legendPanel, BorderLayout.CENTER);
        
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(blocksPanel), BorderLayout.CENTER);
    }
    
    private JLabel createBlockLabel(int blockNumber) {
        JLabel label = new JLabel(String.valueOf(blockNumber), JLabel.CENTER);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        label.setFont(new Font("Arial", Font.PLAIN, 9));
        label.setPreferredSize(new Dimension(40, 40));
        
        // Tooltip inicial
        label.setToolTipText("Bloque " + blockNumber + " - Libre");
        
        // Listeners para hover effect
        label.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!disk.isBlockFree(blockNumber)) {
                    label.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            }
        });
        
        return label;
    }
    
    private JPanel createLegendPanel() {
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Leyenda"));
        
        legendPanel.add(createLegendItem(FREE_COLOR, "Libre"));
        legendPanel.add(createLegendItem(OCCUPIED_COLOR, "Ocupado"));
        legendPanel.add(createLegendItem(SYSTEM_COLOR, "Sistema"));
        
        return legendPanel;
    }
    
    private JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        
        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        colorLabel.setPreferredSize(new Dimension(15, 15));
        
        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        
        itemPanel.add(colorLabel);
        itemPanel.add(textLabel);
        
        return itemPanel;
    }
    
    public void updateDisplay() {
        updateStatus();
        updateBlocks();
        revalidate();
        repaint();
    }
    
    private void updateStatus() {
        int used = disk.getUsedBlocks();
        int total = disk.getTotalBlocks();
        double percentage = disk.getUsedSpacePercentage();
        
        String status = String.format("Disco: %d/%d bloques usados (%.1f%%) - %d bloques libres", 
                                    used, total, percentage, total - used);
        statusLabel.setText(status);
    }
    
    private void updateBlocks() {
        int totalBlocks = disk.getTotalBlocks();
        
        for (int i = 0; i < totalBlocks; i++) {
            JLabel blockLabel = blockLabels.get(i);
            Block block = disk.getBlock(i);
            
            if (block.isFree()) {
                blockLabel.setBackground(FREE_COLOR);
                blockLabel.setForeground(Color.BLACK);
                blockLabel.setToolTipText("Bloque " + i + " - Libre");
            } else {
                blockLabel.setBackground(OCCUPIED_COLOR);
                blockLabel.setForeground(Color.WHITE);
                
                String owner = block.getOwnerFile();
                if (owner != null && !owner.isEmpty()) {
                    blockLabel.setToolTipText("Bloque " + i + " - Ocupado por: " + owner);
                } else {
                    blockLabel.setToolTipText("Bloque " + i + " - Ocupado");
                }
            }
            
            // Resaltar bloques del sistema
            if (block.getOwnerFile() != null && 
                (block.getOwnerFile().contains("system") || 
                 block.getOwnerFile().contains("root"))) {
                blockLabel.setBackground(SYSTEM_COLOR);
            }
        }
    }
    
    /**
     * Resalta un bloque específico (útil para mostrar operaciones en curso)
     */
    public void highlightBlock(int blockNumber, boolean highlight) {
        JLabel blockLabel = blockLabels.get(blockNumber);
        if (blockLabel != null) {
            if (highlight) {
                blockLabel.setBorder(BorderFactory.createLineBorder(SELECTED_COLOR, 3));
                blockLabel.setBackground(SELECTED_COLOR);
            } else {
                blockLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                updateBlocks(); // Restaurar color original
            }
        }
    }
    
    /**
     * Resalta múltiples bloques (para mostrar archivos completos)
     */
    public void highlightBlocks(java.util.List<Integer> blockNumbers, boolean highlight) {
        for (int blockNumber : blockNumbers) {
            highlightBlock(blockNumber, highlight);
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Fondo con degradado suave
        Graphics2D g2d = (Graphics2D) g;
        Color color1 = new Color(245, 245, 245);
        Color color2 = new Color(255, 255, 255);
        g2d.setPaint(new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2));
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}