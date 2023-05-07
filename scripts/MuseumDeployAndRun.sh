#!/bin/bash

source .env

echo "Transfering data to the museum node."
sshpass -f password ssh $LOGIN@$NODE05 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh $LOGIN@$NODE05 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp $BUILD_PATH/dirMuseum.zip $LOGIN@$NODE05:test/HeistToTheMuseum

echo "Decompressing data sent to the museum node."
sshpass -f password ssh $LOGIN@$NODE05 'cd test/HeistToTheMuseum ; unzip -uq dirMuseum.zip'

echo "Executing program at the server museum."
sshpass -f password ssh $LOGIN@$NODE05 "cd test/HeistToTheMuseum/dirMuseum ; java serverSide.main.ServerHeistToTheMuseumMuseum $PORT2 $NODE06 $PORT2 $NODE01 $PORT2 $NODE02 $PORT2"
