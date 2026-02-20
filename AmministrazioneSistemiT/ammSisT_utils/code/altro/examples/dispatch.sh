#!/bin/bash

# Funzione per interrogare via SNMP e verificare i requisiti sui server
snmp_check() {
    local ip="$1"
    local utente="$2"
    local comando="$3"

    # Verifica se il server ha il programma eseguibile /usr/local/bin/COMANDO
    # Verifica se esiste il file authorized_keys senza permessi di scrittura da gruppo e altri
    snmpget -v 1 -c public -Ovq "$ip" 'NET-SNMP-EXTEND-MIB::nsExtendOutputFull."comandi"' | grep -q "/$comando"\$ &&
    snmpget -v 1 -c public -Ovq "$ip" 'NET-SNMP-EXTEND-MIB::nsExtendOutputFull."authkeys"' | grep -Eq "^-rw-.--.--.*/home/$utente/.ssh/authorized_keys"
}



# Monitoraggio del file di log
tail -F /var/log/req.log | while IFS=_ read ipc utente comando; do

    # Se sono stati estratti correttamente utente e comando
    if [[ -n "$utente" && -n "$comando" ]]; then

        tmpdir=$(mktemp -d)
        cd "$tmpdir"
        for h in 172.22.22.{193..254}; do
            snmp_check "$h" "$utente" "$comando" && touch "$h" &
        done

        wait

        # Se ci sono server validi, scegli uno casualmente e invia il messaggio syslog
        ips=$(ls -1 | shuf | head -1)

        if [[ -n "$ips" ]]; then
            logger -p local6.warn -n "$ipc" "_$ips_$utente_$comando"
        fi
done
