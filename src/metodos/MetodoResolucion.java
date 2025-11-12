package metodos;

import circuitos.Circuito;

/**
 * Interfaz base para todos los métodos de resolución de circuitos
 */
public interface MetodoResolucion extends Runnable {
    
    /**
     * Nombre identificador del método
     */
    String getNombre();
    
    /**
     * Resuelve el sistema de ecuaciones del circuito
     * @param circuito El circuito a resolver
     * @return Array con las corrientes de cada malla
     */
    double[] resolver(Circuito circuito);
    
    /**
     * Obtiene el tiempo de ejecución en milisegundos
     */
    long getTiempoEjecucion();
    
    /**
     * Obtiene la solución calculada
     */
    double[] getSolucion();
    
    /**
     * Establece el circuito a resolver
     */
    void setCircuito(Circuito circuito);
    
    /**
     * Obtiene el estado actual del método
     */
    EstadoMetodo getEstado();
    
    /**
     * Estados posibles de un método de resolución
     */
    enum EstadoMetodo {
        LISTO,      // Preparado para ejecutar
        EJECUTANDO, // En proceso de cálculo
        TERMINADO,  // Completado exitosamente
        ERROR       // Error durante ejecución
    }
}