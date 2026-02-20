#!/bin/bash

systemctl status slapd && ip a | grep -Eqw '172\.22\.0\.10' && echo "ATTIVO"