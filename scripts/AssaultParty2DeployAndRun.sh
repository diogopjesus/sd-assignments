#!/bin/bash

source .env

echo "Transfering data to the assault party 2 node."
sshpass -f .password ssh $LOGIN@$NODE03 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE03 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirAssaultParty.zip $LOGIN@$NODE03:test/HeistToTheMuseum

echo "Decompressing data sent to the assault party 2 node."
sshpass -f .password ssh $LOGIN@$NODE03 'cd test/HeistToTheMuseum ; unzip -uq dirAssaultParty.zip'

echo "Executing program at the assault party 2 node."
sshpass -f .password ssh $LOGIN@$NODE03 "cd test/HeistToTheMuseum/dirAssaultParty ; ./asspart_com_d.sh $LOGIN 2"
