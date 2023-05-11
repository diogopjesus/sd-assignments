#!/bin/bash

FILE=".env"

# check if script is being run at the root of the project
if [ ! -f "scripts/createDotEnv.sh" ]; then
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

# get login from user
read "Please enter your login:" LOGIN

# get first port number from user
read "Please enter the first port number:" PORT0
for i in {1..9}
do
  PORT[$i]=$((PORT0+i))
done

# Print global variables to .env file
echo "ROOT_PATH=$(pwd)" > $FILE
echo "BUILD_PATH=$BUILD_PATH" >> $FILE
echo "DOC_PATH=$DOC_PATH" >> $FILE
echo "LIB_PATH=$LIB_PATH" >> $FILE
echo "LOG_PATH=$LOG_PATH" >> $FILE
echo "OTHERS_PATH=$OTHERS_PATH" >> $FILE
echo "SCRIPTS_PATH=$SCRIPTS_PATH" >> $FILE
echo "SRC_PATH=$SRC_PATH" >> $FILE
echo "TEST_PATH=$TEST_PATH" >> $FILE
echo "UTILS_PATH=$UTILS_PATH" >> $FILE
echo "LOGIN=$LOGIN" >> $FILE
echo "NODE01=l040101-ws01.ua.pt" >> $FILE
echo "NODE02=l040101-ws02.ua.pt" >> $FILE
echo "NODE03=l040101-ws03.ua.pt" >> $FILE
echo "NODE04=l040101-ws04.ua.pt" >> $FILE
echo "NODE05=l040101-ws05.ua.pt" >> $FILE
echo "NODE06=l040101-ws06.ua.pt" >> $FILE
echo "NODE07=l040101-ws07.ua.pt" >> $FILE
echo "NODE08=l040101-ws08.ua.pt" >> $FILE
echo "NODE09=l040101-ws09.ua.pt" >> $FILE
echo "PORT0=$PORT0" >> $FILE
echo "PORT1=$PORT1" >> $FILE
echo "PORT2=$PORT2" >> $FILE
echo "PORT3=$PORT3" >> $FILE
echo "PORT4=$PORT4" >> $FILE
echo "PORT5=$PORT5" >> $FILE
echo "PORT6=$PORT6" >> $FILE
echo "PORT7=$PORT7" >> $FILE
echo "PORT8=$PORT8" >> $FILE
echo "PORT9=$PORT9" >> $FILE
