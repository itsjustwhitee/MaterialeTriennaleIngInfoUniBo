#!/bin/bash

MATRICOLA="0001070812"

# Controlla che ci sia almeno un argomento
if [ $# -lt 1 ]; then
  echo "Usage: $0 file_or_directory [file_or_directory ...]"
  exit 1
fi

# Crea l'archivio 7z con tutti gli argomenti passati (ricorsivo di default)
7z a "${MATRICOLA}.7z" "$@"

# Verifica se il comando è andato a buon fine
if [ $? -eq 0 ]; then
  echo "Archivio creato: ${MATRICOLA}.7z"
else
  echo "Errore durante la creazione dell'archivio"
  exit 2
fi

