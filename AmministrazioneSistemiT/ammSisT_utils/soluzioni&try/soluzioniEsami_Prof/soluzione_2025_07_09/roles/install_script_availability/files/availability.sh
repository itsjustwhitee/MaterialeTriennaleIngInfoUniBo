#!/bin/bash

FLOATIP="172.22.0.10"
BINDDN="cn=admin,dc=labammsis"
BINDPW="gennaio.marzo"


ldap_sync() {
    # $1 = REMOTE
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

if ip a | grep -Eqw '172\.22\.0\.11' ; then
    REMOTE=172.22.0.12
elif ip a | grep -Eqw '172\.22\.0\.12' ; then
    # paranoid check
    REMOTE=172.22.0.11
else
    echo "Not a server"
    exit 1
fi
                                                                                                                                                                      if snmpget -Oqv -v 1 -c public "$REMOTE" NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"check_status\" | grep -q "ACTIVE" ; then                                                                                                            # l'altro server ha slapd attivo e detiene l'indirizzo floating

    ip a | grep -q "$FLOATIP" && ip a del "$FLOATIP/28" dev eth1
    ldap_sync $REMOTE

else
# l'altro Server non ha slapd attivo (o non risponde affatto), oppure l'altro Server ha slapd attivo ma non detiene l'indirizzo floating

    systemctl status slapd >/dev/null ||
        systemctl start slapd

    systemctl status slapd >/dev/null &&
    ! ip a | grep -q "$FLOATIP" &&
        ip a add "$FLOATIP/28" dev eth1
fi
