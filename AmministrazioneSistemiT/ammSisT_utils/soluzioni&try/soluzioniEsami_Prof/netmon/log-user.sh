#!/bin/bash

# configurazione lato client:
# /etc/snmp/snmpd.conf:
# 1) abilitare ricezione dall'esterno
# agentAddress  udp:161
#  
# 2) ampliare la view
# view   all      included        .1
#
# 3) attivare le community
# rocommunity public  default    -V all
# rwcommunity supercom  default    -V all
# 
# 4) programmare le estensioni
# extend    netmon  /bin/sudo /bin/ss -ntp
# extend    users   /bin/ps -axho pid,user
# 
# 5) abilitare snmpd a usare sudo ss
# /etc/sudoers
# Debian-snmp ALL=NOPASSWD: /bin/ss -ntp

# output di snmpget

# NET-SNMP-EXTEND-MIB::nsExtendOutputFull."netmon" = STRING: State    Recv-Q    Send-Q        Local Address:Port       Peer Address:Port
# ESTAB    0         0            192.168.56.201:22         192.168.56.1:49450     users:((\"sshd\",pid=745,fd=3),(\"sshd\",pid=720,fd=3))

PROCNUM=$(snmpget -v 1 -c public $1 NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"netmon\" | egrep "[^0-9]+$1:$3[^0-9]+$2:$4[^0-9]+" | awk -F 'pid=' '{ print $2 }' | cut -f1 -d,)

GUILTY=$(snmpget -v 1 -c public $1 NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"users\" | egrep -w $PROCNUM | awk '{ print $2 }')
logger -p local2.info "$GUILTY"

COUNTER=$(ldapsearch -x -H ldap://localhost -b "uid=$GUILTY,ou=People,dc=labammsis" -s base | egrep '^description: ' | cut -f2 -d' ')

( 

  echo "dn: uid=$GUILTY,ou=People,dc=labammsis"
  echo "changetype: modify"

  # nota: in questo caso si potrebbe semplificare perché
  # replace funziona come add se l'attributo è assente
  # ma per completare l'esempio mettiamo la distinzione

  if test -z "$COUNTER" ; then 
	COUNTER=1
	echo "add: description"
  else 
	((COUNTER++))
	echo "replace: description"
  fi
  echo "description: $COUNTER"

) | ldapmodify -x -H ldap://localhost -D "cn=admin,dc=labammsis" -w gennaio.marzo 


