#!/bin/bash

# connessioni aperte
tcpdump -n -l 'tcp port 22 and (tcp[tcpflags] & (tcp-syn|tcp-fin|tcp-rst) != 0)' 2>/dev/null | 
while read _ _ IP_SRC DIR IP_DEST OTHER; do 
    logger -p local1.info "$IP_SRC $DIR $IP_DEST"
done

# connessioni chiuse
tcpdump -n -l 'tcp port 22 and (tcp[tcpflags] & (tcp-fin|tcp-rst) != 0)' 2>/dev/null | 
while read _ _ IP_SRC DIR IP_DEST OTHER; do 
    logger -p local1.info "$IP_SRC $DIR $IP_DEST"
done

# connessioni aperte e chiuse tra due ip
tcpdump -n -l 'tcp port 22 and host 10.1.1.1 and host 10.2.2.2 and (tcp[tcpflags] & (tcp-syn|tcp-fin|tcp-rst) != 0)' 2>/dev/null | 
while read _ _ IP_SRC DIR IP_DEST OTHER; do 
    logger -p local1.info "$IP_SRC $DIR $IP_DEST"
done



