#!/bin/bash

source .env

echo "Transfering data to the control collection site node."
sshpass -f .password ssh $LOGIN@$NODE04 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE04 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $BUILD_PATH/dirControlCollectionSite.zip $LOGIN@$NODE04:test/HeistToTheMuseum

echo "Decompressing data sent to the control collection site node."
sshpass -f .password ssh $LOGIN@$NODE04 'cd test/HeistToTheMuseum ; unzip -uq dirControlCollectionSite.zip'

echo "Executing program at the server control collection site."
sshpass -f .password ssh $LOGIN@$NODE04 "cd test/HeistToTheMuseum/dirControlCollectionSite ; java serverSide.main.ServerHeistToTheMuseumControlCollectionSite $PORT0 $NODE06 $PORT0 $NODE01 $PORT0 $NODE02 $PORT0"
