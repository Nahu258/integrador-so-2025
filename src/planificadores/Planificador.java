package planificadores;

import metodos.MetodoResolucion;
import circuitos.Circuito;

/**
 * Interfaz para algoritmos de planificación de procesos
 * Gestiona la ejecución de múltiples métodos de resolución
 */
public interface Planificador {
    
    /**
     * Nombre del algoritmo de planificación
     */
    String getNombre();
    
    /**
     * Agrega un método a la cola de ejecución
     * @param metodo Método a ejecutar
     * @param circuito Circuito a resolver
     */
    void agregarTarea(MetodoResolucion metodo, Circuito circuito);
    
    /**
     * Ejecuta todos los métodos según el algoritmo de planificación
     */
    void ejecutar();
    
    /**
     * Obtiene las métricas de ejecución
     */
    MetricasPlanificacion getMetricas();
    
    /**
     * Verifica si hay tareas pendientes
     */
    boolean tieneTareasPendientes();
    
    /**
     * Limpia la cola de tareas
     */
    void limpiar();
}