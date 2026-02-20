#!/bin/bash

IPR=10.100.1.254 # server LDAP

check_username() {
    # $1 deve essere minuscolo e non presente già in LDAP
    echo "$1" | grep -qxE '[a-z]{2,}' && ldapsearch -x -H "ldap://$IPR" -b ou=People,dc=labammsis -s one "(cn=$1)"
}

get_ldapgroups() {
    ldapsearch -x -LLL -H "ldap://$IPR" -LLL -b ou=Groups,dc=labammsis -s one | grep -i "cn:*" | cut -d':' -f2 | tr -d ' '
}

generate_password() {
    do
        SIZE=$(tr -dc '0-9' </dev/urandom | head -c 2)
    done while [ "$SIZE" -lt 6 ]
    tr -dc 'A-Za-z0-9' </dev/urandom | head -c $SIZE
}

do
    echo "Inserire uno username valido"
    read USERNAME
done while ! (check_username "$USERNAME")

GROUPS=$(mktemp)
get_ldapgroups > "$GROUPS"

do
    echo "Scegliere uno dei seguenti gruppi:"
    cat "$GROUPS"
    read GROUP
done while ! cat "$GROUPS" | grep -qx "$GROUP"
    
GID=$(ldapsearch -x -LLL -H "ldap://$IPR" -LLL -b "cn=${GROUP}ou=Groups,dc=labammsis" -s base "(gidNumber)" | cut -d':' -f2 | tr -d ' ')
UID=$(ldapsearch -x -LLL -H "ldap://$IPR" -LLL -b "ou=People,dc=labammsis" -s one "(uidNumber)"| cut -d':' -f2 | tr -d ' ' | sort -n | tail -n1)
(( UID++ ))

PASSWORD=$(generate_password)

ldapadd -x -D "cn=admin,dc=labammsis" -w "gennaio.marzo" -H ldap://$IPR <<EOF
dn: uid=$USERNAME,ou=People,dc=labammsis
objectClass: top
objectClass: posixAccount
objectClass: shadowAccount
objectClass: inetOrgPerson
givenName: $USERNAME
cn: $USERNAME
sn: $USERNAME
uid: $USERNAME
uidNumber: $UID
gidNumber: $GID
homeDirectory: /tmp
loginShell: /bin/bash
userPassword: {crypt}x
EOF

if [[ $? -ne 0 ]]; then
    echo "Errore nella creazione dell'utente"
    exit 1
fi

ldappasswd -H ldap://$IPR -D "cn=admin,dc=labammsis" -w "gennaio.marzo" "uid=$USERNAME,ou=People,dc=labammsis" -s "$PASSWORD"

echo "=============================================="
echo "      Credenziali per l'accesso"
echo "  Username: $USERNAME"
echo "  Password: $PASSWORD"
echo "=============================================="

rm -f "$GROUPS"


