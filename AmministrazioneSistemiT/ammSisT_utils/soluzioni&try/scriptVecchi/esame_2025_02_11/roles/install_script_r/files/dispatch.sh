#!/bin/bash

snmp_check()
{
    local IP=$1
    local UTENTE="$2"
    local COMANDO="$3"

    test -n $(snmpget -v 1 -c public -Ovq "$IP" NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"comandi\" | grep -oEq "$COMANDO\$") &&
    test -n $(snmpget -v 1 -c public -Ovq "$IP" NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"chiavi\" | grep -oEq "^-rw*/home/$UTENTE/.ssh/authorized_keys\$") 
}

#IPS_MIN=172.22.22.192
#IPS_MAX=172.22.22.254
IPS=172.22.22.
LAST=192

TEMP=$(mktemp -d)

tail -n0 -f /var/log/req.log | while IFS=_ read HEAD IPC U C TAIL
do

    if ! [[ -n "$C" && -n "$U" ]]
    then
        continue
    fi
    while [[ $LAST -le 254 ]]
    do
        snmp_check "$IPS$LAST" "$U" "$C" && > "$LAST" &
        ((LAST++))
    done
    wait

    IP="$(ls -l ${TEMP} | grep -vEx "total [0-9]+" | shuf | head -n1)"

    if [[ -n "$IP" ]]; then
        logger -p local6.warn -n "$IPC" "_$IP_$U_$C"
    fi

    rm -rf "$TEMP"
done




