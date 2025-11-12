package planificadores;

import metodos.MetodoResolucion;
import circuitos.Circuito;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Planificador Round Robin
 * 
 * Características:
 * - Expulsivo (preemptive)
 * - Cada tarea recibe un quantum de tiempo
 * - Rota circularmente entre todas las tareas
 * - Bueno para fairness, evita inanición
 */
public class RoundRobin implements Planificador {
    
    private Queue<TareaRR> colaTareas;
    private MetricasPlanificacion metricas;
    private long quantum; // Tiempo de CPU asignado en milisegundos
    
    public RoundRobin(long quantum) {
        this.colaTareas = new LinkedList<>();
        this.metricas = new MetricasPlanificacion();
        this.quantum = quantum;
    }
    
    public RoundRobin() {
        this(100); // Quantum por defecto: 100ms
    }
    
    @Override
    public String getNombre() {
        return "Round Robin (quantum=" + quantum + "ms)";
    }
    
    @Override
    public void agregarTarea(MetodoResolucion metodo, Circuito circuito) {
        metodo.setCircuito(circuito);
        TareaRR tarea = new TareaRR(metodo, circuito);
        colaTareas.offer(tarea);
        System.out.println("[RoundRobin] Tarea agregada: " + tarea);
    }
    
    @Override
    public void ejecutar() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("EJECUTANDO: " + getNombre());
        System.out.println("Tareas en cola: " + colaTareas.size());
        System.out.println("=".repeat(60) + "\n");
        
        long tiempoInicioTotal = System.currentTimeMillis();
        int tareasCompletadas = 0;
        int contextSwitches = 0;
        long sumaEspera = 0;
        long sumaRespuesta = 0;
        
        // Crear executor con thread por tarea (mejor control)
        ExecutorService executor = Executors.newCachedThreadPool();
        
        while (!colaTareas.isEmpty()) {
            TareaRR tarea = colaTareas.poll();
            
            if (!tarea.haIniciado()) {
                tarea.setTiempoInicio(System.currentTimeMillis());
                System.out.println("[RoundRobin] Primera ejecución de: " + tarea);
            } else {
                System.out.println("[RoundRobin] Reanudando: " + tarea);
                contextSwitches++;
            }
            
            try {
                Future<?> future = executor.submit(tarea.getMetodo());
                
                try {
                    // Esperar con timeout
                    future.get(quantum, TimeUnit.MILLISECONDS);
                    
                    // Terminó antes del quantum
                    tarea.marcarCompletada();
                    tarea.setTiempoFin(System.currentTimeMillis());
                    
                    System.out.println("[RoundRobin] ✓ Completada: " + tarea);
                    System.out.println("       - Tiempo: " + tarea.getMetodo().getTiempoEjecucion() + " ms");
                    System.out.println("       - Quantums: " + tarea.getQuantumsUsados());
                    
                    sumaEspera += tarea.getTiempoEspera();
                    sumaRespuesta += tarea.getTiempoRespuesta();
                    tareasCompletadas++;
                    
                } catch (TimeoutException e) {
                    // ✓ MEJORADO: Cancelar el future
                    boolean cancelado = future.cancel(true);  // true = interrumpir
                    
                    if (cancelado) {
                        System.out.println("[RoundRobin] ⏱ Quantum agotado, interrumpida");
                    } else {
                        System.out.println("[RoundRobin] ⚠ No se pudo interrumpir (en punto no-cancelable)");
                    }
                    
                    tarea.incrementarQuantums();
                    
                    // Límite de quantums (evitar loop infinito)
                    if (tarea.getQuantumsUsados() > 10) {
                        System.out.println("[RoundRobin] ✗ Máximo de quantums excedido, descartando");
                        tareasCompletadas++;
                    } else {
                        colaTareas.offer(tarea);
                        System.out.println("[RoundRobin] Reencolada (quantum " + tarea.getQuantumsUsados() + ")");
                    }
                }
                
            } catch (InterruptedException e) {
                System.err.println("[RoundRobin] Interrumpida: " + e.getMessage());
                Thread.currentThread().interrupt();
                tareasCompletadas++;
            } catch (ExecutionException e) {
                System.err.println("[RoundRobin] Error: " + e.getMessage());
                tareasCompletadas++;
            }
            
            System.out.println();
        }
        
        executor.shutdownNow();  // Fuerza cierre de threads pendientes
        
        // Calcular métricas finales
        long tiempoFinTotal = System.currentTimeMillis();
        metricas.setTiempoTotal(tiempoFinTotal - tiempoInicioTotal);
        metricas.setTareasCompletadas(tareasCompletadas);
        metricas.setContextSwitches(contextSwitches);
        
        if (tareasCompletadas > 0) {
            metricas.setTiempoEsperaPromedio(sumaEspera / tareasCompletadas);
            metricas.setTiempoRespuestaPromedio(sumaRespuesta / tareasCompletadas);
        }
        
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
    
    public void setQuantum(long quantum) {
        this.quantum = quantum;
    }
    
    public long getQuantum() {
        return quantum;
    }
}

/**
 * Extensión de Tarea para Round Robin
 * Incluye información sobre quantums usados
 */
class TareaRR extends Tarea {
    private int quantumsUsados;
    private boolean completada;
    
    public TareaRR(MetodoResolucion metodo, Circuito circuito) {
        super(metodo, circuito);
        this.quantumsUsados = 0;
        this.completada = false;
    }
    
    public void incrementarQuantums() {
        quantumsUsados++;
    }
    
    public int getQuantumsUsados() {
        return quantumsUsados;
    }
    
    public boolean haIniciado() {
        return getTiempoInicio() > 0;
    }
    
    public void marcarCompletada() {
        this.completada = true;
    }
    
    public boolean estaCompletada() {
        return completada;
    }
}