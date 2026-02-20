#!/bin/bash

LOGFILE="/var/log/onoff.log"
FLOATIP="172.22.0.10"
BINDDN="cn=admin,dc=labammsis"
BINDPW="gennaio.marzo"
LDAPWRITE="-x -H ldap://$FLOATIP -D $BINDDN -w $BINDPW"
LDAPREAD="-x -H ldap://$FLOATIP"


get_next_id() {
    MAXUID=$(ldapsearch $LDAPREAD -b "ou=People,dc=labammsis" -s one | grep uidNumber | awk '{print $2}' | sort -rn | head -1)

    if [ -z "$MAXUID" ] || [ "$MAXUID" -lt 10000 ] ; then
        echo 10000
    else
        echo $(( MAXUID + 1 ))
    fi
}


generate_password() {
    tr -dc 'A-Za-z0-9' </dev/urandom | head -c 10
}

ldap_user_group_pass() {
    local username="$1"
    local userid="$2"
    local password="$3"

    ldapadd $LDAPWRITE <<EOF
dn: cn=$username,ou=Groups,dc=labammsis
objectClass: top
objectClass: posixGroup
cn: $username
gidNumber: $userid

dn: uid=$username,ou=People,dc=labammsis
objectClass: inetOrgPerson
objectClass: posixAccount
objectClass: shadowAccount
cn: $username
sn: $username
uid: $username
uidNumber: $userid
gidNumber: $userid
homeDirectory: /home/$username
loginShell: /bin/bash
userPassword: {CRYPT}x
EOF

    ldappasswd $LDAPWRITE -s "$password" "uid=$username,ou=People,dc=labammsis"
}


touch "$LOGFILE"
# monitora i messaggi ricevuti dalle WKS
tail -n0 -F "$LOGFILE" | while IFS=_ read HEAD COMMAND FIRST SECOND TAIL ; do

    echo $COMMAND $FIRST $SECOND
    if [ "$COMMAND" = CREATE ] ; then

        IP=$FIRST
        FILENAME=$SECOND
        # echo "[INFO] Ricevuta richiesta CREATE da $IP con $FILENAME"

        nextid=$(get_next_id)
        username="temp${nextid}"
        password=$(generate_password)
        # echo "[INFO] user con $username (uid/gid=$nextid), password=$password"

        ldap_user_group_pass "$username" "$nextid" "$password"

        ssh -o StrictHostKeyChecking=no "$IP" "echo -e '$username\n$password' > $FILENAME"

    elif [ "$COMMAND" = REMOVE ] ; then

        username=$FIRST
        # echo "[INFO] Ricevuta richiesta REMOVE per $username"

        ldapdelete $LDAPWRITE "uid=$username,ou=People,dc=labammsis"
        ldapdelete $LDAPWRITE "cn=$username,ou=Groups,dc=labammsis"

    fi
done
