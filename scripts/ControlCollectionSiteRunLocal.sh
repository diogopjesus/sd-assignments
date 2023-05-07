#!/bin/bash

source .env

echo "Executing the control collection site node."

cd  $BUILD_PATH/dirControlCollectionSite

java serverSide.main.ServerHeistToTheMuseumControlCollectionSite 22314 127.0.0.1 22310 127.0.0.1 22311 127.0.0.1 22312
echo "Control collection site server shutdown."
