#!/bin/bash

source .env

echo "Executing the GeneraRepos node."

cd  $BUILD_PATH/dirGeneralRepository

java serverSide.main.ServerHeistToTheMuseumGeneralRepository 22310

echo "GeneralRepos Server shutdown."
