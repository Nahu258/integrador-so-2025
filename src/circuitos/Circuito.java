package circuitos;

/**
 * Representa un circuito eléctrico como sistema de ecuaciones lineales
 */
public class Circuito {
    private double[][] coeficientes;
    private double[] terminosIndep;
    private int numMallas;
    private String complejidad;
    private String nombre;
    
    public Circuito(int numMallas, String complejidad, String nombre) {
        this.numMallas = numMallas;
        this.complejidad = complejidad;
        this.nombre = nombre;
        this.coeficientes = new double[numMallas][numMallas];
        this.terminosIndep = new double[numMallas];
    }
    
    /**
     * CIRCUITO SIMPLE: 3 mallas (antes 2)
     */
    public static Circuito generarCircuitoSimple() {
        Circuito c = new Circuito(3, "SIMPLE", "Circuito-3-Mallas");
        
        // Matriz 3x3 con valores más complejos
        c.coeficientes[0][0] = 15.0;
        c.coeficientes[0][1] = -5.0;
        c.coeficientes[0][2] = 0.0;
        
        c.coeficientes[1][0] = -5.0;
        c.coeficientes[1][1] = 20.0;
        c.coeficientes[1][2] = -8.0;
        
        c.coeficientes[2][0] = 0.0;
        c.coeficientes[2][1] = -8.0;
        c.coeficientes[2][2] = 18.0;
        
        c.terminosIndep[0] = 12.0;
        c.terminosIndep[1] = 0.0;
        c.terminosIndep[2] = 15.0;
        
        return c;
    }
    
    /**
     * CIRCUITO MEDIO: 6 mallas (antes 4)
     */
    public static Circuito generarCircuitoMedio() {
        Circuito c = new Circuito(6, "MEDIO", "Circuito-6-Mallas");
        
        // Matriz 6x6 densamente acoplada
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (i == j) {
                    c.coeficientes[i][j] = 30.0 + i * 5;
                } else if (Math.abs(i - j) == 1) {
                    c.coeficientes[i][j] = -10.0;
                } else if (Math.abs(i - j) == 2) {
                    c.coeficientes[i][j] = -3.0;
                } else {
                    c.coeficientes[i][j] = 0.0;
                }
            }
            c.terminosIndep[i] = (i % 2 == 0) ? 20.0 : 0.0;
        }
        
        return c;
    }
    
    /**
     * CIRCUITO COMPLEJO: 9 mallas (antes 6)
     * Este tardará MUCHO más, especialmente con Cramer
     */
    public static Circuito generarCircuitoComplejo() {
        Circuito c = new Circuito(9, "COMPLEJO", "Circuito-9-Mallas");
        
        // Matriz 9x9 totalmente acoplada
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (i == j) {
                    c.coeficientes[i][j] = 50.0 + (i * 8);
                } else if (Math.abs(i - j) <= 2) {
                    c.coeficientes[i][j] = -12.0 / (Math.abs(i - j) + 1);
                } else {
                    c.coeficientes[i][j] = -2.0;
                }
            }
            c.terminosIndep[i] = 25.0 * (i % 3);
        }
        
        return c;
    }
    
    // Getters
    public double[][] getCoeficientes() { return coeficientes; }
    public double[] getTerminosIndep() { return terminosIndep; }
    public int getNumMallas() { return numMallas; }
    public String getComplejidad() { return complejidad; }
    public String getNombre() { return nombre; }
    
    @Override
    public String toString() {
        return String.format("Circuito[%s, %d mallas, %s]", 
            nombre, numMallas, complejidad);
    }
}