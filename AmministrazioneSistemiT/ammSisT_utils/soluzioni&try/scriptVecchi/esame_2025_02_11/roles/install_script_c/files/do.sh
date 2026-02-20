#!/bin/bash

if [[ $# -ne 1 || -z $(echo "$1" | grep -Ex [[:alnum:]]) ]]; then
    echo "Usage: $0 <command>"
    echo "  <command>: alphanumeric string"
    exit 1
fi

IPR=172.21.21.1

IPC=$( ip a | grep -oE 'inet 172\.21\.21\.[1-2]{1}[0-9]{1}[0-9]{1}') | awk '{print $2}'
UTENTE="$(whoami)"
COMANDO="$1"

logger -p local6.info -n $IPR "_$IPC_$UTENTE_$COMANDO_"

sleep 10

IPS=""
tail -n20 /var/log/ans.log | while IFS=_ read HEAD IP U C TAIL
do
    if [[ "$UTENTE" == "$U" && "$COMANDO" == "$C" && IP =~ 172\.22\.22\.[1-2]{1}[0-9]{1}[0-9]{1} ]]
    then
        IPS=$IP
        break
    fi
done

if [[ -z $IPS ]]
then
    echo "No matching log entry found. Cannot proceed."
    exit 1
fi

DIR="/home/$UTENTE/$(date +%Y%m%d%H%M%S)"
mkdir -p "$DIR"

ssh -i /home/$UTENTE/.ssh/id_rsa -o StrictHostKeyChecking=no exam@$IPS "bash -c '$COMANDO'" > "$DIR/${COMANDO}.out" 2> "$DIR/${COMANDO}.err"


