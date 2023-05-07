#!/bin/bash

source .env

echo "Executing the ap1 node."

cd  $TEST_PATH/dirAssaultParty

java serverSide.main.ServerHeistToTheMuseumAssaultParty 0 22311 127.0.0.1 22310

echo "AP1 Server shutdown."
