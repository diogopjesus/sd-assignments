#!/bin/bash

source .env

echo "Executing assault party 2 program locally."

cd  $TEST_PATH/dirAssaultParty

java serverSide.main.ServerHeistToTheMuseumAssaultParty 1 22312 127.0.0.1 22310
