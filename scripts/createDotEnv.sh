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

# TODO: add authentication parameters to .env file
