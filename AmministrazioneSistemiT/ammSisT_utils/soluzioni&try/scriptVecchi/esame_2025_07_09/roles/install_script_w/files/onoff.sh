#!/bin/bash
FLOATIP=172.22.0.10
IPR=10.99.0.1

if [[ "$*" ~= "USERNAME=" ]]
then
    # no parametri
    FILENAME=$(mktemp /home/login)
    THISIP=$(ip a show eth1 | grep -E "inet 10\.99\.1\.[0-9]{1,3}" | awk '{ print $2 }' | cut -d'/' -f1)

    logger -p local6.info -n $IPR "_CREATE_${THISIP}_${FILENAME}"

    USERNAME=""
    PASSWORD=""

    tail -f "${FILENAME}" | while read HEAD TOKEN TAIL
    do
        if [[ -z "$USERNAME" ]]
        then
            USERNAME="$TOKEN"
        else
            PASSWORD="$TOKEN"
            break
        fi
    done

    sudo mkdir -p /home/${USERNAME}
    chown ${USERNAME}:${USERNAME} /home/${USERNAME}
    chmod 700 /home/${USERNAME}

    rm -f "${FILENAME}"

    at -f "/usr/bin/onoff.sh" now + 72 hours 

    echo "============================================="
    echo "      Credenziali per la connessione"
    echo "          Username: $USERNAME"
    echo "          Password: $PASSWORD"
    echo "============================================="
else
    # parametro username
    USERNAME=$(echo $1 | cut -d '=' -f2)
    logger -p local6.info -n $IPR "_REMOVE_${USERNAME}_"

    while ldapsearch -x -LLL -H "ldap://$IP/" -b "uid=$USERNAME,ou=People,dc=labammsis" -s base | grep "^dn:"
    do
        sleep 1
    done

    sudo rm -rf /home/${USERNAME}
fi

