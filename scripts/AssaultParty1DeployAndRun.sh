#!/bin/bash

source .env

echo "Transfering data to the assault party 1 node."
sshpass -f .password ssh $LOGIN@$NODE02 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE02 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirAssaultParty.zip $LOGIN@$NODE02:test/HeistToTheMuseum

echo "Decompressing data sent to the assault party 1 node."
sshpass -f .password ssh $LOGIN@$NODE02 'cd test/HeistToTheMuseum ; unzip -uq dirAssaultParty.zip'

echo "Executing program at the assault party 1 node."
sshpass -f .password ssh $LOGIN@$NODE02 "cd test/HeistToTheMuseum/dirAssaultParty ; ./asspart_com_d.sh $LOGIN 1"
