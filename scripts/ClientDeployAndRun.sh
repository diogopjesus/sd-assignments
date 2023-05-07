#!/bin/bash

source .env

echo "Transfering data to the client node."
sshpass -f password ssh $LOGIN@$NODE07 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh $LOGIN@$NODE07 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp $BUILD_PATH/dirClient.zip $LOGIN@$NODE07:test/HeistToTheMuseum

echo "Decompressing data sent to the client node."
sshpass -f password ssh $LOGIN@$NODE07 'cd test/HeistToTheMuseum ; unzip -uq dirClient.zip'

echo "Executing program at the client node."
sshpass -f password ssh $LOGIN@$NODE07 "cd test/HeistToTheMuseum/dirClient ; java clientSide.main.ClientHeistToTheMuseum $NODE01 $PORT2 $NODE02 $PORT2 $NODE03 $PORT2 $NODE04 $PORT2 $NODE05 $PORT2 $NODE06 $PORT2 stat"
