#!/bin/bash

source .env

echo "Executing general repository program locally."

cd  $TEST_PATH/dirGeneralRepository

java serverSide.main.ServerHeistToTheMuseumGeneralRepository 22310

less stat

echo "GeneralRepos Server shutdown."
