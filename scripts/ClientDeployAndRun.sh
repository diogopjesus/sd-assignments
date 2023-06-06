#!/bin/bash

source .env

echo "Transfering data to the client node."
sshpass -f .password ssh $LOGIN@$NODE01 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE01 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirClient.zip $LOGIN@$NODE01:test/HeistToTheMuseum

echo "Decompressing data sent to the client node."
sshpass -f .password ssh $LOGIN@$NODE01 'cd test/HeistToTheMuseum ; unzip -uq dirClient.zip'

echo "Executing program at the client node."
sshpass -f .password ssh $LOGIN@$NODE01 'cd test/HeistToTheMuseum/dirClient ; ./client_com_d.sh'
