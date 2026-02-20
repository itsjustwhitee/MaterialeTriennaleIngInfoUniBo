#!/bin/bash
FLOATIP=172.22.0.10

ldap_sync() {
    # $1 = REMOTE IP
    (
        ldapsearch -x -H "ldap://localhost" -b "ou=People,dc=labammsis" -s one
        ldapsearch -x -H "ldap://localhost" -b "ou=Groups,dc=labammsis" -s one
    ) |
    grep '^dn: ' | cut -c5- |
    ldapdelete -x -H "ldap://localhost" -D "$BINDDN" -w "$BINDPW"

    (
        ldapsearch -x -H "ldap://$1" -b "ou=People,dc=labammsis" -s one
        ldapsearch -x -H "ldap://$1" -b "ou=Groups,dc=labammsis" -s one
    ) |
    ldapadd -x -H "ldap://localhost" -D "$BINDDN" -w "$BINDPW"
}

MYIP=$(ip a | grep -Eo '172\.22\.0\.1[1-2]')
if [[ "$MYIP" == "172.22.0.11" ]]; then
  OIP="172.22.0.12"
else
  OIP="172.22.0.11"
fi

STATUS=$(snmpget -Oqv -v 1 -c public "$OIP" NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"status\")

if [[ "$STATUS" == "ATTIVO" ]]
then
    # altro server attivo
    ip a | grep -q "$FLOATIP" && ip a del "$FLOATIP/28" dev eth1 > /dev/null 2>&1
    ldap_sync "$OIP"

else
    systemctl status slapd > /dev/null || systemctl start slapd
    systemctl status slapd > /dev/null && ! ( ip a | grep -q "$FLOATIP" ) && ip a add "$FLOATIP/28" dev eth1
fi