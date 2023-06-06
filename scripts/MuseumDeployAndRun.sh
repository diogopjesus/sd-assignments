#!/bin/bash

source .env

echo "Transfering data to the museum node."
sshpass -f .password ssh $LOGIN@$NODE06 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE06 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirMuseum.zip $LOGIN@$NODE06:test/HeistToTheMuseum

echo "Decompressing data sent to the museum node."
sshpass -f .password ssh $LOGIN@$NODE06 'cd test/HeistToTheMuseum ; unzip -uq dirMuseum.zip'

echo "Executing program at the museum node."
sshpass -f .password ssh $LOGIN@$NODE06 "cd test/HeistToTheMuseum/dirMuseum ; ./museum_com_d.sh $LOGIN"
