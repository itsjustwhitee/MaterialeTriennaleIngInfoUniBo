#!/bin/bash

# Controlla se l'argomento (file) esiste ed è leggibile
if ! [[ -r "$1" ]]
then
	echo "missing argument or unreadable file"
	exit 1
fi

FILE=$1

PATH_KEYS=/home/vagrant/keys

# Crea la directory delle chiavi se non esiste
if [[ ! -e $PATH_KEYS ]]
then
	mkdir $PATH_KEYS
fi

# Elabora ogni riga valida del file (formato: USERNAME;UID;PASSWORD)
egrep '^[^#][^;]+;[^;]+;[^;]+$' $FILE | while IFS=\; read USERNAME USERID PASSWORD ; do
	# echo $USERNAME $USERID $PASSWORD

	# insert user
	# Aggiunge un nuovo utente LDAP sotto ou=People
    ldapadd -x -H ldapi:/// -D "cn=admin,dc=labammsis" -w "gennaio.marzo" <<LDIF
dn: uid=$USERNAME,ou=People,dc=labammsis
objectClass: top
objectClass: posixAccount
objectClass: shadowAccount
objectClass: inetOrgPerson
givenName: $USERNAME
cn: $USERNAME
sn: $USERNAME
mail: $USERNAME@$USERNAME.com
uid: $USERNAME
uidNumber: $USERID
gidNumber: $USERID
homeDirectory: /home/$USERNAME
loginShell: /bin/bash
gecos:$USERNAME
userPassword: {crypt}x
LDIF

	# insert group
	# Crea un gruppo LDAP associato all'utente
	ldapadd -x -H ldap:/// -D "cn=admin,dc=labammsis" -w "gennaio.marzo" <<LDIF
dn: cn=$USERNAME,ou=Groups,dc=labammsis
objectClass: top
objectClass: posixGroup
cn: $USERNAME
gidNumber: $USERID
LDIF

	# Imposta la password dell'utente
	ldappasswd -D "cn=admin,dc=labammsis" -w "gennaio.marzo" "uid=$USERNAME,ou=People,dc=labammsis" -s "$PASSWORD"

	# key (first remove if already present)
	# Rimuove eventuali chiavi RSA già esistenti
	rm  -f "${PATH_KEYS}/$USERNAME" "${PATH_KEYS}/$USERNAME.pub"

	# Genera nuova coppia di chiavi RSA (senza passphrase)
	ssh-keygen -t rsa -f ${PATH_KEYS}/$USERNAME -N ''

	# syslog
	# Invia 21 messaggi syslog ai server (tutti i server) 10.11.11.X e 10.22.22.X
	for LAST in {100..120} ; do
		logger -p local1.info -n 10.11.11.$LAST "$USERNAME"
		logger -p local1.info -n 10.22.22.$LAST "$USERNAME"
	done

	# add command to snmpd (estensioni)
	# Configura l'agent SNMP per mostrare le chiavi RSA dell'utente come MIB "extend"
	echo "extend-sh ${USERNAME}_PUB /usr/bin/sudo -u vagrant /usr/bin/cat ${PATH_KEYS}/${USERNAME}.pub" | sudo /usr/bin/tee -a /etc/snmp/snmpd.conf > /dev/null
	echo "extend-sh ${USERNAME}_PRIV /usr/bin/sudo -u vagrant /usr/bin/cat ${PATH_KEYS}/$USERNAME" | sudo /usr/bin/tee -a /etc/snmp/snmpd.conf > /dev/null

done

# restart snmpd once for all
# Riavvia il demone SNMP per applicare le estensioni configurate
sudo /usr/bin/systemctl restart snmpd.service

