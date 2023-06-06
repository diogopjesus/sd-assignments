source .env

xterm  -T "RMI registry" -hold -e "$SCRIPTS_PATH/RMIRegistryDeployAndRun.sh" &
sleep 6

xterm  -T "Registry" -hold -e "$SCRIPTS_PATH/RegistryDeployAndRun.sh" &
sleep 8

xterm  -T "General Repository" -hold -e "$SCRIPTS_PATH/GeneralRepositoryDeployAndRun.sh" &
sleep 6

xterm  -T "Assault Party 1" -hold -e "$SCRIPTS_PATH/AssaultParty1DeployAndRun.sh" &

xterm  -T "Assault Party 2" -hold -e "$SCRIPTS_PATH/AssaultParty2DeployAndRun.sh" &
sleep 6

xterm  -T "Control Collection Site" -hold -e "$SCRIPTS_PATH/ControlCollectionSiteDeployAndRun.sh" &

xterm  -T "Museum" -hold -e "$SCRIPTS_PATH/MuseumDeployAndRun.sh" &
sleep 6

xterm  -T "Concentration Site" -hold -e "$SCRIPTS_PATH/ConcentrationSiteDeployAndRun.sh" &
sleep 6

xterm  -T "Client" -hold -e "$SCRIPTS_PATH/ClientDeployAndRun.sh" &
