#!/bin/bash

source .env

echo "Transfering data to the assault party node."
sshpass -f .password ssh $LOGIN@$NODE01 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE01 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $BUILD_PATH/dirAssaultParty.zip $LOGIN@$NODE01:test/HeistToTheMuseum

echo "Decompressing data sent to the assault party node."
sshpass -f .password ssh $LOGIN@$NODE01 'cd test/HeistToTheMuseum ; unzip -uq dirAssaultParty.zip'

echo "Executing program at the server assault party."
sshpass -f .password ssh $LOGIN@$NODE01 "cd test/HeistToTheMuseum/dirAssaultParty ; java serverSide.main.ServerHeistToTheMuseumAssaultParty 0 $PORT0 $NODE06 $PORT0"
