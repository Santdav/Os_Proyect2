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
    private int blockNumber;    // Número del bloque (0, 1, 2...)
    private Block nextBlock;    // Siguiente bloque en la cadena ← IMPORTANTE
    private boolean isFree;     // true = libre, false = ocupado
    private String ownerFile;   // Qué archivo lo usa (opcional)
    
    // Constructor
    public Block(int blockNumber) {
        this.blockNumber = blockNumber;
        this.isFree = true;
        this.nextBlock = null;
        this.ownerFile = null;
    }
    
    // Getters y setters básicos
    // (métodos para get/set nextBlock, isFree, ownerFile)
}