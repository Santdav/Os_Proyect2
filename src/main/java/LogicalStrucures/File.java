/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicalStrucures;

import DataStructures.Block;
import DataStructures.FileSystemElement;

/**
 *
 * @author santi
 */
public class File extends FileSystemElement {
    private int sizeInBlocks;
    private Block firstBlock;
    private Directory parent;
    private String content;
    
    public File(String name, String owner, int sizeInBlocks, Directory parent) {
        super(name, owner);
        this.sizeInBlocks = sizeInBlocks;
        this.parent = parent;
        this.firstBlock = null;
        this.content = "";
    }
    
    
    
    @Override
    public boolean isDirectory() {
        return false;
    }
    
    @Override
    public int getSize() {
        return sizeInBlocks;
    }
    
    @Override
    public String getPath() {
        if (parent == null) {
            return "/" + name;
        }
        return parent.getPath() + "/" + name;
    }
    
    // Getters y setters
    public int getSizeInBlocks() {
        return sizeInBlocks;
    }
    
    public void setSizeInBlocks(int sizeInBlocks) {
        this.sizeInBlocks = sizeInBlocks;
    }
    
    public Block getFirstBlock() {
        return firstBlock;
    }
    
    public void setFirstBlock(Block firstBlock) {
        this.firstBlock = firstBlock;
    }
    
    public Directory getParent() {
        return parent;
    }
    
    public void setParent(Directory parent) {
        this.parent = parent;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    // Método auxiliar
    public boolean hasBlocksAssigned() {
        return firstBlock != null;
    }

    @Override
    public boolean isFile() {
        return true;
    }
    
    /*public boolean canRead(User currentUser) {
        if (currentUser.isAdmin()) return true;
        return getPermissions().canRead() && 
               (getPermissions().isPublic() || getOwner().equals(currentUser.getName()));
    }*/
}