#!/bin/bash

source .env

xterm  -T "General Repository" -hold -e "$SCRIPTS_PATH/GeneralRepositoryDeployAndRun.sh" &
xterm  -T "Assault Party" -hold -e "$SCRIPTS_PATH/AssaultPartyDeployAndRun.sh" &
xterm  -T "Concentration Site" -hold -e "$SCRIPTS_PATH/ConcentrationSiteDeployAndRun.sh" &
xterm  -T "Control Collection Site" -hold -e "$SCRIPTS_PATH/ControlCollectionSiteDeployAndRun.sh" &
xterm  -T "Museum" -hold -e "$SCRIPTS_PATH/MuseumDeployAndRun.sh" &

sleep 1

xterm  -T "Master Thief" -hold -e "$SCRIPTS_PATH/MasterThiefDeployAndRun.sh" &
xterm  -T "Ordinary Thieves" -hold -e "$SCRIPTS_PATH/OrdinaryThievesDeployAndRun.sh" &
