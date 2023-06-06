#!/bin/bash

source .env

echo "Transfering data to the RMIregistry node."
sshpass -f .password ssh $LOGIN@$NODE08 'mkdir -p test/HeistToTheMuseum'
sshpass -f .password ssh $LOGIN@$NODE08 'rm -rf test/HeistToTheMuseum/*'
sshpass -f .password ssh $LOGIN@$NODE08 'mkdir -p Public/classes/interfaces'
sshpass -f .password ssh $LOGIN@$NODE08 'rm -rf Public/classes/interfaces/*'
sshpass -f .password scp $EXPORT_PATH/dirRMIRegistry.zip $LOGIN@$NODE08:test/HeistToTheMuseum

echo "Decompressing data sent to the RMIregistry node."
sshpass -f .password ssh $LOGIN@$NODE08 'cd test/HeistToTheMuseum ; unzip -uq dirRMIRegistry.zip'
sshpass -f .password ssh $LOGIN@$NODE08 "cd test/HeistToTheMuseum/dirRMIRegistry ; cp interfaces/*.class /home/$LOGIN/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/$LOGIN"

echo "Executing program at the RMIregistry node."
sshpass -f .password ssh $LOGIN@$NODE08 "./set_rmiregistry_d.sh $LOGIN 22180"
