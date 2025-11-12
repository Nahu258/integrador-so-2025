#!/bin/bash

# Script de ejecución con monitoreo completo en Linux
# Uso: ./scripts/ejecutar_con_monitoreo.sh

echo "=========================================="
echo "  EJECUCIÓN CON MONITOREO DE SISTEMA"
echo "=========================================="

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m'

# Timestamp
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
OUTPUT_DIR="resultados/metricas_${TIMESTAMP}"
mkdir -p $OUTPUT_DIR

echo -e "${BLUE}Directorio de salida: $OUTPUT_DIR${NC}\n"

# Compilar si es necesario
if [ ! -d "bin" ] || [ -z "$(ls -A bin)" ]; then
    echo -e "${YELLOW}Compilando proyecto...${NC}"
    ./scripts/compilar.sh
fi

# Función para capturar métricas del sistema
capturar_metricas_sistema() {
    while true; do
        # CPU y memoria
        top -b -n 1 | head -n 20 >> $OUTPUT_DIR/top_snapshot.txt
        echo "---" >> $OUTPUT_DIR/top_snapshot.txt
        
        # Procesos Java
        ps aux | grep java | grep -v grep >> $OUTPUT_DIR/procesos_java.txt
        
        # Memoria
        free -m >> $OUTPUT_DIR/memoria.txt
        echo "---" >> $OUTPUT_DIR/memoria.txt
        
        # Context switches
        grep ctxt /proc/stat >> $OUTPUT_DIR/context_switches.txt
        
        sleep 2
    done
}

# Iniciar monitoreo en background
echo -e "${YELLOW}Iniciando monitoreo del sistema...${NC}"
capturar_metricas_sistema &
MONITOR_PID=$!

# Ejecutar aplicación con medición de tiempo
echo -e "${GREEN}Ejecutando aplicación...${NC}\n"
/usr/bin/time -v java -cp bin Main --todo 2>&1 | tee $OUTPUT_DIR/ejecucion.log

# Detener monitoreo
kill $MONITOR_PID 2>/dev/null

# Generar resumen
echo -e "\n${GREEN}Generando resumen...${NC}"

echo "=== RESUMEN DE EJECUCIÓN ===" > $OUTPUT_DIR/resumen.txt
echo "Fecha: $(date)" >> $OUTPUT_DIR/resumen.txt
echo "" >> $OUTPUT_DIR/resumen.txt

echo "Archivos generados:" >> $OUTPUT_DIR/resumen.txt
ls -lh $OUTPUT_DIR >> $OUTPUT_DIR/resumen.txt

echo -e "\n${GREEN}✓ Monitoreo completado${NC}"
echo "Resultados en: $OUTPUT_DIR"
echo ""
echo "Archivos generados:"
ls -1 $OUTPUT_DIR