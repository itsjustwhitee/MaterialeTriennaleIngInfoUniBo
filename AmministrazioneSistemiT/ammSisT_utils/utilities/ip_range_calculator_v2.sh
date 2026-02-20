#!/bin/bash

if [ -z "$1" ]; then
  echo "Uso: $0 <IP/CIDR> o <IP/Netmask>"
  echo "es. $0 192.168.1.10/24"
  echo "es. $0 192.168.1.10/255.255.255.0"
  exit 1
fi

ip_cidr_or_mask="$1"

# Funzione per convertire IP in numero
ip_to_int() {
  local ip="$1"
  IFS=. read -r o1 o2 o3 o4 <<< "$ip"
  echo "$(( (o1 << 24) + (o2 << 16) + (o3 << 8) + o4 ))"
}

# Funzione per convertire numero in IP
int_to_ip() {
  local ip=$1
  echo "$(( (ip >> 24) & 255 )).$(( (ip >> 16) & 255 )).$(( (ip >> 8) & 255 )).$(( ip & 255 ))"
}

# Estrai l'IP e la parte dopo lo slash
ip="${ip_cidr_or_mask%/*}"
cidr_or_mask="${ip_cidr_or_mask#*/}"

# Controlla se la parte dopo lo slash è un CIDR (solo numeri)
if [[ "$cidr_or_mask" =~ ^[0-9]+$ ]]; then
  # È un CIDR, calcola la netmask
  cidr="$cidr_or_mask"
  mask=$(( 0xFFFFFFFF << (32 - cidr) & 0xFFFFFFFF ))
else
  # È una netmask in formato IP, convertila
  mask=$(ip_to_int "$cidr_or_mask")
fi

# Converti l'IP in numero
ip_int=$(ip_to_int "$ip")

# Calcola network e broadcast
network=$(( ip_int & mask ))
broadcast=$(( network | (~mask & 0xFFFFFFFF) ))

# Calcola range valido per DHCP
dhcp_start=$(( network + 1 ))
dhcp_end=$(( broadcast - 1 ))

echo "Estremo inferiore (network):     $(int_to_ip $network)"
echo "Estremo superiore (broadcast):   $(int_to_ip $broadcast)"
echo "In dnsmasq ricorda di escludere gli indirizzi estremi!"
echo "Range valido per DHCP:"
echo "  Inizio: $(int_to_ip $dhcp_start)"
echo "  Fine:   $(int_to_ip $dhcp_end)"
