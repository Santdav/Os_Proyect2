/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package LogicalStrucures;

import DataStructures.FileSystemElement;
import DataStructures.LinkedList;
import LogicalStrucures.File;

/**
 *
 * @author santi
 */
public class Directory extends FileSystemElement {
    private Directory parent;
    private LinkedList<FileSystemElement> children;
    
    public Directory(String name, String owner, Directory parent) {
        super(name, owner);
        this.parent = parent;
        this.children = new LinkedList<>();
    }
    
    @Override
    public boolean isDirectory() {
        return true;
    }
    
    @Override
    public int getSize() {
        return children.size();
    }
    
    @Override
    public String getPath() {
        if (parent == null) {
            return "/" + name;
        }
        return parent.getPath() + "/" + name;
    }
    
    // Métodos para gestionar hijos
    public void addChild(FileSystemElement element) {
        children.add(element);
    }
    
    public boolean removeChild(FileSystemElement element) {
        return children.remove(element);
    }
    
    public boolean removeChild(String name) {
        for (int i = 0; i < children.size(); i++) {
            FileSystemElement element = children.get(i);
            if (element.getName().equals(name)) {
                return children.remove(element);
            }
        }
        return false;
    }
    
    public FileSystemElement getChild(String name) {
        for (int i = 0; i < children.size(); i++) {
            FileSystemElement element = children.get(i);
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }
    
    // Getters y setters
    public Directory getParent() {
        return parent;
    }
    
    public void setParent(Directory parent) {
        this.parent = parent;
    }
    
    public LinkedList<FileSystemElement> getChildren() {
        return children;
    }
    
    // Método para verificar si existe un hijo con ese nombre
    public boolean contains(String name) {
        return getChild(name) != null;
    }
    
    // Método para obtener solo los archivos
    public LinkedList<File> getFiles() {
        LinkedList<File> files = new LinkedList<>();
        for (int i = 0; i < children.size(); i++) {
            FileSystemElement element = children.get(i);
            if (!element.isDirectory()) {
                files.add((File) element);
            }
        }
        return files;
    }
    
    // Método para obtener solo los subdirectorios
    public LinkedList<Directory> getSubdirectories() {
        LinkedList<Directory> directories = new LinkedList<>();
        for (int i = 0; i < children.size(); i++) {
            FileSystemElement element = children.get(i);
            if (element.isDirectory()) {
                directories.add((Directory) element);
            }
        }
        return directories;
    }
    
    // Método para buscar recursivamente un archivo o directorio
    public FileSystemElement findElement(String path) {
        if (path == null || path.isEmpty() || path.equals("/")) {
            return this;
        }

        // Normalizar el path (remover / inicial si existe)
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        if (normalizedPath.isEmpty()) {
            return this;
        }

        String[] parts = normalizedPath.split("/");
        return findElementRecursive(parts, 0);
    }
    
    private FileSystemElement findElementRecursive(String[] parts, int index) {
        if (index >= parts.length) {
            return this;
        }

        String currentPart = parts[index];
        if (currentPart.isEmpty() || currentPart.equals(".")) {
            return findElementRecursive(parts, index + 1);
        }

        if (currentPart.equals("..")) {
            if (parent != null) {
                return parent.findElementRecursive(parts, index + 1);
            }
            return null;
        }

        // Buscar en los hijos directos
        for (int i = 0; i < children.size(); i++) {
            FileSystemElement child = children.get(i);
            if (child.getName().equals(currentPart)) {
                if (index == parts.length - 1) {
                    return child; // Encontrado el elemento final
                } else if (child.isDirectory()) {
                    return ((Directory) child).findElementRecursive(parts, index + 1);
                } else {
                    return null; // No es directorio pero hay más partes en el path
                }
            }
        }

        return null; // No encontrado
    }

    @Override
    public boolean isFile() {
        return false;
    }
}