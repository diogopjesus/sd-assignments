#!/bin/bash

source .env

echo "Executing the ap2 node."

cd  $TEST_PATH/dirAssaultParty

java serverSide.main.ServerHeistToTheMuseumAssaultParty 1 22312 127.0.0.1 22310

echo "AP2 Server shutdown."
