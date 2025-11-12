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
        
        ExecutorService executor = Executors.newSingleThreadExecutor();
        
        // Ejecutar mientras haya tareas pendientes
        while (!colaTareas.isEmpty()) {
            TareaRR tarea = colaTareas.poll();
            
            // Si es la primera vez que se ejecuta
            if (!tarea.haIniciado()) {
                tarea.setTiempoInicio(System.currentTimeMillis());
                System.out.println("[RoundRobin] Primera ejecución de: " + tarea);
            } else {
                System.out.println("[RoundRobin] Reanudando: " + tarea);
                contextSwitches++;
            }
            
            try {
                // Ejecutar durante un quantum
                Future<?> future = executor.submit(tarea.getMetodo());
                
                try {
                    // Esperar el quantum o hasta que termine
                    future.get(quantum, TimeUnit.MILLISECONDS);
                    
                    // Si llegó aquí, terminó antes del quantum
                    tarea.marcarCompletada();
                    tarea.setTiempoFin(System.currentTimeMillis());
                    
                    System.out.println("[RoundRobin] ✓ Tarea completada: " + tarea);
                    System.out.println("       - Tiempo total: " + tarea.getMetodo().getTiempoEjecucion() + " ms");
                    System.out.println("       - Quantums usados: " + tarea.getQuantumsUsados());
                    
                    // Acumular métricas
                    sumaEspera += tarea.getTiempoEspera();
                    sumaRespuesta += tarea.getTiempoRespuesta();
                    tareasCompletadas++;
                    
                } catch (TimeoutException e) {
                    // Se agotó el quantum, reencolar
                    tarea.incrementarQuantums();
                    colaTareas.offer(tarea);
                    
                    System.out.println("[RoundRobin] Quantum agotado, reencolando: " + tarea);
                    System.out.println("       - Quantums usados: " + tarea.getQuantumsUsados());
                    
                    // No cancelar el future, dejar que la tarea siga en background
                    // NOTA: En implementación real, necesitarías mecanismo para pausar/reanudar
                }
                
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("[RoundRobin] Error ejecutando tarea: " + e.getMessage());
                tareasCompletadas++;
            }
            
            System.out.println();
        }
        
        executor.shutdown();
        
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