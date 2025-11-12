package planificadores;

import metodos.MetodoResolucion;
import circuitos.Circuito;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Planificador FCFS (First-Come, First-Served)
 * 
 * Características:
 * - No expulsivo (non-preemptive)
 * - Ejecuta tareas en orden de llegada
 * - Simple pero puede sufrir de "convoy effect"
 * - No hay context switches entre tareas
 */
public class FCFS implements Planificador {
    
    private Queue<Tarea> colaTareas;
    private MetricasPlanificacion metricas;
    
    public FCFS() {
        this.colaTareas = new LinkedList<>();
        this.metricas = new MetricasPlanificacion();
    }
    
    @Override
    public String getNombre() {
        return "FCFS (First-Come First-Served)";
    }
    
    @Override
    public void agregarTarea(MetodoResolucion metodo, Circuito circuito) {
        metodo.setCircuito(circuito);
        Tarea tarea = new Tarea(metodo, circuito);
        colaTareas.offer(tarea);
        System.out.println("[FCFS] Tarea agregada: " + tarea);
    }
    
    @Override
    public void ejecutar() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EJECUTANDO: " + getNombre());
        System.out.println("Tareas en cola: " + colaTareas.size());
        System.out.println("=".repeat(60) + "\n");
        
        long tiempoInicioTotal = System.currentTimeMillis();
        int tareasCompletadas = 0;
        long sumaEspera = 0;
        long sumaRespuesta = 0;
        
        // Ejecutar cada tarea en orden FIFO
        while (!colaTareas.isEmpty()) {
            Tarea tarea = colaTareas.poll();
            
            System.out.println("[FCFS] Ejecutando tarea " + (tareasCompletadas + 1) + ": " + tarea);
            
            // Marcar tiempo de inicio
            tarea.setTiempoInicio(System.currentTimeMillis());
            
            // Ejecutar el método en un hilo
            MetodoResolucion metodo = tarea.getMetodo();
            Thread hilo = new Thread(metodo);
            hilo.start();
            
            try {
                hilo.join(); // Esperar a que termine (no expulsivo)
            } catch (InterruptedException e) {
                System.err.println("[FCFS] Error: Tarea interrumpida - " + e.getMessage());
            }
            
            // Marcar tiempo de finalización
            tarea.setTiempoFin(System.currentTimeMillis());
            
            // Acumular métricas
            sumaEspera += tarea.getTiempoEspera();
            sumaRespuesta += tarea.getTiempoRespuesta();
            tareasCompletadas++;
            
            // Mostrar resultado
            if (metodo.getEstado() == MetodoResolucion.EstadoMetodo.TERMINADO) {
                System.out.println("[FCFS] ✓ Tarea completada");
                System.out.println("       - Tiempo de ejecución: " + metodo.getTiempoEjecucion() + " ms");
                System.out.println("       - Tiempo de espera: " + tarea.getTiempoEspera() + " ms");
                System.out.println("       - Tiempo de respuesta: " + tarea.getTiempoRespuesta() + " ms");
            } else {
                System.out.println("[FCFS] ✗ Tarea falló");
            }
            System.out.println();
        }
        
        // Calcular métricas finales
        long tiempoFinTotal = System.currentTimeMillis();
        metricas.setTiempoTotal(tiempoFinTotal - tiempoInicioTotal);
        metricas.setTareasCompletadas(tareasCompletadas);
        
        if (tareasCompletadas > 0) {
            metricas.setTiempoEsperaPromedio(sumaEspera / tareasCompletadas);
            metricas.setTiempoRespuestaPromedio(sumaRespuesta / tareasCompletadas);
        }
        
        // FCFS no tiene context switches (cada tarea se ejecuta hasta el final)
        metricas.setContextSwitches(0);
        
        mostrarResumen();
    }
    
    private void mostrarResumen() {
        System.out.println("=".repeat(60));
        System.out.println("RESUMEN DE EJECUCIÓN - " + getNombre());
        System.out.println("=".repeat(60));
        System.out.println(metricas);
        System.out.println("=".repeat(60) + "\n");
    }
    
    @Override
    public MetricasPlanificacion getMetricas() {
        return metricas;
    }
    
    @Override
    public boolean tieneTareasPendientes() {
        return !colaTareas.isEmpty();
    }
    
    @Override
    public void limpiar() {
        colaTareas.clear();
        metricas = new MetricasPlanificacion();
    }
}