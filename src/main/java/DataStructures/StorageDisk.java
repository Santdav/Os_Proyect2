/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

/**
 *
 * @author santi
 */
public class StorageDisk {
    private Block[] blocks;
    private int totalBlocks;
    private int freeBlocks;
    
    public StorageDisk(int totalBlocks) {
        this.totalBlocks = totalBlocks;
        this.freeBlocks = totalBlocks;
        this.blocks = new Block[totalBlocks];
        
        // Inicializar todos los bloques
        for (int i = 0; i < totalBlocks; i++) {
            blocks[i] = new Block(i);
        }
    }
    
    // ==================== GETTERS BÁSICOS ====================
    public int getTotalBlocks() {
        return totalBlocks;
    }
    
    public int getFreeBlocks() {
        return freeBlocks;
    }
    
    public int getUsedBlocks() {
        return totalBlocks - freeBlocks;
    }
    
    public Block getBlock(int blockNumber) {
        if (blockNumber < 0 || blockNumber >= totalBlocks) {
            throw new IndexOutOfBoundsException("Block number out of range: " + blockNumber);
        }
        return blocks[blockNumber];
    }
    
    public Block[] getAllBlocks() {
        return blocks;
    }
    
    // ==================== GESTIÓN DE ESPACIO ====================
    public boolean hasEnoughSpace(int numBlocks) {
        return freeBlocks >= numBlocks;
    }
    
    public double getUsedSpacePercentage() {
        return ((double) getUsedBlocks() / totalBlocks) * 100;
    }
    
    // ==================== ASIGNACIÓN DE BLOQUES ====================
    public LinkedList<Integer> findFreeBlocks(int numBlocks) {
        LinkedList<Integer> freeBlockList = new LinkedList<>();
        
        if (numBlocks > freeBlocks) {
            return freeBlockList; // Retorna lista vacía si no hay espacio suficiente
        }
        
        // Buscar bloques libres consecutivos
        for (int i = 0; i < totalBlocks && freeBlockList.size() < numBlocks; i++) {
            if (blocks[i].isFree()) {
                freeBlockList.add(i);
            }
        }
        
        return freeBlockList;
    }
    
    public boolean allocateBlocks(LinkedList<Integer> blockNumbers, String ownerFile) {
        if (blockNumbers.size() > freeBlocks) {
            return false;
        }

        // VERIFICAR que todos los bloques estén libres ANTES de asignar
        for (int i = 0; i < blockNumbers.size(); i++) {
            int blockNum = blockNumbers.get(i);
            if (blockNum < 0 || blockNum >= totalBlocks || !blocks[blockNum].isFree()) {
                return false;
            }
        }

        // AHORA SÍ asignar todos los bloques
        for (int i = 0; i < blockNumbers.size(); i++) {
            int blockNum = blockNumbers.get(i);
            blocks[blockNum].setFree(false);
            blocks[blockNum].setOwnerFile(ownerFile);
        }

        freeBlocks -= blockNumbers.size();
        return true;
    }
    
    public void freeBlock(int blockNumber) {
        if (blockNumber < 0 || blockNumber >= totalBlocks) {
            System.out.println("DEBUG: Intento de liberar bloque inválido: " + blockNumber);
            return;
        }

        Block block = blocks[blockNumber];
        if (block.isFree()) {
            System.out.println("DEBUG: Bloque " + blockNumber + " ya está libre");
            return;
        }

        System.out.println("DEBUG: Liberando bloque " + blockNumber + " (era de: " + block.getOwnerFile() + ")");
        block.clear();
        freeBlocks++;

        System.out.println("DEBUG: Bloque " + blockNumber + " liberado. Libres: " + freeBlocks);
    }
    
    public void freeBlockChain(Block firstBlock) {
        if (firstBlock == null) return;

        System.out.println("DEBUG: Iniciando liberación de cadena desde bloque: " + firstBlock.getBlockNumber());

        Block current = firstBlock;
        int bloquesLiberados = 0;

        while (current != null) {
            // GUARDAR la referencia al siguiente bloque ANTES de liberar el actual
            Block nextBlock = current.getNextBlock();

            System.out.println("DEBUG: Liberando bloque: " + current.getBlockNumber());
            freeBlock(current.getBlockNumber());
            bloquesLiberados++;

            // Avanzar al siguiente bloque (que guardamos antes de liberar)
            current = nextBlock;
        }

        System.out.println("DEBUG: Total bloques liberados: " + bloquesLiberados);
    }
    
    // ==================== MÉTODOS DE CONSULTA ====================
    public boolean isBlockFree(int blockNumber) {
        if (blockNumber < 0 || blockNumber >= totalBlocks) {
            return false;
        }
        return blocks[blockNumber].isFree();
    }
    
    public String getBlockOwner(int blockNumber) {
        if (blockNumber < 0 || blockNumber >= totalBlocks) {
            return null;
        }
        return blocks[blockNumber].getOwnerFile();
    }
    
    public LinkedList<Integer> getBlocksByOwner(String ownerFile) {
        LinkedList<Integer> ownedBlocks = new LinkedList<>();
        for (int i = 0; i < totalBlocks; i++) {
            if (!blocks[i].isFree() && ownerFile.equals(blocks[i].getOwnerFile())) {
                ownedBlocks.add(i);
            }
        }
        return ownedBlocks;
    }
    
    // ==================== ESTADO DEL DISCO ====================
    public String getDiskStatus() {
        return String.format("Disk: %d/%d blocks used (%.1f%%)", 
                           getUsedBlocks(), totalBlocks, getUsedSpacePercentage());
    }
    
    @Override
    public String toString() {
        return getDiskStatus();
    }
    
    // ==================== MÉTODOS PARA UI ====================
    public LinkedList<Block> getOccupiedBlocks() {
        LinkedList<Block> occupied = new LinkedList<>();
        for (Block block : blocks) {
            if (!block.isFree()) {
                occupied.add(block);
            }
        }
        return occupied;
    }
    
    public LinkedList<Block> getFreeBlocksList() {
        LinkedList<Block> free = new LinkedList<>();
        for (Block block : blocks) {
            if (block.isFree()) {
                free.add(block);
            }
        }
        return free;
    }
}