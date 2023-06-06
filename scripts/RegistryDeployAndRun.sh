#!/bin/bash

source .env

echo "Transfering data to the registry node."
sshpass -f .password ssh $LOGIN@$NODE08 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password scp $EXPORT_PATH/dirRegistry.zip $LOGIN@$NODE08:test/HeistToTheMuseum

echo "Decompressing data sent to the registry node."
sshpass -f .password ssh $LOGIN@$NODE08 'cd test/HeistToTheMuseum ; unzip -uq dirRegistry.zip'

echo "Executing program at the registry node."
sshpass -f .password ssh $LOGIN@$NODE08 "cd test/HeistToTheMuseum/dirRegistry ; ./registry_com_d.sh $LOGIN"
