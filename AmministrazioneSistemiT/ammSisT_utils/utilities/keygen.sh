#!/bin/bash

# Percorso chiave
KEY_PATH="id_rsa"

# Controlla se la chiave esiste già
if [[ -f "$KEY_PATH" ]]; then
    echo "La chiave privata '$KEY_PATH' esiste già. Annullato per sicurezza."
    exit 1
fi

# Crea la directory ~/.ssh se non esiste
mkdir -p ~/.ssh
chmod 700 ~/.ssh

# Genera la coppia di chiavi RSA da 4096 bit, senza passphrase
ssh-keygen -t rsa -b 4096 -f "$KEY_PATH" -N ""

# Imposta permessi raccomandati
chmod 600 "$KEY_PATH"
chmod 644 "${KEY_PATH}.pub"

echo "Coppia di chiavi RSA generata:"
echo "Privata: $KEY_PATH"
echo "Pubblica: ${KEY_PATH}.pub"

