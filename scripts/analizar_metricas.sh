#!/bin/bash

# Script para analizar métricas capturadas
# Uso: ./scripts/analizar_metricas.sh <directorio_metricas>

if [ $# -eq 0 ]; then
    echo "Uso: $0 <directorio_metricas>"
    echo "Ejemplo: $0 resultados/metricas_20241111_143000"
    exit 1
fi

DIR=$1

if [ ! -d "$DIR" ]; then
    echo "Error: Directorio no existe: $DIR"
    exit 1
fi

echo "=========================================="
echo "  ANÁLISIS DE MÉTRICAS"
echo "=========================================="
echo "Directorio: $DIR"
echo ""

# Análisis de CPU
if [ -f "$DIR/top_snapshot.txt" ]; then
    echo "--- Uso de CPU ---"
    grep "Cpu(s)" $DIR/top_snapshot.txt | head -5
    echo ""
fi

# Análisis de Memoria
if [ -f "$DIR/memoria.txt" ]; then
    echo "--- Uso de Memoria ---"
    grep "Mem:" $DIR/memoria.txt | head -5
    echo ""
fi

# Context Switches
if [ -f "$DIR/context_switches.txt" ]; then
    echo "--- Context Switches ---"
    FIRST=$(head -n 1 $DIR/context_switches.txt | awk '{print $2}')
    LAST=$(tail -n 1 $DIR/context_switches.txt | awk '{print $2}')
    TOTAL=$((LAST - FIRST))
    echo "Context switches durante ejecución: $TOTAL"
    echo ""
fi

# Procesos Java
if [ -f "$DIR/procesos_java.txt" ]; then
    echo "--- Procesos Java ---"
    wc -l $DIR/procesos_java.txt
    echo ""
fi

echo "Análisis completo. Revisa los archivos en: $DIR"