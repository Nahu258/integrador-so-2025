package planificadores;

import metodos.MetodoResolucion;
import circuitos.Circuito;

/**
 * Clase que encapsula una tarea (método + circuito)
 */
public class Tarea {
    private MetodoResolucion metodo;
    private Circuito circuito;
    private long tiempoLlegada;
    private long tiempoInicio;
    private long tiempoFin;
    private long tiempoEjecucionEstimado;
    
    public Tarea(MetodoResolucion metodo, Circuito circuito) {
        this.metodo = metodo;
        this.circuito = circuito;
        this.tiempoLlegada = System.currentTimeMillis();
        this.tiempoInicio = 0;
        this.tiempoFin = 0;
        
        // Estimación basada en complejidad del circuito
        this.tiempoEjecucionEstimado = estimarTiempoEjecucion(metodo, circuito);
    }
    
    /**
     * Estima el tiempo de ejecución basado en el método y complejidad
     */
    private long estimarTiempoEjecucion(MetodoResolucion metodo, Circuito circuito) {
        String nombreMetodo = metodo.getNombre();
        int numMallas = circuito.getNumMallas();
        
        // Estimaciones aproximadas en milisegundos
        if (nombreMetodo.equals("Cramer")) {
            // Cramer crece factorialmente
            return (long) (Math.pow(2, numMallas) * 0.5);
        } else if (nombreMetodo.equals("Gauss-Jordan")) {
            // O(n³)
            return (long) (Math.pow(numMallas, 3) * 0.1);
        } else {
            // Librería (más optimizado)
            return (long) (Math.pow(numMallas, 2.5) * 0.1);
        }
    }
    
    public MetodoResolucion getMetodo() { return metodo; }
    public Circuito getCircuito() { return circuito; }
    
    public long getTiempoLlegada() { return tiempoLlegada; }
    public long getTiempoInicio() { return tiempoInicio; }
    public void setTiempoInicio(long tiempo) { this.tiempoInicio = tiempo; }
    
    public long getTiempoFin() { return tiempoFin; }
    public void setTiempoFin(long tiempo) { this.tiempoFin = tiempo; }
    
    public long getTiempoEjecucionEstimado() { return tiempoEjecucionEstimado; }
    
    public long getTiempoEspera() {
        return tiempoInicio - tiempoLlegada;
    }
    
    public long getTiempoRespuesta() {
        return tiempoFin - tiempoLlegada;
    }
    
    @Override
    public String toString() {
        return String.format("[%s - %s]", 
            metodo.getNombre(), circuito.getNombre());
    }
}