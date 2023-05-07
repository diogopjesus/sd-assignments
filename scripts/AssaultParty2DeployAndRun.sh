#!/bin/bash

source .env

echo "Transfering data to the assault party node."
sshpass -f password ssh $LOGIN@$NODE02 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh $LOGIN@$NODE02 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp $BUILD_PATH/dirAssaultParty.zip $LOGIN@$NODE02:test/HeistToTheMuseum

echo "Decompressing data sent to the assault party node."
sshpass -f password ssh $LOGIN@$NODE02 'cd test/HeistToTheMuseum ; unzip -uq dirAssaultParty.zip'

echo "Executing program at the server assault party."
sshpass -f password ssh $LOGIN@$NODE02 "cd test/HeistToTheMuseum/dirAssaultParty ; java serverSide.main.ServerHeistToTheMuseumAssaultParty 1 $PORT2 $NODE06 $PORT2"
