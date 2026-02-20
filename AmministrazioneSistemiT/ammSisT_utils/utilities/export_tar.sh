#!/bin/bash

MATRICOLA="0001070812"

# Controlla che ci sia almeno un argomento
if [ $# -lt 1 ]; then
  echo "Usage: $0 file_or_directory [file_or_directory ...]"
  exit 1
fi

# Crea l'archivio tar.gz con tutti gli argomenti passati
tar czf "${MATRICOLA}.tar.gz" "$@"

# Verifica se il comando è andato a buon fine
if [ $? -eq 0 ]; then
  echo "Archivio creato: ${MATRICOLA}.tar.gz"
else
  echo "Errore durante la creazione dell'archivio"
  exit 2
fi

