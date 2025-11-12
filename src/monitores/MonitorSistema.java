package monitores;

import java.io.*;
import java.lang.management.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Monitor de métricas del sistema
 * Captura uso de CPU, memoria y tiempos de ejecución
 */
public class MonitorSistema {
    
    private static final OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    private static final MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
    private static final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
    private static final RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
    
    private long inicioMonitoreo;
    private String nombreArchivo;
    private PrintWriter writer;
    
    public MonitorSistema(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
        this.inicioMonitoreo = System.currentTimeMillis();
        
        try {
            File archivo = new File("resultados/metricas/" + nombreArchivo);
            archivo.getParentFile().mkdirs();
            writer = new PrintWriter(new FileWriter(archivo, true));
        } catch (IOException e) {
            System.err.println("Error creando monitor: " + e.getMessage());
        }
    }
    
    /**
     * Captura snapshot de métricas actuales
     */
    public void capturarMetricas(String etiqueta) {
        long timestamp = System.currentTimeMillis() - inicioMonitoreo;
        
        // Uso de CPU
        double cpuLoad = osBean.getSystemLoadAverage();
        
        // Memoria
        MemoryUsage heapMemory = memoryBean.getHeapMemoryUsage();
        long usedMemory = heapMemory.getUsed() / (1024 * 1024); // MB
        long maxMemory = heapMemory.getMax() / (1024 * 1024);
        
        // Hilos
        int threadCount = threadBean.getThreadCount();
        
        // Escribir a archivo
        String linea = String.format("%d,%s,%.2f,%d,%d,%d",
            timestamp, etiqueta, cpuLoad, usedMemory, maxMemory, threadCount);
        
        if (writer != null) {
            writer.println(linea);
            writer.flush();
        }
        
        // También imprimir en consola
        System.out.printf("[MONITOR] %s - CPU: %.2f | RAM: %d/%d MB | Hilos: %d\n",
            etiqueta, cpuLoad, usedMemory, maxMemory, threadCount);
    }
    
    /**
     * Genera encabezado del archivo CSV
     */
    public void iniciarArchivo() {
        if (writer != null) {
            writer.println("timestamp_ms,etiqueta,cpu_load,memoria_usada_mb,memoria_max_mb,hilos");
            writer.flush();
        }
    }
    
    /**
     * Cierra el monitor
     */
    public void cerrar() {
        if (writer != null) {
            writer.close();
        }
    }
    
    /**
     * Obtiene información del sistema
     */
    public static String getInfoSistema() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN DEL SISTEMA ===\n");
        info.append("OS: ").append(System.getProperty("os.name")).append("\n");
        info.append("Arquitectura: ").append(System.getProperty("os.arch")).append("\n");
        info.append("Procesadores: ").append(osBean.getAvailableProcessors()).append("\n");
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("JVM: ").append(runtimeBean.getVmName()).append("\n");
        return info.toString();
    }
}