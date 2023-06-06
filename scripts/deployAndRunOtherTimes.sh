xterm  -T "General Repository" -hold -e "./GeneralRepositoryDeployAndRun.sh" &
sleep 8

xterm  -T "Assault Party 1" -hold -e "./AssaultParty1DeployAndRun.sh" &

xterm  -T "Assault Party 2" -hold -e "./AssaultParty2DeployAndRun.sh" &
sleep 8

xterm  -T "Control Collection Site" -hold -e "./ControlCollectionSiteDeployAndRun.sh" &

xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 8

xterm  -T "Concentration Site" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
sleep 8

xterm  -T "Client" -hold -e "./ClientDeployAndRun.sh" &
