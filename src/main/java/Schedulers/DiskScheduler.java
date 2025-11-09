/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Schedulers;
import DataStructures.Queue;
import LogicalStrucures.Process;
/**
 *
 * @author santi
*/

/**
 * Interfaz para los algoritmos de planificación de disco.
 * Define el método para seleccionar el próximo proceso a ejecutar.
 */
public interface DiskScheduler {
    
    /**
     * 
     * @param readyQueue 
     * @return
     */
    Process selectNextProcess(Queue<Process> readyQueue);
    
    /**
     * Retorna el nombre del algoritmo de planificación.
     * 
     * @return Nombre descriptivo del algoritmo
     */
    String getAlgorithmName();
    
    /**
     * Retorna una descripción del algoritmo de planificación.
     * 
     * @return Descripción del algoritmo
     */
    String getAlgorithmDescription();

}