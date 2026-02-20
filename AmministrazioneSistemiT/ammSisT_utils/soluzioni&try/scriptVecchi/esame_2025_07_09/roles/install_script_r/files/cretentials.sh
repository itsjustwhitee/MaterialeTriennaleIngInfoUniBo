#!/bin/bash
FLOATIP=172.22.0.10

getNextId()
{
    local UID=$(ldapsearch -x -LLL -H "ldap://$FLOATIP/" -b "ou=People,dc=labammsis" -s one "uidNumber" | awk -F 'uidNumber: ' '{ print $2 }' | sort -nr | head -1)

    if [[ $UID -lt 10000 ]]
    then
        echo 10000
    else
        echo $((UID + 1))
    fi
}

generate_password() {
    tr -dc 'A-Za-z0-9' </dev/urandom | head -c 10
}

create_new_user()
{
    local ID=$1
    USERNAME="temp${ID}"

    ldapadd -x -D "cn=admin,dc=labammsis" -w "gennaio.marzo" -H "ldap://$FLOATIP" <<EOF
dn: cn=$USERNAME,ou=Groups,dc=labammsis
objectClass: top
objectClass: posixGroup
cn: $USERNAME
gidNumber: $ID

dn: uid=$USERNAME,ou=People,dc=labammsis
objectClass: top
objectClass: inetOrgPerson
objectClass: posixAccount
objectClass: shadowAccount
givenName: $USERNAME
cn: $USERNAME
sn: $USERNAME
uid: $USERNAME
uidNumber: $ID
gidNumber: $ID
homeDirectory: /home/$USERNAME
loginShell: /bin/bash
gecos: Commento per l'utente $USERNAME
userPassword: {CRYPT}x
EOF
    PASSWORD=$(generate_password)
    ldappasswd -x -H ldap://$FLOATIP -D $BINDDN -w $BINDPW -s "$password" "uid=$USERNAME,ou=People,dc=labammsis"
}

tail -n0 -F /var/log/onoff.log | while read HEAD ACT FIRST SEC TAIL
do
    if [[ "$ACT" == "CREATE" ]]
    then
        IP=$FIRST
        FILENAME=$SEC
        ID=$(getNextId)

        create_new_user $ID

        ssh -o StrictHostKeyChecking=no ${IP} "printf $USERNAME\n$PASSWORD > $FILENAME"

    else
        USERNAME="$FIRST"
        ldapdelete -x -H ldap://$FLOATIP -D "cn=admin,dc=labammsis" -w "gennaio.marzo" "uid=$USERNAME,ou=People,dc=labammsis"
        ldapdelete -x -H ldap://$FLOATIP -D "cn=admin,dc=labammsis" -w "gennaio.marzo" "uid=$USERNAME,ou=Groups,dc=labammsis"
    fi
   
done