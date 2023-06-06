#!/bin/bash

ENV_FILE='.env'
PASSWORD_FILE='.password'

# check if script is being run at the root of the project
if [ ! -f 'scripts/createDotEnv.sh' ]; then
  echo "Please run this script from the root of the project"
  exit 1
fi

# Define global variables
ROOT_PATH=$(pwd)
BUILD_PATH=$ROOT_PATH/build/
DOC_PATH=$ROOT_PATH/doc/
LIB_PATH=$ROOT_PATH/lib/
LOG_PATH=$ROOT_PATH/log/
OTHERS_PATH=$ROOT_PATH/others/
SCRIPTS_PATH=$ROOT_PATH/scripts/
SRC_PATH=$ROOT_PATH/src/
TEST_PATH=$ROOT_PATH/test/
UTILS_PATH=$ROOT_PATH/utils/
EXPORT_PATH=$ROOT_PATH/export/

# get login from user
read -p "Please enter your login: " LOGIN

# get first port number from user
read -p "Please enter the first port number: " PORT0
if [[ ! $PORT0 =~ ^-?[0-9]+$ ]]; then
  echo "Error: Port number must be an integer!"
  exit 1
fi
if [ "$PORT0" -lt 0 ] || [ "$PORT0" -gt 65535 ]; then
  echo "Error: Port number must be between 0 and 65535"
  exit 1
fi

#generate other port numbers
for i in $(seq 1 10); do
  eval "PORT${i}=$(expr $PORT0 + $i)"
done

# get password from user
read -sp "Please enter your password: " PASSWORD
echo

# Print global variables to .env file
echo "ROOT_PATH=$(pwd)" > $ENV_FILE
echo "BUILD_PATH=$BUILD_PATH" >> $ENV_FILE
echo "DOC_PATH=$DOC_PATH" >> $ENV_FILE
echo "LIB_PATH=$LIB_PATH" >> $ENV_FILE
echo "LOG_PATH=$LOG_PATH" >> $ENV_FILE
echo "OTHERS_PATH=$OTHERS_PATH" >> $ENV_FILE
echo "SCRIPTS_PATH=$SCRIPTS_PATH" >> $ENV_FILE
echo "SRC_PATH=$SRC_PATH" >> $ENV_FILE
echo "TEST_PATH=$TEST_PATH" >> $ENV_FILE
echo "UTILS_PATH=$UTILS_PATH" >> $ENV_FILE
echo "EXPORT_PATH=$EXPORT_PATH" >> $ENV_FILE
echo "LOGIN=$LOGIN" >> $ENV_FILE
echo "NODE01=l040101-ws01.ua.pt" >> $ENV_FILE
echo "NODE02=l040101-ws02.ua.pt" >> $ENV_FILE
echo "NODE03=l040101-ws03.ua.pt" >> $ENV_FILE
echo "NODE04=l040101-ws04.ua.pt" >> $ENV_FILE
echo "NODE05=l040101-ws05.ua.pt" >> $ENV_FILE
echo "NODE06=l040101-ws06.ua.pt" >> $ENV_FILE
echo "NODE07=l040101-ws07.ua.pt" >> $ENV_FILE
echo "NODE08=l040101-ws08.ua.pt" >> $ENV_FILE
echo "NODE09=l040101-ws09.ua.pt" >> $ENV_FILE
echo "PORT0=$PORT0" >> $ENV_FILE
echo "PORT1=$PORT1" >> $ENV_FILE
echo "PORT2=$PORT2" >> $ENV_FILE
echo "PORT3=$PORT3" >> $ENV_FILE
echo "PORT4=$PORT4" >> $ENV_FILE
echo "PORT5=$PORT5" >> $ENV_FILE
echo "PORT6=$PORT6" >> $ENV_FILE
echo "PORT7=$PORT7" >> $ENV_FILE
echo "PORT8=$PORT8" >> $ENV_FILE
echo "PORT9=$PORT9" >> $ENV_FILE
echo "$PASSWORD" >> $PASSWORD_FILE

echo -e ".env file created successfully!\n"
