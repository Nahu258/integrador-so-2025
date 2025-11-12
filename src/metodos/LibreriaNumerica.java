package metodos;

import circuitos.Circuito;

/**
 * Wrapper para usar librería numérica externa
 * Por ahora usa implementación propia (LU decomposition)
 */
public class LibreriaNumerica implements MetodoResolucion {
    private Circuito circuito;
    private double[] solucion;
    private long tiempoEjecucion;
    private EstadoMetodo estado;
    
    public LibreriaNumerica() {
        this.estado = EstadoMetodo.LISTO;
    }
    
    @Override
    public String getNombre() {
        return "Librería-Numérica";
    }
    
    @Override
    public void setCircuito(Circuito circuito) {
        this.circuito = circuito;
        this.estado = EstadoMetodo.LISTO;
    }
    
    @Override
    public void run() {
        if (circuito == null) {
            System.err.println("[" + getNombre() + "] Error: Circuito no establecido");
            estado = EstadoMetodo.ERROR;
            return;
        }
        
        System.out.println("[" + getNombre() + "] Iniciando resolución de: " + circuito.getNombre());
        estado = EstadoMetodo.EJECUTANDO;
        
        long inicio = System.nanoTime();
        try {
            solucion = resolver(circuito);
            tiempoEjecucion = (System.nanoTime() - inicio) / 1_000_000;
            estado = EstadoMetodo.TERMINADO;
            System.out.println("[" + getNombre() + "] Completado en " + tiempoEjecucion + " ms");
        } catch (Exception e) {
            estado = EstadoMetodo.ERROR;
            System.err.println("[" + getNombre() + "] Error: " + e.getMessage());
        }
    }
    
    @Override
    public double[] resolver(Circuito circuito) {
        return resolverConLU(circuito);
    }
    
    /**
     * Resuelve usando descomposición LU (Lower-Upper)
     * Más eficiente que Gauss-Jordan: O(n³) pero con mejor constante
     */
    private double[] resolverConLU(Circuito circuito) {
        int n = circuito.getNumMallas();
        double[][] A = copiarMatriz(circuito.getCoeficientes());
        double[] b = circuito.getTerminosIndep().clone();
        
        // Descomposición LU: A = L * U
        double[][] L = new double[n][n];
        double[][] U = new double[n][n];
        
        // Inicializar L como identidad
        for (int i = 0; i < n; i++) {
            L[i][i] = 1.0;
        }
        
        // Calcular L y U
        for (int i = 0; i < n; i++) {
            // Calcular U
            for (int k = i; k < n; k++) {
                double sum = 0;
                for (int j = 0; j < i; j++) {
                    sum += L[i][j] * U[j][k];
                }
                U[i][k] = A[i][k] - sum;
            }
            
            // Calcular L
            for (int k = i + 1; k < n; k++) {
                double sum = 0;
                for (int j = 0; j < i; j++) {
                    sum += L[k][j] * U[j][i];
                }
                L[k][i] = (A[k][i] - sum) / U[i][i];
            }
        }
        
        // Resolver L*y = b (sustitución hacia adelante)
        double[] y = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L[i][j] * y[j];
            }
            y[i] = b[i] - sum;
        }
        
        // Resolver U*x = y (sustitución hacia atrás)
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < n; j++) {
                sum += U[i][j] * x[j];
            }
            x[i] = (y[i] - sum) / U[i][i];
        }
        
        return x;
    }
    
    private double[][] copiarMatriz(double[][] original) {
        int n = original.length;
        double[][] copia = new double[n][n];
        for (int i = 0; i < n; i++) {
            copia[i] = original[i].clone();
        }
        return copia;
    }
    
    @Override
    public long getTiempoEjecucion() {
        return tiempoEjecucion;
    }
    
    @Override
    public double[] getSolucion() {
        return solucion;
    }
    
    @Override
    public EstadoMetodo getEstado() {
        return estado;
    }
}