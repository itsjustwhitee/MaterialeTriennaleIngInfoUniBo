#!/bin/bash

# Configurazione LDAP
LDAP_SERVER="ldap://localhost"
BIND_DN="cn=admin,dc=labammsis"
BIND_PW="gennaio.marzo"
BASE_DN="ou=People,dc=labammsis"

# Cerco tutti gli utenti e li processo uno alla volta
# 'objectClass=inetOrgPerson' per individuare i singoli utenti con gli attributi di interesse (cn, sn e mail)
ldapsearch -x -H "$LDAP_SERVER" -D "$BIND_DN" -w "$BIND_PW" -b "$BASE_DN" -LLL "(objectClass=inetOrgPerson)" dn cn sn mail | \
while read -r line
do
    if [[ -z "$line" || "$line" =~ ^# ]]
    then
        continue
    fi

    if [[ "$line" =~ ^dn: ]]
    then
        DN=$(echo "$line" | cut -c5-)
    elif [[ "$line" =~ ^cn: ]]
    then
        CN=$(echo "$line" | cut -c5-)
    elif [[ "$line" =~ ^sn: ]]
    then
        SN=$(echo "$line" | cut -c5-)
    elif [[ "$line" =~ ^mail: ]]
    then
        MAIL=$(echo "$line" | cut -c6-)
        
        # Controllo e chiedo input se un attributo è vuoto
        if [[ -z "$CN" || -z "$SN" || -z "$MAIL" ]]
        then
            echo "Attributi mancanti per l'utente: $DN"
            
            if [[ -z "$CN" ]]
            then
                read -p "Inserire il valore per 'cn': " CN
            fi
            if [[ -z "$SN" ]]
            then
                read -p "Inserire il valore per 'sn': " SN
            fi
            if [[ -z "$MAIL" ]]
            then
                read -p "Inserire il valore per 'mail': " MAIL
            fi
        fi
        GECOS="$CN,$SN,$MAIL"

        # Preparo file LDIF per modifica
        TEMP_LDIF=$(mktemp)
        
        cat << EOF > "$TEMP_LDIF"        
dn: $DN
changetype: modify
replace: gecos
gecos: $GECOS
EOF

        # Eseguo modifica
        ldapmodify -x -H "$LDAP_SERVER" -D "$BIND_DN" -w "$BIND_PW" -f "$TEMP_LDIF"

        # Pulisco e ripristino le variabili per la prossima iterazione (per rilevare il vuoto)
        rm "$TEMP_LDIF"
        DN=""
        CN=""
        SN=""
        MAIL=""
    fi
done
