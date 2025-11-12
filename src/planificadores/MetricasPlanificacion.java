package planificadores;

/**
 * Clase para almacenar métricas de un planificador
 */
public class MetricasPlanificacion {
    private long tiempoTotal;
    private long tiempoEsperaPromedio;
    private long tiempoRespuestaPromedio;
    private int tareasCompletadas;
    private int contextSwitches;
    
    public MetricasPlanificacion() {
        this.tiempoTotal = 0;
        this.tiempoEsperaPromedio = 0;
        this.tiempoRespuestaPromedio = 0;
        this.tareasCompletadas = 0;
        this.contextSwitches = 0;
    }
    
    // Getters y Setters
    public long getTiempoTotal() { return tiempoTotal; }
    public void setTiempoTotal(long tiempo) { this.tiempoTotal = tiempo; }
    
    public long getTiempoEsperaPromedio() { return tiempoEsperaPromedio; }
    public void setTiempoEsperaPromedio(long tiempo) { this.tiempoEsperaPromedio = tiempo; }
    
    public long getTiempoRespuestaPromedio() { return tiempoRespuestaPromedio; }
    public void setTiempoRespuestaPromedio(long tiempo) { this.tiempoRespuestaPromedio = tiempo; }
    
    public int getTareasCompletadas() { return tareasCompletadas; }
    public void setTareasCompletadas(int tareas) { this.tareasCompletadas = tareas; }
    
    public int getContextSwitches() { return contextSwitches; }
    public void setContextSwitches(int switches) { this.contextSwitches = switches; }
    public void incrementarContextSwitches() { this.contextSwitches++; }
    
    @Override
    public String toString() {
        return String.format(
            "Métricas:\n" +
            "  - Tiempo Total: %d ms\n" +
            "  - Tiempo Espera Promedio: %d ms\n" +
            "  - Tiempo Respuesta Promedio: %d ms\n" +
            "  - Tareas Completadas: %d\n" +
            "  - Context Switches: %d",
            tiempoTotal, tiempoEsperaPromedio, tiempoRespuestaPromedio, 
            tareasCompletadas, contextSwitches
        );
    }
}