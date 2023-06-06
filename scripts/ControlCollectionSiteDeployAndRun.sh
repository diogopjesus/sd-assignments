#!/bin/bash

source .env

echo "Transfering data to the control collection site node."
sshpass -f .password ssh $LOGIN@$NODE05 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE05 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $EXPORT_PATH/dirControlCollectionSite.zip $LOGIN@$NODE05:test/HeistToTheMuseum

echo "Decompressing data sent to the control collection site node."
sshpass -f .password ssh $LOGIN@$NODE05 'cd test/HeistToTheMuseum ; unzip -uq dirControlCollectionSite.zip'

echo "Executing program at the control collection site node."
sshpass -f .password ssh $LOGIN@$NODE05 "cd test/HeistToTheMuseum/dirControlCollectionSite ; ./contcolsite_com_d.sh $LOGIN"
