#!/bin/bash

source .env

echo "Executing the concentration site node."

cd  $BUILD_PATH/dirConcentrationSite

java serverSide.main.ServerHeistToTheMuseumConcentrationSite 22313 127.0.0.1 22310 127.0.0.1 22314 127.0.0.1 22311 127.0.0.1 22312 127.0.0.1 22315
echo "Concentration site server shutdown."
