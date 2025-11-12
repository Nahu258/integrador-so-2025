import circuitos.Circuito;
import metodos.*;
import monitores.MonitorSistema;
import planificadores.*;

/**
 * Clase principal - Prueba de planificadores
 * Compara FCFS, Round Robin y SJF
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE RESOLUCIÓN DE CIRCUITOS - FASE 2             ║");
        System.out.println("║   Comparación de Algoritmos de Planificación              ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // Determinar qué prueba ejecutar
        if (args.length == 0) {
            System.out.println("Uso: java -cp bin Main <opcion>");
            System.out.println("Opciones:");
            System.out.println("  --basico     : Prueba básica sin planificadores");
            System.out.println("  --simple     : Solo circuito simple con 3 planificadores");
            System.out.println("  --medio      : Solo circuito medio con 3 planificadores");
            System.out.println("  --complejo   : Solo circuito complejo con 3 planificadores");
            System.out.println("  --mix        : Solo escenario mix");
            System.out.println("  --todo       : Ejecutar todo (por defecto)");
            return;
        }

        String opcion = args[0];

        if (opcion.equals("--basico")) {
            pruebaBasica();
        } else if (opcion.equals("--simple")) {
            probarSoloCircuito("simple");
        } else if (opcion.equals("--medio")) {
            probarSoloCircuito("medio");
        } else if (opcion.equals("--complejo")) {
            probarSoloCircuito("complejo");
        } else if (opcion.equals("--mix")) {
            probarSoloMix();
        } else if (opcion.equals("--todo")) {
            pruebaCompleta();
        } else {
            System.out.println("Opción no válida: " + opcion);
            System.out.println("Ejecutando todo por defecto...\n");
            pruebaCompleta();
        }
    }
    
    /**
     * Prueba básica sin planificadores
     */
    private static void pruebaBasica() {
        System.out.println("=== PRUEBA BÁSICA (Sin planificadores) ===\n");
        
        Circuito simple = Circuito.generarCircuitoSimple();
        probarMetodosBasico(simple);
    }
    
    /**
     * Prueba completa con los tres planificadores
     */
    private static void pruebaCompleta() {
        MonitorSistema monitor = new MonitorSistema("metricas_ejecucion.csv");
        monitor.iniciarArchivo();
        
        System.out.println(MonitorSistema.getInfoSistema());
        
        monitor.capturarMetricas("INICIO");
        System.out.println("=== PRUEBA COMPLETA (Con planificadores) ===\n");
        
        // Crear circuitos de diferentes complejidades
        Circuito simple = Circuito.generarCircuitoSimple();
        Circuito medio = Circuito.generarCircuitoMedio();
        Circuito complejo = Circuito.generarCircuitoComplejo();
        
        // Escenario 1: Circuito simple con los 3 métodos
        System.out.println("\n" + "█".repeat(70));
        System.out.println("  ESCENARIO 1: Circuito Simple - 3 Métodos de Resolución");
        System.out.println("█".repeat(70));
        
        probarPlanificadores(simple);
        
        // Escenario 2: Mix de complejidades
        System.out.println("\n" + "█".repeat(70));
        System.out.println("  ESCENARIO 2: Mix de Complejidades");
        System.out.println("█".repeat(70));
        
        probarPlanificadoresMix(simple, medio, complejo);
        
        // Resumen comparativo
        mostrarResumenFinal();
        monitor.capturarMetricas("FIN");
        monitor.cerrar();
    }
    
    /**
     * Prueba los tres planificadores con un mismo circuito
     */
    private static void probarPlanificadores(Circuito circuito) {
        System.out.println("\nCircuito a resolver: " + circuito);
        System.out.println("Métodos: Gauss-Jordan, Cramer, Librería Numérica\n");
        
        // PLANIFICADOR 1: FCFS
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  PLANIFICADOR 1: FCFS               │");
        System.out.println("└─────────────────────────────────────┘");
        
        Planificador fcfs = new FCFS();
        fcfs.agregarTarea(new GaussJordan(), circuito);
        fcfs.agregarTarea(new Cramer(), circuito);
        fcfs.agregarTarea(new LibreriaNumerica(), circuito);
        fcfs.ejecutar();
        
        // PLANIFICADOR 2: Round Robin
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  PLANIFICADOR 2: Round Robin        │");
        System.out.println("└─────────────────────────────────────┘");
        
        Planificador rr = new RoundRobin(50); // Quantum de 50ms
        rr.agregarTarea(new GaussJordan(), circuito);
        rr.agregarTarea(new Cramer(), circuito);
        rr.agregarTarea(new LibreriaNumerica(), circuito);
        rr.ejecutar();
        
        // PLANIFICADOR 3: SJF
        System.out.println("\n┌─────────────────────────────────────┐");
        System.out.println("│  PLANIFICADOR 3: SJF                │");
        System.out.println("└─────────────────────────────────────┘");
        
        Planificador sjf = new SJF();
        sjf.agregarTarea(new GaussJordan(), circuito);
        sjf.agregarTarea(new Cramer(), circuito);
        sjf.agregarTarea(new LibreriaNumerica(), circuito);
        sjf.ejecutar();
        
        // Comparar resultados
        compararPlanificadores(fcfs, rr, sjf);
    }
    
    /**
     * Prueba con mix de complejidades
     */
    private static void probarPlanificadoresMix(Circuito simple, Circuito medio, Circuito complejo) {
        System.out.println("\nProbando con circuitos variados...\n");
        
        // FCFS
        Planificador fcfs = new FCFS();
        fcfs.agregarTarea(new Cramer(), complejo);        // Lento
        fcfs.agregarTarea(new GaussJordan(), simple);     // Rápido
        fcfs.agregarTarea(new LibreriaNumerica(), medio); // Medio
        fcfs.ejecutar();
        
        // SJF (debería reordenar para ejecutar rápidos primero)
        Planificador sjf = new SJF();
        sjf.agregarTarea(new Cramer(), complejo);        // Lento
        sjf.agregarTarea(new GaussJordan(), simple);     // Rápido
        sjf.agregarTarea(new LibreriaNumerica(), medio); // Medio
        sjf.ejecutar();
        
        compararPlanificadores(fcfs, null, sjf);
    }
    
    /**
     * Compara métricas entre planificadores
     */
    private static void compararPlanificadores(Planificador fcfs, Planificador rr, Planificador sjf) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              COMPARACIÓN DE PLANIFICADORES                 ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        System.out.printf("%-25s %-15s %-15s %-15s\n", 
            "Métrica", "FCFS", "Round Robin", "SJF");
        System.out.println("─".repeat(70));
        
        MetricasPlanificacion m1 = fcfs.getMetricas();
        MetricasPlanificacion m2 = rr != null ? rr.getMetricas() : null;
        MetricasPlanificacion m3 = sjf.getMetricas();
        
        System.out.printf("%-25s %-15d %-15s %-15d ms\n", 
            "Tiempo Total", 
            m1.getTiempoTotal(),
            m2 != null ? m2.getTiempoTotal() + " ms" : "N/A",
            m3.getTiempoTotal());
        
        System.out.printf("%-25s %-15d %-15s %-15d ms\n", 
            "Espera Promedio", 
            m1.getTiempoEsperaPromedio(),
            m2 != null ? m2.getTiempoEsperaPromedio() + " ms" : "N/A",
            m3.getTiempoEsperaPromedio());
        
        System.out.printf("%-25s %-15d %-15s %-15d ms\n", 
            "Respuesta Promedio", 
            m1.getTiempoRespuestaPromedio(),
            m2 != null ? m2.getTiempoRespuestaPromedio() + " ms" : "N/A",
            m3.getTiempoRespuestaPromedio());
        
        System.out.printf("%-25s %-15d %-15s %-15d\n", 
            "Context Switches", 
            m1.getContextSwitches(),
            m2 != null ? String.valueOf(m2.getContextSwitches()) : "N/A",
            m3.getContextSwitches());
        
        System.out.printf("%-25s %-15d %-15s %-15d\n", 
            "Tareas Completadas", 
            m1.getTareasCompletadas(),
            m2 != null ? String.valueOf(m2.getTareasCompletadas()) : "N/A",
            m3.getTareasCompletadas());
        
        System.out.println();
    }
    
    /**
     * Resumen final del análisis
     */
    private static void mostrarResumenFinal() {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                  CONCLUSIONES GENERALES                    ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("✓ FCFS:");
        System.out.println("  - Simple y predecible");
        System.out.println("  - Puede sufrir 'convoy effect' si tarea lenta llega primero");
        System.out.println("  - Sin overhead de context switches");
        
        System.out.println("\n✓ Round Robin:");
        System.out.println("  - Fairness: todas las tareas avanzan");
        System.out.println("  - Overhead por context switches");
        System.out.println("  - Quantum afecta el rendimiento");
        
        System.out.println("\n✓ SJF:");
        System.out.println("  - Minimiza tiempo de espera promedio");
        System.out.println("  - Requiere estimación precisa de tiempos");
        System.out.println("  - Riesgo de inanición en tareas largas");
        
        System.out.println("\n" + "═".repeat(65));
    }
    
    /**
     * Prueba básica de métodos (sin planificadores)
     */
    private static void probarMetodosBasico(Circuito circuito) {
        MetodoResolucion[] metodos = {
            new GaussJordan(),
            new Cramer(),
            new LibreriaNumerica()
        };
        
        for (MetodoResolucion metodo : metodos) {
            metodo.setCircuito(circuito);
            Thread hilo = new Thread(metodo);
            hilo.start();
            
            try {
                hilo.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
            if (metodo.getEstado() == MetodoResolucion.EstadoMetodo.TERMINADO) {
                System.out.println("✓ " + metodo.getNombre() + ": " + 
                    metodo.getTiempoEjecucion() + " ms");
            }
        }
    }

    /**
     * Ejecuta solo un tipo de circuito
     */
    private static void probarSoloCircuito(String tipo) {
        System.out.println("=== PRUEBA INDIVIDUAL ===\n");

        Circuito circuito;

        if (tipo.equals("simple")) {
            circuito = Circuito.generarCircuitoSimple();
        } else if (tipo.equals("medio")) {
            circuito = Circuito.generarCircuitoMedio();
        } else {
            circuito = Circuito.generarCircuitoComplejo();
        }

        System.out.println("=".repeat(70));
        System.out.println("  Probando: " + circuito);
        System.out.println("=".repeat(70));

        probarPlanificadores(circuito);
    }

    /**
     * Ejecuta solo el escenario mix
     */
    private static void probarSoloMix() {
        System.out.println("=== ESCENARIO MIX ===\n");

        Circuito simple = Circuito.generarCircuitoSimple();
        Circuito medio = Circuito.generarCircuitoMedio();
        Circuito complejo = Circuito.generarCircuitoComplejo();

        System.out.println("=".repeat(70));
        System.out.println("  Mix de Complejidades");
        System.out.println("=".repeat(70));

        probarPlanificadoresMix(simple, medio, complejo);
    }
}