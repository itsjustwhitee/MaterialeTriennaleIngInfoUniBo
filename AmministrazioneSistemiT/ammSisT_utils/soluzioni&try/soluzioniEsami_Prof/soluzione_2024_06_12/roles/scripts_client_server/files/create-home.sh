#!/bin/bash

FILE_LOG=/var/log/newusers  # File di log da cui leggere i nuovi utenti
SNMP_AGENT=$(hostname -I | egrep -o "10\.(11|22).(11|22)").1  # IP del server SNMP (con estensioni chiavi RSA)

# Legge ogni riga del file /var/log/newusers 
while read MON DAY TIME R USER USERNAME ; do

	# Crea la home dell'utente se non esiste
	if [[ ! -e /home/$USERNAME ]]
	then
		mkdir -p /home/$USERNAME/.ssh
		chown -R $USERNAME:$USERNAME /home/$USERNAME
		chmod -R 700 /home/$USERNAME
	fi

	# Recupera la chiave pubblica dell'utente tramite SNMP
	PUBKEY=$(snmpget -Ovq -v 1 -c public $SNMP_AGENT NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"${USERNAME}_PUB\")

	if [[ $(hostname) =~ C[0-9]+ ]] 
	then
		# if on client
		# Recupera anche la chiave privata via SNMP
		snmpget -Ovq -v 1 -c public $SNMP_AGENT NET-SNMP-EXTEND-MIB::nsExtendOutputFull.\"${USERNAME}_PRIV\" > /home/$USERNAME/.ssh/id_rsa
		echo "$PUBKEY" > /home/$USERNAME/.ssh/id_rsa.pub

		# Imposta i permessi corretti sulle chiavi
		chown $USERNAME:$USERNAME /home/$USERNAME/.ssh/id_rsa*
		chmod 400 /home/$USERNAME/.ssh/id_rsa
		chmod 444 /home/$USERNAME/.ssh/id_rsa.pub

	elif [[ $(hostname) =~ S[0-9]+ ]]
	then
		# if on server
		# Se la chiave pubblica non è già presente in authorized_keys, aggiungila
		grep "$PUBKEY" /home/$USERNAME/.ssh/authorized_keys || echo "$PUBKEY" >> /home/$USERNAME/.ssh/authorized_keys
		# in case it did not exist before
		chown $USERNAME:$USERNAME /home/$USERNAME/.ssh/authorized_keys
		chmod 644 /home/$USERNAME/.ssh/authorized_keys
	fi

done < $FILE_LOG  # Fine ciclo: elaborazione di ogni riga del file di log

