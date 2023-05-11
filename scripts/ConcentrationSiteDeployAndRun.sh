#!/bin/bash

source .env

echo "Transfering data to the concentration site node."
sshpass -f .password ssh $LOGIN@$NODE03 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE03 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password scp $BUILD_PATH/dirConcentrationSite.zip $LOGIN@$NODE03:test/HeistToTheMuseum

echo "Decompressing data sent to the concentration site node."
sshpass -f .password ssh $LOGIN@$NODE03 'cd test/HeistToTheMuseum ; unzip -uq dirConcentrationSite.zip'

echo "Executing program at the server concentration site."
sshpass -f .password ssh $LOGIN@$NODE03 "cd test/HeistToTheMuseum/dirConcentrationSite ; java serverSide.main.ServerHeistToTheMuseumConcentrationSite $PORT0 $NODE06 $PORT0 $NODE04 $PORT0 $NODE01 $PORT0 $NODE02 $PORT0 $NODE05 $PORT0"
