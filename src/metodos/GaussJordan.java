package metodos;

import circuitos.Circuito;

/**
 * Implementación del método de Gauss-Jordan
 * Complejidad: O(n³)
 */
public class GaussJordan implements MetodoResolucion {
    private Circuito circuito;
    private double[] solucion;
    private long tiempoEjecucion;
    private EstadoMetodo estado;
    
    public GaussJordan() {
        this.estado = EstadoMetodo.LISTO;
    }
    
    @Override
    public String getNombre() {
        return "Gauss-Jordan";
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
            tiempoEjecucion = (System.nanoTime() - inicio) / 1_000_000; // Convertir a ms
            estado = EstadoMetodo.TERMINADO;
            System.out.println("[" + getNombre() + "] Completado en " + tiempoEjecucion + " ms");
        } catch (Exception e) {
            estado = EstadoMetodo.ERROR;
            System.err.println("[" + getNombre() + "] Error: " + e.getMessage());
        }
    }
    
    @Override
    public double[] resolver(Circuito circuito) {
        int n = circuito.getNumMallas();
        
        // Copiar matrices para no modificar originales
        double[][] A = copiarMatriz(circuito.getCoeficientes());
        double[] b = circuito.getTerminosIndep().clone();
        
        // Eliminación hacia adelante con pivoteo parcial
        for (int i = 0; i < n; i++) {
            // Encontrar pivote máximo
            int maxRow = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > Math.abs(A[maxRow][i])) {
                    maxRow = k;
                }
            }
            
            // Intercambiar filas si es necesario
            if (maxRow != i) {
                intercambiarFilas(A, b, i, maxRow);
            }
            
            // Verificar singularidad
            if (Math.abs(A[i][i]) < 1e-10) {
                throw new ArithmeticException("Matriz singular o casi singular");
            }
            
            // Normalizar fila pivote
            double pivote = A[i][i];
            for (int j = i; j < n; j++) {
                A[i][j] /= pivote;
            }
            b[i] /= pivote;
            
            // Eliminar columna en otras filas
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = A[k][i];
                    for (int j = i; j < n; j++) {
                        A[k][j] -= factor * A[i][j];
                    }
                    b[k] -= factor * b[i];
                }
            }
        }
        
        return b; // La matriz está en forma reducida, b contiene la solución
    }
    
    private double[][] copiarMatriz(double[][] original) {
        int n = original.length;
        double[][] copia = new double[n][n];
        for (int i = 0; i < n; i++) {
            copia[i] = original[i].clone();
        }
        return copia;
    }
    
    private void intercambiarFilas(double[][] A, double[] b, int i, int j) {
        double[] tempRow = A[i];
        A[i] = A[j];
        A[j] = tempRow;
        
        double tempB = b[i];
        b[i] = b[j];
        b[j] = tempB;
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