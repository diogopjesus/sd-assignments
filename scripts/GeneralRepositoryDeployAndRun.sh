#!/bin/bash

source .env

echo "Transfering data to the general repository node."
sshpass -f password ssh $LOGIN@$NODE01 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh $LOGIN@$NODE01 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp $BUILD_PATH/dirGeneralRepository.zip $LOGIN@$NODE01:test/HeistToTheMuseum

echo "Decompressing data sent to the general repository node."
sshpass -f password ssh $LOGIN@$NODE01 'cd test/HeistToTheMuseum ; unzip -uq dirGeneralRepository.zip'

echo "Executing program at the server general repository."
sshpass -f password ssh $LOGIN@$NODE01 'cd test/HeistToTheMuseum/dirGeneralRepository ; java serverSide.main.ServerHeistToTheMuseumGeneralRepository $PORT2'

echo "Server shutdown."
sshpass -f password ssh $LOGIN@$NODE01 'cd test/HeistToTheMuseum/dirGeneralRepository ; less stat'
