/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Schedulers;
import DataStructures.LinkedList;
import LogicalStrucures.Process;
/**
 *
 * @author santi
*/

/**
 * Interfaz para los algoritmos de planificación de disco.
 * Define el método para seleccionar el próximo proceso a ejecutar.
 */
/**
 * Interfaz para los algoritmos de planificación de disco.
 * Ahora trabaja con IORequest en lugar de Process.
 */
public interface DiskScheduler {
    Process selectNextProcess(LinkedList<Process> requestQueue);
    String getAlgorithmName();
    String getAlgorithmDescription();
}