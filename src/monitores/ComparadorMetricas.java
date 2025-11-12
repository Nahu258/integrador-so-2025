package monitores;

import planificadores.MetricasPlanificacion;
import java.io.*;
import java.util.*;

/**
 * Genera tablas y reportes comparativos
 */
public class ComparadorMetricas {
    
    private Map<String, MetricasPlanificacion> metricas;
    
    public ComparadorMetricas() {
        this.metricas = new HashMap<>();
    }
    
    public void agregar(String nombre, MetricasPlanificacion metrica) {
        metricas.put(nombre, metrica);
    }
    
    /**
     * Genera tabla comparativa en formato CSV
     */
    public void generarCSV(String nombreArchivo) {
        try {
            File archivo = new File("resultados/metricas/" + nombreArchivo);
            archivo.getParentFile().mkdirs();
            
            PrintWriter writer = new PrintWriter(new FileWriter(archivo));
            
            // Encabezado
            writer.println("Planificador,Tiempo_Total_ms,Espera_Promedio_ms,Respuesta_Promedio_ms,Context_Switches,Tareas_Completadas");
            
            // Datos
            for (Map.Entry<String, MetricasPlanificacion> entry : metricas.entrySet()) {
                MetricasPlanificacion m = entry.getValue();
                writer.printf("%s,%d,%d,%d,%d,%d\n",
                    entry.getKey(),
                    m.getTiempoTotal(),
                    m.getTiempoEsperaPromedio(),
                    m.getTiempoRespuestaPromedio(),
                    m.getContextSwitches(),
                    m.getTareasCompletadas()
                );
            }
            
            writer.close();
            System.out.println("[COMPARADOR] CSV generado: " + archivo.getPath());
            
        } catch (IOException e) {
            System.err.println("Error generando CSV: " + e.getMessage());
        }
    }
    
    /**
     * Genera reporte en formato Markdown
     */
    public void generarReporteMarkdown(String nombreArchivo) {
        try {
            File archivo = new File("resultados/metricas/" + nombreArchivo);
            archivo.getParentFile().mkdirs();
            
            PrintWriter writer = new PrintWriter(new FileWriter(archivo));
            
            writer.println("# Reporte Comparativo de Planificadores\n");
            writer.println("Fecha: " + new Date() + "\n");
            
            writer.println("## Resultados\n");
            writer.println("| Planificador | Tiempo Total | Espera Prom. | Respuesta Prom. | Context Switches |");
            writer.println("|-------------|--------------|--------------|-----------------|------------------|");
            
            for (Map.Entry<String, MetricasPlanificacion> entry : metricas.entrySet()) {
                MetricasPlanificacion m = entry.getValue();
                writer.printf("| %s | %d ms | %d ms | %d ms | %d |\n",
                    entry.getKey(),
                    m.getTiempoTotal(),
                    m.getTiempoEsperaPromedio(),
                    m.getTiempoRespuestaPromedio(),
                    m.getContextSwitches()
                );
            }
            
            writer.println("\n## Análisis\n");
            
            // Encontrar el mejor en cada categoría
            String mejorTiempoTotal = encontrarMejor("tiempo_total");
            String mejorEspera = encontrarMejor("espera");
            
            writer.println("- **Mejor tiempo total:** " + mejorTiempoTotal);
            writer.println("- **Mejor tiempo de espera:** " + mejorEspera);
            
            writer.close();
            System.out.println("[COMPARADOR] Reporte MD generado: " + archivo.getPath());
            
        } catch (IOException e) {
            System.err.println("Error generando reporte: " + e.getMessage());
        }
    }
    
    private String encontrarMejor(String criterio) {
        String mejor = "";
        long menorValor = Long.MAX_VALUE;
        
        for (Map.Entry<String, MetricasPlanificacion> entry : metricas.entrySet()) {
            MetricasPlanificacion m = entry.getValue();
            long valor = criterio.equals("tiempo_total") ? 
                m.getTiempoTotal() : m.getTiempoEsperaPromedio();
            
            if (valor < menorValor) {
                menorValor = valor;
                mejor = entry.getKey();
            }
        }
        
        return mejor + " (" + menorValor + " ms)";
    }
}