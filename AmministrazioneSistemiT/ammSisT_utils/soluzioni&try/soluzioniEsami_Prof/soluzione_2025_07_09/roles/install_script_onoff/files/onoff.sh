#!/bin/bash

ROUIP="10.99.0.1"
FLOATIP="172.22.0.10"
PRIO="local6.info"
MYIP=$(hostname -I | grep -Eo '10\.99\.1\.[0-9]+')

if ! [[ "$*" =~ USERNAME ]] ; then
    # nessun parametro utile, non si può contare solo
    # il numero di parametri perché il processo di login
    # ne può aggiungere alla shell che lancia
    FILENAME=$(mktemp /home/login/onoff.XXXXX)

    logger -n $ROUIP -p $PRIO "_CREATE_${MYIP}_${FILENAME}_"

    #attendo che il router mi scriva in ssh
    while [[ $(wc -l < "$FILENAME") -lt 2 ]]; do
        sleep 1
    done

	#leggo
    NEWUSERNAME=$(head -1 "$FILENAME")
    NEWPASSWORD=$(tail -1 "$FILENAME")

	#home con permessi RICORDA SUDOERS
    sudo mkdir -p /home/$NEWUSERNAME
    sudo chown $NEWUSERNAME:$NEWUSERNAME /home/$NEWUSERNAME
    sudo chmod 700 /home/$NEWUSERNAME

    # rimuovere file temporaneo
    rm -f "$FILENAME"

	#schedulo con at la rimozione
    echo "/usr/bin/onoff.sh USERNAME=$NEWUSERNAME" | at now + 72 hours

    # stampa a video le credenziali
    echo "=============================================="
    echo "           Utente: $NEWUSERNAME"
    echo "           Password: $NEWPASSWORD"
    echo " Questo account sarà rimosso tra 72 ore"
    echo "=============================================="

else
# script lanciato CON parametro → RIMOZIONE ACCOUNT

    NEWUSERNAME=$(echo "$*" | awk -F 'USERNAME=' '{ print $2 }')

    # invio messaggio syslog al router
    logger -n $ROUIP -p $PRIO "_REMOVE_${NEWUSERNAME}_"

	#attendo finchè la ricerca non fallisce
	while ldapsearch -x -H ldap://$FLOATIP -b "uid=$NEWUSERNAME,ou=People,dc=labammsis" -s base | grep -q "^dn:" ; do
	    sleep 1
 	done

    # rimuovo home directory
    sudo rm -rf "/home/$NEWUSERNAME"

fi
