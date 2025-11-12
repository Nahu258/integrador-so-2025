#!/bin/bash

# Script de compilación para el proyecto de Sistemas Operativos
# Autor: Sistema de Resolución de Circuitos
# Uso: ./compilar.sh

echo "========================================="
echo "  COMPILANDO PROYECTO SO - CIRCUITOS"
echo "========================================="

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Crear directorios si no existen
echo -e "${YELLOW}[1/3]${NC} Verificando estructura de directorios..."
mkdir -p bin
mkdir -p resultados
mkdir -p docs

# Limpiar compilaciones anteriores
echo -e "${YELLOW}[2/3]${NC} Limpiando compilaciones anteriores..."
rm -rf bin/*

# Compilar el proyecto
echo -e "${YELLOW}[3/3]${NC} Compilando código fuente..."

# Encontrar todos los archivos .java
JAVA_FILES=$(find src -name "*.java")

# Compilar
javac -d bin -encoding UTF-8 $JAVA_FILES

# Verificar resultado
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Compilación exitosa${NC}"
    echo ""
    echo "Archivos compilados:"
    find bin -name "*.class" | wc -l
    echo ""
    echo "Para ejecutar: ./scripts/ejecutar.sh"
    exit 0
else
    echo -e "${RED}✗ Error en la compilación${NC}"
    echo "Revisa los mensajes de error arriba"
    exit 1
fi