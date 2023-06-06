#!/bin/bash

source .env

echo "Transfering data to the general repository node."
sshpass -f .password ssh $LOGIN@$NODE07 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE07 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirGeneralRepository.zip $LOGIN@$NODE07:test/HeistToTheMuseum

echo "Decompressing data sent to the general repository node."
sshpass -f .password ssh $LOGIN@$NODE07 'cd test/HeistToTheMuseum ; unzip -uq dirGeneralRepository.zip'

echo "Executing program at the general repository node."
sshpass -f .password ssh $LOGIN@$NODE07 "cd test/HeistToTheMuseum/dirGeneralRepository ; ./repos_com_d.sh $LOGIN"

echo "Server shutdown."
sshpass -f .password ssh $LOGIN@$NODE07 'cd test/HeistToTheMuseum/dirGeneralRepository ; less stat'
