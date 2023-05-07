#!/bin/bash

source .env

echo "Executing the GeneraRepos node."

cd  $TEST_PATH/dirGeneralRepository

java serverSide.main.ServerHeistToTheMuseumGeneralRepository 22310

less stat

echo "GeneralRepos Server shutdown."
