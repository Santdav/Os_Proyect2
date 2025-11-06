/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataStructures;

/**
 *
 * @author santi
 */
public class Block {
    private int blockNumber;
    private Block nextBlock;
    private boolean isFree;
    private String ownerFile;
    private byte[] data;
    
    public Block(int blockNumber) {
        this.blockNumber = blockNumber;
        this.isFree = true;
        this.nextBlock = null;
        this.ownerFile = null;
        this.data = new byte[512]; // Tamaño típico de bloque - puedes ajustar
    }
    
    // ==================== GETTERS ====================
    public int getBlockNumber() {
        return blockNumber;
    }
    
    public Block getNextBlock() {
        return nextBlock;
    }
    
    public boolean isFree() {
        return isFree;
    }
    
    public String getOwnerFile() {
        return ownerFile;
    }
    
    public byte[] getData() {
        return data;
    }
    
    // ==================== SETTERS ====================
    public void setNextBlock(Block nextBlock) {
        this.nextBlock = nextBlock;
    }
    
    public void setFree(boolean free) {
        isFree = free;
        if (free) {
            this.ownerFile = null;
            clearData();
        }
    }
    
    public void setOwnerFile(String ownerFile) {
        this.ownerFile = ownerFile;
    }
    
    public void setData(byte[] data) {
        this.data = data;
    }
    
    // ==================== MÉTODOS DE UTILIDAD ====================
    public boolean hasNextBlock() {
        return nextBlock != null;
    }
    
    public void clearData() {
        this.data = new byte[512];
    }
    
    public void clear() {
    this.isFree = true;
    // NO establecer nextBlock = null aquí, porque necesitamos la referencia para liberar la cadena
    this.ownerFile = null;
    clearData();
}
    
    // ==================== MÉTODOS PARA CADENA DE BLOQUES ====================
    public Block getLastBlockInChain() {
        Block current = this;
        while (current.hasNextBlock()) {
            current = current.getNextBlock();
        }
        return current;
    }
    
    public int getChainLength() {
        int length = 1;
        Block current = this;
        while (current.hasNextBlock()) {
            current = current.getNextBlock();
            length++;
        }
        return length;
    }
    
    // ==================== TO STRING ====================
    @Override
    public String toString() {
        return String.format("Block[%d] - Free: %s, Owner: %s, Next: %s", 
            blockNumber, 
            isFree, 
            ownerFile != null ? ownerFile : "None",
            nextBlock != null ? nextBlock.getBlockNumber() : "None");
    }
}