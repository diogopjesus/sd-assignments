#!/bin/bash

source .env

echo "Executing client program locally."

cd  $TEST_PATH/dirClient

java clientSide.main.ClientHeistToTheMuseum 127.0.0.1 22311 127.0.0.1 22312 127.0.0.1 22313 127.0.0.1 22314 127.0.0.1 22315 127.0.0.1 22310 stat
