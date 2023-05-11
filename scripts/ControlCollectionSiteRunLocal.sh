#!/bin/bash

source .env

echo "Executing control collection site program locally."

cd  $TEST_PATH/dirControlCollectionSite

java serverSide.main.ServerHeistToTheMuseumControlCollectionSite 22314 127.0.0.1 22310 127.0.0.1 22311 127.0.0.1 22312
