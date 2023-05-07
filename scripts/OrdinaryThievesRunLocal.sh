#!/bin/bash

source .env

echo "Executing the ordinary thief node."

cd  $TEST_PATH/dirOrdinaryThieves

java clientSide.main.ClientHeistToTheMuseumOrdinaryThief 127.0.0.1 22311 127.0.0.1 22312 127.0.0.1 22313 127.0.0.1 22314 127.0.0.1 22315 127.0.0.1 22310
echo "Ordinary thief server shutdown."
