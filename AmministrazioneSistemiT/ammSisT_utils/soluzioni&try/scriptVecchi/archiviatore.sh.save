#!/bin/bash
# Lo script deve individuare nel sottoalbero del filesystem passato come parametro tutti i file che rispettano almeno una di queste caratteristiche (ogni punto elenco rappresenta 
# una caratteristica da verificare integralmente):
#	- sono stati modificati o acceduti nell'ultima settimana
#	- hanno un qualsiasi bit speciale settato e non sono di proprietà dell'utente root
#	- sono di tipo text (secondo il comando file), di dimensione inferiore a 100k, e contengono la stringa DOC
# e li archivi in un file di nome backup_DATA,tar.gz
# (DATA sia una stringa che rappresenta l'istante di creazione nel formato AAAAMMGG_HHMM)

if [[ $# != 1 ]]
then
	echo "Error. Usage: $0 SOTTOALBER" >&2
	exit 2
fi

WD=$( pwd )
WEEK=$( date -d "now -7 days" +%s )
ls -lR | while read line
do
	if ! [[ $line =~ ^total ]]
	then
		if [[ $line =~  ^./ ]] # sottopath
		then 
			cd $WD/$line # sarà qualocosa tipo /wd/./path/filtrato/da/ls
		else # singolo file dentro il sottopath
			FILE="$( echo $line | rev | cut -d' ' -f1 | rev )"
			if [[ -f "$FILE" && (  $ ( stat -c %X "$FILE" ) -ge $WEEK ||  $( stat -c %Y "$FILE" ) -ge $WEEK ) ]] # controllo ultimo accesso/modifica negli ultimi 7 giorri
			then
				if [[ $( stat -c %U  "$FILE" ) != "root" && ( -g "$FILE" || -k "$FILE" || -u "$FILE" ) ]] # controllo  proprietà non root e bit speciali settati
				then
					if [[
				fi
			fi
		fi
	fi
done
