#!/bin/bash

source .env

echo "Transfering data to the master thief node."
sshpass -f password ssh $LOGIN@$NODE06 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh $LOGIN@$NODE06 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp $BUILD_PATH/dirMasterThief.zip $LOGIN@$NODE06:test/HeistToTheMuseum

echo "Decompressing data sent to the master thief node."
sshpass -f password ssh $LOGIN@$NODE06 'cd test/HeistToTheMuseum ; unzip -uq dirMasterThief.zip'

echo "Executing program at the master thief node."
sshpass -f password ssh $LOGIN@$NODE06 'cd test/HeistToTheMuseum/dirMasterThief ; java clientSide.main.ClientHeistToTheMuseumMasterThief $NODE01 $PORT2 $NODE02 $PORT2'
