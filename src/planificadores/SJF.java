package planificadores;

import metodos.MetodoResolucion;
import circuitos.Circuito;
import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * Planificador SJF (Shortest Job First)
 * 
 * Características:
 * - No expulsivo (versión básica)
 * - Ejecuta primero las tareas con menor tiempo estimado
 * - Minimiza tiempo de espera promedio
 * - Puede causar inanición de tareas largas
 * - Requiere estimación del tiempo de ejecución
 */
public class SJF implements Planificador {
    
    private PriorityQueue<Tarea> colaTareas;
    private MetricasPlanificacion metricas;
    
    public SJF() {
        // Cola de prioridad ordenada por tiempo estimado (menor primero)
        this.colaTareas = new PriorityQueue<>(
            Comparator.comparingLong(Tarea::getTiempoEjecucionEstimado)
        );
        this.metricas = new MetricasPlanificacion();
    }
    
    @Override
    public String getNombre() {
        return "SJF (Shortest Job First)";
    }
    
    @Override
    public void agregarTarea(MetodoResolucion metodo, Circuito circuito) {
        metodo.setCircuito(circuito);
        Tarea tarea = new Tarea(metodo, circuito);
        colaTareas.offer(tarea);
        
        System.out.println("[SJF] Tarea agregada: " + tarea + 
            " (estimado: " + tarea.getTiempoEjecucionEstimado() + " ms)");
    }
    
    @Override
    public void ejecutar() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EJECUTANDO: " + getNombre());
        System.out.println("Tareas en cola: " + colaTareas.size());
        System.out.println("=".repeat(60) + "\n");
        
        // Mostrar orden de ejecución planificado
        mostrarOrdenPlanificado();
        
        long tiempoInicioTotal = System.currentTimeMillis();
        int tareasCompletadas = 0;
        long sumaEspera = 0;
        long sumaRespuesta = 0;
        long sumaError = 0; // Para calcular precisión de estimaciones
        
        // Ejecutar tareas en orden de menor tiempo estimado
        while (!colaTareas.isEmpty()) {
            Tarea tarea = colaTareas.poll();
            
            System.out.println("[SJF] Ejecutando tarea " + (tareasCompletadas + 1) + ": " + tarea);
            System.out.println("      Tiempo estimado: " + tarea.getTiempoEjecucionEstimado() + " ms");
            
            // Marcar tiempo de inicio
            tarea.setTiempoInicio(System.currentTimeMillis());
            
            // Ejecutar el método en un hilo
            MetodoResolucion metodo = tarea.getMetodo();
            Thread hilo = new Thread(metodo);
            hilo.start();
            
            try {
                hilo.join(); // Esperar a que termine (no expulsivo)
            } catch (InterruptedException e) {
                System.err.println("[SJF] Error: Tarea interrumpida - " + e.getMessage());
            }
            
            // Marcar tiempo de finalización
            tarea.setTiempoFin(System.currentTimeMillis());
            
            // Calcular error de estimación
            long tiempoReal = metodo.getTiempoEjecucion();
            long tiempoEstimado = tarea.getTiempoEjecucionEstimado();
            long errorEstimacion = Math.abs(tiempoReal - tiempoEstimado);
            sumaError += errorEstimacion;
            
            // Acumular métricas
            sumaEspera += tarea.getTiempoEspera();
            sumaRespuesta += tarea.getTiempoRespuesta();
            tareasCompletadas++;
            
            // Mostrar resultado
            if (metodo.getEstado() == MetodoResolucion.EstadoMetodo.TERMINADO) {
                System.out.println("[SJF] ✓ Tarea completada");
                System.out.println("       - Tiempo real: " + tiempoReal + " ms");
                System.out.println("       - Tiempo estimado: " + tiempoEstimado + " ms");
                System.out.println("       - Error: " + errorEstimacion + " ms");
                System.out.println("       - Tiempo de espera: " + tarea.getTiempoEspera() + " ms");
                System.out.println("       - Tiempo de respuesta: " + tarea.getTiempoRespuesta() + " ms");
            } else {
                System.out.println("[SJF] ✗ Tarea falló");
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
            
            long errorPromedio = sumaError / tareasCompletadas;
            System.out.println("[SJF] Error de estimación promedio: " + errorPromedio + " ms");
        }
        
        // SJF no expulsivo no tiene context switches
        metricas.setContextSwitches(0);
        
        mostrarResumen();
    }
    
    /**
     * Muestra el orden planificado de ejecución (sin modificar la cola)
     */
    private void mostrarOrdenPlanificado() {
        System.out.println("[SJF] Orden de ejecución planificado:");
        
        // Crear copia temporal para no modificar la cola original
        PriorityQueue<Tarea> copiaCola = new PriorityQueue<>(
            Comparator.comparingLong(Tarea::getTiempoEjecucionEstimado)
        );
        copiaCola.addAll(colaTareas);
        
        int posicion = 1;
        while (!copiaCola.isEmpty()) {
            Tarea t = copiaCola.poll();
            System.out.println("  " + posicion + ". " + t + 
                " (est: " + t.getTiempoEjecucionEstimado() + " ms)");
            posicion++;
        }
        System.out.println();
    }
    
    private void mostrarResumen() {
        System.out.println("=".repeat(60));
        System.out.println("RESUMEN DE EJECUCIÓN - " + getNombre());
        System.out.println("=".repeat(60));
        System.out.println(metricas);
        System.out.println("Ventaja: Minimiza tiempo de espera promedio");
        System.out.println("Desventaja: Puede causar inanición en tareas largas");
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