#!/bin/bash

source .env

xterm  -T "General Repository" -hold -e "$SCRIPTS_PATH/GeneralRepositoryRunLocal.sh" &

sleep 1

xterm  -T "Assault Party 1" -hold -e "$SCRIPTS_PATH/AssaultParty1RunLocal.sh" &
xterm  -T "Assault Party 2" -hold -e "$SCRIPTS_PATH/AssaultParty2RunLocal.sh" &
xterm  -T "Concentration Site" -hold -e "$SCRIPTS_PATH/ConcentrationSiteRunLocal.sh" &
xterm  -T "Control Collection Site" -hold -e "$SCRIPTS_PATH/ControlCollectionSiteRunLocal.sh" &
xterm  -T "Museum" -hold -e "$SCRIPTS_PATH/MuseumRunLocal.sh" &

sleep 1

xterm  -T "Master Thief" -hold -e "$SCRIPTS_PATH/MasterThiefRunLocal.sh" &
xterm  -T "Ordinary Thieves" -hold -e "$SCRIPTS_PATH/OrdinaryThievesRunLocal.sh" &
