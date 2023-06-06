#!/bin/bash

source .env

echo "Transfering data to the concentration site node."
sshpass -f .password ssh $LOGIN@$NODE09 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE09 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirConcentrationSite.zip $LOGIN@$NODE09:test/HeistToTheMuseum

echo "Decompressing data sent to the concentration site node."
sshpass -f .password ssh $LOGIN@$NODE09 'cd test/HeistToTheMuseum ; unzip -uq dirConcentrationSite.zip'

echo "Executing program at the concentration site node."
sshpass -f .password ssh $LOGIN@$NODE09 "cd test/HeistToTheMuseum/dirConcentrationSite ; ./concsite_com_d.sh $LOGIN"
