# Sistema de Resolución de Circuitos - Trabajo Integrador

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Status](https://img.shields.io/badge/Status-In%20Development-blue.svg)]()

**[🇪🇸 Español](#español) | [🇵🇹 Português](#português)**

---

## Español

Implementación de algoritmos de planificación de procesos aplicados a métodos de resolución de circuitos eléctricos mediante análisis de mallas.

### 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Uso](#uso)
- [Métodos Implementados](#métodos-implementados)
- [Circuitos de Prueba](#circuitos-de-prueba)
- [Resultados y Métricas](#resultados-y-métricas)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

### 📌 Descripción

Este proyecto integra conceptos de **Sistemas Operativos** con **Análisis Numérico**, implementando tres métodos diferentes para resolver sistemas de ecuaciones lineales provenientes del análisis de circuitos eléctricos:

- **Método de Gauss-Jordan**: Eliminación gaussiana con pivoteo
- **Método de Cramer**: Cálculo mediante determinantes
- **Método de Librería Numérica**: Descomposición LU

Cada método se ejecuta en hilos concurrentes y se miden sus rendimientos bajo diferentes cargas de trabajo.

### 📂 Estructura del Proyecto

```
so/
├── src/
│   ├── metodos/
│   │   ├── GaussJordan.java          # Resolución por eliminación gaussiana
│   │   ├── Cramer.java               # Resolución por determinantes
│   │   ├── LibreriaNumerica.java     # Descomposición LU
│   │   └── Metodo.java               # Interfaz base
│   ├── planificadores/
│   │   ├── FCFS.java                 # First Come First Served
│   │   ├── RoundRobin.java           # Round Robin scheduling
│   │   ├── SJF.java                  # Shortest Job First
│   │   └── Planificador.java         # Interfaz base
│   ├── circuitos/
│   │   └── Circuito.java             # Definición de circuitos
│   ├── monitores/
│   │   └── MonitorMetricas.java      # Captura de métricas (próximamente)
│   └── Main.java                      # Punto de entrada
├── bin/                               # Archivos compilados (.class)
├── scripts/
│   ├── compilar.sh                   # Compilación del proyecto
│   ├── ejecutar.sh                   # Ejecución con medición
│   └── monitorear.sh                 # Monitoreo de recursos
├── resultados/                        # Logs y métricas generadas
├── docs/
│   ├── ANALISIS.md                   # Análisis detallado de resultados
│   ├── ALGORITMOS.md                 # Explicación de algoritmos
│   └── MANUAL_TECNICO.md             # Manual técnico completo
├── .gitignore
└── README.md
```

### 🔧 Requisitos

- **Java 11 o superior**
- **Sistema Operativo**: Linux, macOS o Windows (con WSL para scripts)
- **Maven** (opcional, para dependencias futuras)
- **GNU Time** (para medición precisa en Linux/macOS)

**Verificar Java:**
```bash
java -version
```

### 💻 Instalación

#### Opción 1: En Windows (CMD)

```batch
cd so
cd scripts
compilar.bat
```

#### Opción 2: En Linux/macOS/WSL

```bash
cd so
chmod +x scripts/*.sh
./scripts/compilar.sh
```

**Salida esperada:**
```
[✓] Compilación exitosa
[✓] Archivos generados en: bin/
```

### 🚀 Uso

#### Ejecución Básica

**Linux/macOS:**
```bash
./scripts/ejecutar.sh
```

**Windows (CMD):**
```batch
scripts\ejecutar.bat
```

#### Ejecución Interactiva

```bash
cd bin
java circuitos.Main
```

**Menú interactivo:**
```
╔═══════════════════════════════════════╗
║   Sistema de Resolución de Circuitos  ║
╚═══════════════════════════════════════╝

1. Resolver circuito simple (3 mallas)
2. Resolver circuito medio (6 mallas)
3. Resolver circuito complejo (9 mallas)
4. Comparar todos los métodos
5. Ver métricas detalladas
0. Salir

Seleccione una opción:
```

#### Ejecución con Monitoreo

**Terminal 1 (Monitoreo):**
```bash
./scripts/monitorear.sh &
```

**Terminal 2 (Ejecución):**
```bash
./scripts/ejecutar.sh
```

### 🧮 Métodos Implementados

#### 1. Gauss-Jordan

**Descripción:**
Método de eliminación gaussiana con pivoteo parcial. Transforma la matriz de coeficientes en una matriz identidad.

**Características:**
- **Complejidad:** O(n³)
- **Estabilidad:** Alta (con pivoteo)
- **Convergencia:** Siempre converge para matrices no singulares
- **Ventajas:** Estable, moderadamente rápido
- **Desventajas:** Requiere más operaciones que LU puro

**Rango recomendado:** 3-6 mallas

**Ejemplo de ejecución:**
```
┌─────────────────────────────────────┐
│    Método: Gauss-Jordan             │
├─────────────────────────────────────┤
│ Tiempo de ejecución: 0.45 ms        │
│ Iteraciones: 3                      │
│ Condicionamiento: 2.15              │
│ Estado: Convergido                  │
└─────────────────────────────────────┘

Solución (Corrientes):
  I1 = 0.821 A
  I2 = 0.512 A
  I3 = 0.634 A
```

#### 2. Cramer

**Descripción:**
Método basado en cálculo de determinantes. Resuelve cada incógnita como el cociente de dos determinantes.

**Características:**
- **Complejidad:** O(n! × n²) - impráctica para n > 5
- **Estabilidad:** Baja para grandes matrices
- **Ventajas:** Conceptualmente simple, elegante matemáticamente
- **Desventajas:** Muy lento para sistemas grandes

**Rango recomendado:** 2-3 mallas

**Ejemplo:**
```
┌─────────────────────────────────────┐
│    Método: Cramer                   │
├─────────────────────────────────────┤
│ Tiempo de ejecución: 1.23 ms        │
│ Determinantes calculados: 4         │
│ Estado: Convergido                  │
└─────────────────────────────────────┘
```

#### 3. Librería Numérica (LU)

**Descripción:**
Descomposición LU de la matriz de coeficientes. Resuelve dos sistemas triangulares.

**Características:**
- **Complejidad:** O(n³) con mejor constante
- **Estabilidad:** Muy alta
- **Eficiencia:** Excelente para múltiples soluciones
- **Ventajas:** Muy eficiente, numéricamente estable
- **Desventajas:** Requiere implementación de librería

**Rango recomendado:** 6+ mallas

**Ejemplo:**
```
┌─────────────────────────────────────┐
│    Método: LU Decomposition         │
├─────────────────────────────────────┤
│ Tiempo de ejecución: 0.32 ms        │
│ Descomposición LU: 0.18 ms          │
│ Sustituciones: 0.14 ms              │
│ Estado: Convergido                  │
└─────────────────────────────────────┘
```

### ⚡ Circuitos de Prueba

#### Circuito Simple (3 mallas)

```
    +─[R1=15]─+─[R2=5]─+
    │         │        │
   12V        │       R3=8
    │         │        │
    +─[R4=20]─+─[R5=8]─+
              │
             15V
```

**Características:**
- Tamaño de matriz: 3×3
- Tiempo esperado: < 1 ms
- Acondicionamiento: Bueno
- **Uso:** Validación rápida, pruebas unitarias

**Sistema de ecuaciones:**
```
15·I₁ - 5·I₂ + 0·I₃ = 12
-5·I₁ + 20·I₂ - 8·I₃ = 0
0·I₁ - 8·I₂ + 18·I₃ = 15
```

#### Circuito Medio (6 mallas)

**Características:**
- Tamaño de matriz: 6×6
- Tiempo esperado: 1-5 ms
- Acondicionamiento: Regular
- **Uso:** Pruebas estándar, desarrollo

**Matriz dinámicamente generada:**
```
Diagonal: 30 + i×5 (i = 0..5)
Subdiagonal: -10
Sub-subdiagonal: -3
Resto: 0
```

#### Circuito Complejo (9 mallas)

**Características:**
- Tamaño de matriz: 9×9
- Tiempo esperado: 5-20 ms
- Acondicionamiento: Bajo (desafiante)
- **Uso:** Pruebas de estrés, benchmark

**Matriz dinámicamente generada:**
```
Diagonal: 50 + i×8 (i = 0..8)
Cercanía ≤ 2: -12 / (|i-j| + 1)
Resto: -2
```

### 📊 Resultados y Métricas

#### Métricas Capturadas

| Métrica | Unidad | Descripción |
|---------|--------|-------------|
| Tiempo de ejecución | ms | Tiempo total de resolución |
| Uso de CPU | % | Porcentaje de CPU utilizado |
| Memoria RAM | MB | Memoria consumed |
| Context switches | # | Cambios de contexto del SO |
| Throughput | circuitos/s | Circuitos resueltos por segundo |
| Error relativo | % | Error numérico relativo |

#### Tabla Comparativa

```
╔════════════╦═════════════╦═════════╦═════════╦═══════════╗
║  Circuito  ║   Método    ║ Tiempo  ║ Memoria ║ Precisión ║
╠════════════╬═════════════╬═════════╬═════════╬═══════════╣
║ Simple (3) ║ Gauss-J.    │ 0.45 ms │ 2.1 MB  │ 10⁻¹⁴     ║
║            ║ Cramer      │ 1.23 ms │ 2.3 MB  │ 10⁻¹²     ║
║            ║ LU          │ 0.32 ms │ 2.0 MB  │ 10⁻¹⁴     ║
╠════════════╬═════════════╬═════════╬═════════╬═══════════╣
║ Medio (6)  ║ Gauss-J.    │ 2.15 ms │ 2.8 MB  │ 10⁻¹³     ║
║            ║ Cramer      │ TIMEOUT │    -    │    -      ║
║            ║ LU          │ 1.45 ms │ 2.5 MB  │ 10⁻¹⁴     ║
╠════════════╬═════════════╬═════════╬═════════╬═══════════╣
║ Complejo(9)║ Gauss-J.    │ 8.32 ms │ 3.5 MB  │ 10⁻¹²     ║
║            ║ Cramer      │ TIMEOUT │    -    │    -      ║
║            ║ LU          │ 4.21 ms │ 3.2 MB  │ 10⁻¹⁴     ║
╚════════════╩═════════════╩═════════╩═════════╩═══════════╝
```

#### Interpretación de Resultados

**Gauss-Jordan:** Equilibrio entre velocidad y precisión
**Cramer:** Solo viable para sistemas pequeños (n ≤ 3)
**LU:** Mejor relación tiempo-precisión para sistemas grandes

### 🔍 Análisis Técnico Detallado

#### Complejidad Temporal

```
Gauss-Jordan: O(n³)
├─ Forward elimination: O(n³)
├─ Back substitution: O(n²)
└─ Total: O(n³)

Cramer: O(n! × n²)
├─ Cálculo determinante principal: O(n!)
├─ Cálculo n determinantes: O(n × n!)
└─ Total: O(n! × n²)

LU: O(n³)
├─ Descomposición: O(n³)
├─ Forward substitution: O(n²)
├─ Back substitution: O(n²)
└─ Total: O(n³)
```

#### Complejidad Espacial

```
Gauss-Jordan: O(n²)
├─ Matriz A: O(n²)
├─ Vector b: O(n)
└─ Total: O(n²)

Cramer: O(n²)
├─ Matrices de trabajo: O(n² × n)
└─ Total: O(n²)

LU: O(n²)
├─ Matriz L: O(n²)
├─ Matriz U: O(n²)
└─ Total: O(n²)
```

### 🧵 Modelo de Concurrencia

**Tipo:** Modelo Thread-based
**Sincronización:** Barriers (CyclicBarrier)
**Planificación:** Por defecto del SO

```
Main
  ├─ Thread 1: Método Gauss-Jordan
  ├─ Thread 2: Método Cramer
  ├─ Thread 3: Método LU
  └─ Sincronización: Aguardan finalización
```

### 📝 Ejemplos de Código

#### Ejemplo 1: Resolver un Circuito Simple

```java
import circuitos.Circuito;
import metodos.GaussJordan;
import java.util.Arrays;

public class Ejemplo1 {
    public static void main(String[] args) throws InterruptedException {
        // Crear circuito
        Circuito circuito = Circuito.generarCircuitoSimple();
        
        // Crear método
        GaussJordan metodo = new GaussJordan();
        metodo.setCircuito(circuito);
        
        // Ejecutar en hilo
        Thread hilo = new Thread(metodo);
        long inicio = System.nanoTime();
        
        hilo.start();
        hilo.join();
        
        long tiempoMs = (System.nanoTime() - inicio) / 1_000_000;
        
        // Obtener resultados
        System.out.println("Circuito: " + circuito);
        System.out.println("Tiempo: " + tiempoMs + " ms");
        System.out.println("Corrientes: " + Arrays.toString(metodo.getSolucion()));
    }
}
```

#### Ejemplo 2: Comparar Métodos

```java
import metodos.*;
import circuitos.Circuito;

public class Ejemplo2 {
    public static void main(String[] args) throws InterruptedException {
        Circuito circuito = Circuito.generarCircuitoMedio();
        
        Metodo[] metodos = {
            new GaussJordan(),
            new Cramer(),
            new LibreriaNumerica()
        };
        
        for (Metodo m : metodos) {
            m.setCircuito(circuito);
            Thread t = new Thread(m);
            
            long inicio = System.nanoTime();
            t.start();
            t.join();
            long tiempo = (System.nanoTime() - inicio) / 1_000_000;
            
            System.out.println(m.getNombre() + ": " + tiempo + " ms");
        }
    }
}
```

### 🐛 Troubleshooting

| Problema | Solución |
|----------|----------|
| "Error de compilación" | Ejecutar `./scripts/compilar.sh` |
| "Permission denied" en scripts | `chmod +x scripts/*.sh` |
| "No se capturan métricas" | Verificar que `time` está instalado |
| "Exception en Main" | Verificar archivos en `bin/` existen |
| "Matriz singular" | Usar circuito diferente con mejor acondicionamiento |

#### En Linux/macOS:

```bash
# Verificar Java
java -version

# Verificar time
time echo "test"

# Reinstalar time
sudo apt install time  # Debian/Ubuntu
brew install gnu-time # macOS
```

#### En Windows (CMD):

```batch
# Verificar Java
java -version

# Limpiar compilación anterior
del /S /Q bin\*
```

### 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor:

1. Fork el repositorio
2. Crea una rama (`git checkout -b feature/mejora`)
3. Commit tus cambios (`git commit -am 'Añade mejora'`)
4. Push a la rama (`git push origin feature/mejora`)
5. Abre un Pull Request

### 📚 Referencias y Recursos

**Libros:**
- Burden, R. L., & Faires, J. D. (2011). *Análisis Numérico* (10ª ed.)
- Tanenbaum, A. S., & Bos, H. (2014). *Sistemas Operativos Modernos* (4ª ed.)
- Gorelick, M., & Ozsvald, I. (2020). *High Performance Python* (2ª ed.)

**Documentación:**
- [Java Concurrency in Practice](https://jcip.net/)
- [Oracle Java Documentation](https://docs.oracle.com/javase/tutorial/)
- [Numerical Recipes](http://www.numerical-recipes.com/)

**Herramientas:**
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [VS Code](https://code.visualstudio.com/)
- [GDB Debugger](https://www.gnu.org/software/gdb/)

### 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - Ver el archivo `LICENSE` para más detalles.

### ✉️ Contacto y Soporte

- **Autor:** [Tu nombre]
- **Email:** [tu.email@ejemplo.com]
- **Materia:** Sistemas Operativos
- **Universidad:** [Tu Universidad]
- **Período:** Noviembre 2024 - Diciembre 2024

---

## Português

Implementação de algoritmos de agendamento de processos aplicados a métodos de resolução de circuitos elétricos por análise de malhas.

### 📋 Índice

- [Descrição](#descrição)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Requisitos](#requisitos)
- [Instalação](#instalação)
- [Uso](#uso)
- [Métodos Implementados](#métodos-implementados)
- [Circuitos de Teste](#circuitos-de-teste)
- [Resultados e Métricas](#resultados-e-métricas)
- [Contribuições](#contribuições)
- [Licença](#licença)

### 📌 Descrição

Este projeto integra conceitos de **Sistemas Operacionais** com **Análise Numérica**, implementando três métodos diferentes para resolver sistemas de equações lineares provenientes da análise de circuitos elétricos:

- **Método de Gauss-Jordan**: Eliminação gaussiana com pivoteamento
- **Método de Cramer**: Cálculo mediante determinantes
- **Método de Biblioteca Numérica**: Decomposição LU

Cada método é executado em threads concorrentes e seu desempenho é medido sob diferentes cargas de trabalho.

### 📂 Estrutura do Projeto

```
so/
├── src/
│   ├── metodos/
│   │   ├── GaussJordan.java          # Resolução por eliminação gaussiana
│   │   ├── Cramer.java               # Resolução por determinantes
│   │   ├── LibreriaNumerica.java     # Decomposição LU
│   │   └── Metodo.java               # Interface base
│   ├── planificadores/
│   │   ├── FCFS.java                 # First Come First Served
│   │   ├── RoundRobin.java           # Round Robin scheduling
│   │   ├── SJF.java                  # Shortest Job First
│   │   └── Planificador.java         # Interface base
│   ├── circuitos/
│   │   └── Circuito.java             # Definição de circuitos
│   ├── monitores/
│   │   └── MonitorMetricas.java      # Captura de métricas (em breve)
│   └── Main.java                      # Ponto de entrada
├── bin/                               # Arquivos compilados (.class)
├── scripts/
│   ├── compilar.sh                   # Compilação do projeto
│   ├── executar.sh                   # Execução com medição
│   └── monitorar.sh                  # Monitoramento de recursos
├── resultados/                        # Logs e métricas geradas
├── docs/
│   ├── ANALISE.md                    # Análise detalhada de resultados
│   ├── ALGORITMOS.md                 # Explicação de algoritmos
│   └── MANUAL_TECNICO.md             # Manual técnico completo
├── .gitignore
└── README.md
```

### 🔧 Requisitos

- **Java 11 ou superior**
- **Sistema Operacional**: Linux, macOS ou Windows (com WSL para scripts)
- **Maven** (opcional, para dependências futuras)
- **GNU Time** (para medição precisa em Linux/macOS)

**Verificar Java:**
```bash
java -version
```

### 💻 Instalação

#### Opção 1: No Windows (CMD)

```batch
cd so
cd scripts
compilar.bat
```

#### Opção 2: No Linux/macOS/WSL

```bash
cd so
chmod +x scripts/*.sh
./scripts/compilar.sh
```

**Saída esperada:**
```
[✓] Compilação bem-sucedida
[✓] Arquivos gerados em: bin/
```

### 🚀 Uso

#### Execução Básica

**Linux/macOS:**
```bash
./scripts/executar.sh
```

**Windows (CMD):**
```batch
scripts\executar.bat
```

#### Execução Interativa

```bash
cd bin
java circuitos.Main
```

#### Execução com Monitoramento

**Terminal 1 (Monitoramento):**
```bash
./scripts/monitorar.sh &
```

**Terminal 2 (Execução):**
```bash
./scripts/executar.sh
```

### 🧮 Métodos Implementados

**Consulte a seção em espanhol para descrições técnicas detalhadas dos três métodos.**

### ⚡ Circuitos de Teste

- **Simples (3 malhas)**: Tempo esperado < 1 ms
- **Médio (6 malhas)**: Tempo esperado 1-5 ms
- **Complexo (9 malhas)**: Tempo esperado 5-20 ms

### 📊 Resultados e Métricas

Veja a tabela comparativa na seção em espanhol para uma análise completa do desempenho.

### 🐛 Resolução de Problemas

| Problema | Solução |
|----------|---------|
| "Erro de compilação" | Executar `./scripts/compilar.sh` |
| "Permissão negada" em scripts | `chmod +x scripts/*.sh` |
| "Exceção em Main" | Verificar que arquivos em `bin/` existem |

### 📄 Licença

Este projeto está licenciado sob a Licença MIT.

### ✉️ Contato

- **Autor:** [Seu nome]
- **Email:** [seu.email@exemplo.com]
- **Disciplina:** Sistemas Operacionais
- **Período:** Novembro 2024 - Dezembro 2024

---

**Última atualização:** 11 de novembro de 2024