#!/bin/bash

source .env

echo "Transfering data to the client node."
sshpass -f .password ssh $LOGIN@$NODE07 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE07 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $BUILD_PATH/dirClient.zip $LOGIN@$NODE07:test/HeistToTheMuseum

echo "Decompressing data sent to the client node."
sshpass -f .password ssh $LOGIN@$NODE07 'cd test/HeistToTheMuseum ; unzip -uq dirClient.zip'

echo "Executing program at the client node."
sshpass -f .password ssh $LOGIN@$NODE07 "cd test/HeistToTheMuseum/dirClient ; java clientSide.main.ClientHeistToTheMuseum $NODE01 $PORT0 $NODE02 $PORT0 $NODE03 $PORT0 $NODE04 $PORT0 $NODE05 $PORT0 $NODE06 $PORT0 stat"
