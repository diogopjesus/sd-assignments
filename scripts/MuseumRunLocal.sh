#!/bin/bash

source .env

echo "Executing museum program locally."

cd  $TEST_PATH/dirMuseum

java serverSide.main.ServerHeistToTheMuseumMuseum 22315 127.0.0.1 22310 127.0.0.1 22311 127.0.0.1 22312
