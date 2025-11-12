package metodos;

import circuitos.Circuito;

/**
 * Implementación de la Regla de Cramer
 * Complejidad: O(n! * n²) - Más lento para matrices grandes
 */
public class Cramer implements MetodoResolucion {
    private Circuito circuito;
    private double[] solucion;
    private long tiempoEjecucion;
    private EstadoMetodo estado;
    
    public Cramer() {
        this.estado = EstadoMetodo.LISTO;
    }
    
    @Override
    public String getNombre() {
        return "Cramer";
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
        int n = circuito.getNumMallas();
        double[][] A = circuito.getCoeficientes();
        double[] b = circuito.getTerminosIndep();
        
        // Calcular determinante de A
        double detA = calcularDeterminante(A);
        
        if (Math.abs(detA) < 1e-10) {
            throw new ArithmeticException("Sistema sin solución única (det ≈ 0)");
        }
        
        double[] x = new double[n];
        
        // Para cada incógnita xi
        for (int i = 0; i < n; i++) {
            // Crear matriz Ai reemplazando columna i con vector b
            double[][] Ai = copiarMatriz(A);
            for (int j = 0; j < n; j++) {
                Ai[j][i] = b[j];
            }
            
            // xi = det(Ai) / det(A)
            double detAi = calcularDeterminante(Ai);
            x[i] = detAi / detA;
        }
        
        return x;
    }
    
    /**
     * Calcula el determinante usando expansión por cofactores
     * (recursivo para simplificar, aunque no es el más eficiente)
     */
    private double calcularDeterminante(double[][] matriz) {
        int n = matriz.length;
        
        // Caso base: matriz 1x1
        if (n == 1) {
            return matriz[0][0];
        }
        
        // Caso base: matriz 2x2
        if (n == 2) {
            return matriz[0][0] * matriz[1][1] - matriz[0][1] * matriz[1][0];
        }
        
        // Expansión por primera fila
        double det = 0;
        for (int j = 0; j < n; j++) {
            double[][] submatriz = obtenerSubmatriz(matriz, 0, j);
            double cofactor = Math.pow(-1, j) * matriz[0][j];
            det += cofactor * calcularDeterminante(submatriz);
        }
        
        return det;
    }
    
    /**
     * Obtiene submatriz eliminando fila i y columna j
     */
    private double[][] obtenerSubmatriz(double[][] matriz, int filaElim, int colElim) {
        int n = matriz.length;
        double[][] sub = new double[n - 1][n - 1];
        
        int subI = 0;
        for (int i = 0; i < n; i++) {
            if (i == filaElim) continue;
            
            int subJ = 0;
            for (int j = 0; j < n; j++) {
                if (j == colElim) continue;
                sub[subI][subJ] = matriz[i][j];
                subJ++;
            }
            subI++;
        }
        
        return sub;
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