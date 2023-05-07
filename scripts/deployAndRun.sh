#!/bin/bash

source .env

xterm  -T "General Repository" -hold -e "$SCRIPTS_PATH/GeneralRepositoryDeployAndRun.sh" &
xterm  -T "Assault Party 1" -hold -e "$SCRIPTS_PATH/AssaultParty1DeployAndRun.sh" &
xterm  -T "Assault Party 2" -hold -e "$SCRIPTS_PATH/AssaultParty2DeployAndRun.sh" &
xterm  -T "Concentration Site" -hold -e "$SCRIPTS_PATH/ConcentrationSiteDeployAndRun.sh" &
xterm  -T "Control Collection Site" -hold -e "$SCRIPTS_PATH/ControlCollectionSiteDeployAndRun.sh" &
xterm  -T "Museum" -hold -e "$SCRIPTS_PATH/MuseumDeployAndRun.sh" &

sleep 1

xterm  -T "Client" -hold -e "$SCRIPTS_PATH/ClientDeployAndRun.sh" &
