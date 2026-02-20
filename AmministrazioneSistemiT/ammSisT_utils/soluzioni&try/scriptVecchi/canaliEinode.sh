#!/bin/bash

outfile="output"

# apriamo una volta il file in append su fd 3
exec 3>>"$outfile"

# funzione che restituisce inode del file su disco
inode_of() { stat -c %i "$1" 2>/dev/null; }

# funzione che restituisce inode del fd aperto (qui fd 3)
inode_fd3() { ls -l /proc/$$/fd/3 2>/dev/null | awk '{print $10}' | xargs stat -c %i 2>/dev/null; }

while true; do
    sleep 1
    # scrive su fd3, non su stdout
    dd if=/dev/zero bs=1k count=$(( $(echo $RANDOM | rev | cut -c1) + 1 )) >&3

    # confronto inode
    inode_current=$(inode_of "$outfile")
    inode_open=$(inode_fd3)

    if [ "$inode_current" != "$inode_open" ]; then
        echo "Riapro $outfile (inode cambiato)" >&2
        # chiudo fd3 e lo riapro
        exec 3>&-
        exec 3>>"$outfile"
    fi
done

