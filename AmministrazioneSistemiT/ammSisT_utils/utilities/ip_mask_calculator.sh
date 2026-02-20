#!/bin/bash

if [ -z "$1" ]; then
  echo "Uso: $0 <IP/CIDR>   (es. 192.168.1.10/24)"
  exit 1
fi

cidr="${1#*/}"

# Verifica CIDR valido
if ! [[ "$cidr" =~ ^[0-9]+$ ]] || [ "$cidr" -lt 0 ] || [ "$cidr" -gt 32 ]; then
  echo "CIDR non valido: $cidr"
  exit 1
fi

# Calcola la maschera a 32 bit
mask=$(( 0xFFFFFFFF << (32 - cidr) & 0xFFFFFFFF ))

# Converti in notazione decimale puntata
o1=$(( (mask >> 24) & 255 ))
o2=$(( (mask >> 16) & 255 ))
o3=$(( (mask >> 8)  & 255 ))
o4=$((  mask        & 255 ))

echo "Maschera decimale: $o1.$o2.$o3.$o4"

