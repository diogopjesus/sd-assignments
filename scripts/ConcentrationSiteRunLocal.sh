#!/bin/bash

source .env

echo "Executing concentration site program locally."

cd  $TEST_PATH/dirConcentrationSite

java serverSide.main.ServerHeistToTheMuseumConcentrationSite 22313 127.0.0.1 22310 127.0.0.1 22314 127.0.0.1 22311 127.0.0.1 22312 127.0.0.1 22315
