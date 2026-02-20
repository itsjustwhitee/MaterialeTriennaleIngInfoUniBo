#!/bin/bash

check_agent()
{
    if [[ $(snmpget -v 1 -c public $IPA NET-SNMP-EXTEND-MIB::nsExtendOutputFull./"getCarico/") -gt $SOGLIA ]]
    then
        echo "Attenzione: il server $IPA ha un carico di lavoro superiore a $SOGLIA"
        logger -p local5.info -n $IPA "STOP"
    fi
}


BASE=10.100.
SOGLIA=0.8

for H in {0..7}
do
    for I in {1..255}
    do
        if [ H -eq 7 && I -eq 255 ]
        then
            break 2
        fi

        IPA="${BASE}${H}.${I}"
        check_agent $IPA &
    done
done