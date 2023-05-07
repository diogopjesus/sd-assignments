#!/bin/bash

source .env

echo "Transfering data to the ordinary thieves node."
sshpass -f password ssh $LOGIN@$NODE08 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh $LOGIN@$NODE08 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp $BUILD_PATH/dirOrdinaryThieves.zip $LOGIN@$NODE08:test/HeistToTheMuseum

echo "Decompressing data sent to the ordinary thieves node."
sshpass -f password ssh $LOGIN@$NODE08 'cd test/HeistToTheMuseum ; unzip -uq dirOrdinaryThieves.zip'

echo "Executing program at the ordinary thief node."
sshpass -f password ssh $LOGIN@$NODE08 'cd test/HeistToTheMuseum/dirOrdinaryThieves ; java clientSide.main.ClientHeistToTheMuseumOrdinaryThief $NODE06 $PORT2 $NODE02 $PORT2'
