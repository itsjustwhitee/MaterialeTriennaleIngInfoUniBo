#!/bin/bash

MATRICOLA="0001070812"

# Controlla che ci sia almeno un argomento
if [ $# -lt 1 ]; then
  echo "Usage: $0 file_or_directory [file_or_directory ...]"
  exit 1
fi

# Crea l'archivio zip con tutti gli argomenti passati (-r per ricorsivo)
zip -r "${MATRICOLA}.zip" "$@"

# Verifica se il comando è andato a buon fine
if [ $? -eq 0 ]; then
  echo "Archivio creato: ${MATRICOLA}.zip"
else
  echo "Errore durante la creazione dell'archivio"
  exit 2
fi

