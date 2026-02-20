#!/bin/bash

# Verifica che sia stato passato un parametro
if [ -z "$1" ]; then
    echo "Errore: è necessario specificare un comando."
    exit 1
fi

# Salva il parametro passato
comando="$1"

# Controlla che il comando non contenga caratteri speciali
if [[ "$comando" =~ [^a-zA-Z0-9_-] ]]; then
    echo "Errore: il nome del comando contiene caratteri speciali non ammessi."
    exit 1
fi

# Recupera l'indirizzo IP del client
ipc=$(hostname -I | grep -Eo '172\.21\.21.[0-9]{3,3}')

# Recupera il nome dell'utente
utente=$(whoami)

# Costruisce il messaggio da inviare via syslog
messaggio="_${ipc}_${utente}_${comando}_"

# Invia il messaggio via syslog al router con il tag local6.info
logger -p local6.info -n 172.21.21.1 "$messaggio"

# Attende 10 secondi prima di verificare il file di log
sleep 10

# Controlla le ultime 20 righe del file /var/log/ans.log
log_file="/var/log/ans.log"
if [ -f "$log_file" ]; then
    # Cerca una riga corrispondente nel formato _IPS_UTENTE_COMANDO_
    ips=$(tail -n 20 "$log_file" | grep -Eo "_172\.22\.22.[0-9]{3,3}_${utente}_${comando}_" | cut -f2 -d_ | head -n 1)

    if [ -n "$ips" ]; then
        # Crea una directory nella home dell'utente con timestamp corrente
        timestamp=$(date +"%Y%m%d%H%M%S")
        nuova_dir="$HOME/$timestamp"

        mkdir -p "$nuova_dir"
        echo "Creata directory: $nuova_dir"

	ssh "$ips" "/usr/local/bin/$comando" > "$nuova_dir/$comando.out" 2> "$nuova_dir/$comando.err"
        if [ $? -eq 0 ]; then
            echo "Comando eseguito con successo su $ips. Output salvato in $nuova_dir/$comando.out e errori in $nuova_dir/$comando.err."
        else
            echo "Errore durante l'esecuzione del comando remoto su $ips. Verificare $nuova_dir/$comando.err."
        fi

    else
        echo "Nessuna corrispondenza trovata nel log."
    fi
else
    echo "Errore: il file di log $log_file non esiste."
fi
