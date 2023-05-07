#!/usr/bin/bash

source .env

echo "Executing the master thief node."

cd  $TEST_PATH/dirMasterThief

java clientSide.main.ClientHeistToTheMuseumMasterThief 127.0.0.1 22311 127.0.0.1 22312 127.0.0.1 22313 127.0.0.1 22314 127.0.0.1 22315 127.0.0.1 22310 stat
echo "Master thief server shutdown."
