#!/bin/bash

source .env

echo "Executing assault party 1 program locally."

cd  $TEST_PATH/dirAssaultParty

java serverSide.main.ServerHeistToTheMuseumAssaultParty 0 22311 127.0.0.1 22310
