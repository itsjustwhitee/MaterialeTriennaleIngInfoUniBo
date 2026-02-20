#!/bin/bash

if [ -z "$1" ]; then
  echo "Uso: $0 <IP/CIDR>   (es. 192.168.1.10/24)"
  exit 1
fi

ip_cidr="$1"

ip="${ip_cidr%/*}"
cidr="${ip_cidr#*/}"

# Converti IP in numero
IFS=. read -r o1 o2 o3 o4 <<< "$ip"
ip_int=$(( (o1 << 24) + (o2 << 16) + (o3 << 8) + o4 ))

# Calcola netmask a partire dal CIDR
mask=$(( 0xFFFFFFFF << (32 - cidr) & 0xFFFFFFFF ))

# Calcola network e broadcast
network=$(( ip_int & mask ))
broadcast=$(( network | (~mask & 0xFFFFFFFF) ))

# Funzione per convertire numero in IP
int_to_ip() {
  local ip=$1
  echo "$(( (ip >> 24) & 255 )).$(( (ip >> 16) & 255 )).$(( (ip >> 8) & 255 )).$(( ip & 255 ))"
}

# Calcola range valido per DHCP
dhcp_start=$(( network + 1 ))
dhcp_end=$(( broadcast - 1 ))

echo "Estremo inferiore (network):     $(int_to_ip $network)"
echo "Estremo superiore (broadcast):   $(int_to_ip $broadcast)"
echo "In dnsmasq ricorda di escludere gli indirizzi estremi!"
echo "Range valido per DHCP:"
echo "  Inizio: $(int_to_ip $dhcp_start)"
echo "  Fine:   $(int_to_ip $dhcp_end)"

