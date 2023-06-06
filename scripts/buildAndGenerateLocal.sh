echo "Compiling source code."
javac */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide \
         dirRegistry/serverSide/main \
         dirRegistry/serverSide/objects \
         dirRegistry/interfaces
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces

echo "  General Repository"
rm -rf dirGeneralRepository/serverSide dirGeneralRepository/clientSide dirGeneralRepository/interfaces
mkdir -p dirGeneralRepository/serverSide \
         dirGeneralRepository/serverSide/main \
         dirGeneralRepository/serverSide/objects \
         dirGeneralRepository/interfaces \
         dirGeneralRepository/clientSide \
         dirGeneralRepository/clientSide/entities
cp serverSide/main/SimulPar.class \
   serverSide/main/ServerHeistToTheMuseumGeneralRepository.class \
   dirGeneralRepository/serverSide/main
cp serverSide/objects/GeneralRepository.class dirGeneralRepository/serverSide/objects
cp interfaces/Register.class \
   interfaces/GeneralRepositoryInterface.class \
   dirGeneralRepository/interfaces
cp clientSide/entities/MasterThiefStates.class \
   clientSide/entities/OrdinaryThiefStates.class \
   dirGeneralRepository/clientSide/entities

echo "  Assault Party"
rm -rf dirAssaultParty/serverSide \
       dirAssaultParty/clientSide \
       dirAssaultParty/interfaces \
       dirAssaultParty/commInfra
mkdir -p dirAssaultParty/serverSide \
         dirAssaultParty/serverSide/main \
         dirAssaultParty/serverSide/objects \
         dirAssaultParty/interfaces \
         dirAssaultParty/clientSide \
         dirAssaultParty/clientSide/entities \
         dirAssaultParty/commInfra
cp serverSide/main/SimulPar.class \
   serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   dirAssaultParty/serverSide/main
cp serverSide/objects/AssaultParty.class dirAssaultParty/serverSide/objects
cp interfaces/*.class dirAssaultParty/interfaces
cp clientSide/entities/MasterThiefStates.class \
   clientSide/entities/OrdinaryThiefStates.class \
   dirAssaultParty/clientSide/entities
cp commInfra/*.class dirAssaultParty/commInfra

echo "  Concentration Site"
rm -rf dirConcentrationSite/serverSide \
       dirConcentrationSite/clientSide \
       dirConcentrationSite/interfaces \
       dirConcentrationSite/commInfra
mkdir -p dirConcentrationSite/serverSide \
         dirConcentrationSite/serverSide/main \
         dirConcentrationSite/serverSide/objects \
         dirConcentrationSite/interfaces \
         dirConcentrationSite/clientSide \
         dirConcentrationSite/clientSide/entities \
         dirConcentrationSite/commInfra
cp serverSide/main/SimulPar.class \
   serverSide/main/ServerHeistToTheMuseumConcentrationSite.class \
   dirConcentrationSite/serverSide/main
cp serverSide/objects/ConcentrationSite.class dirConcentrationSite/serverSide/objects
cp interfaces/*.class dirConcentrationSite/interfaces
cp clientSide/entities/MasterThiefStates.class \
   clientSide/entities/OrdinaryThiefStates.class \
   dirConcentrationSite/clientSide/entities
cp commInfra/*.class dirConcentrationSite/commInfra

echo "  Control Collection Site"
rm -rf dirControlCollectionSite/serverSide \
       dirControlCollectionSite/clientSide \
       dirControlCollectionSite/interfaces \
       dirControlCollectionSite/commInfra
mkdir -p dirControlCollectionSite/serverSide \
         dirControlCollectionSite/serverSide/main \
         dirControlCollectionSite/serverSide/objects \
         dirControlCollectionSite/interfaces \
         dirControlCollectionSite/clientSide \
         dirControlCollectionSite/clientSide/entities \
         dirControlCollectionSite/commInfra
cp serverSide/main/SimulPar.class \
   serverSide/main/ServerHeistToTheMuseumControlCollectionSite.class \
   dirControlCollectionSite/serverSide/main
cp serverSide/objects/ControlCollectionSite.class dirControlCollectionSite/serverSide/objects
cp interfaces/*.class dirControlCollectionSite/interfaces
cp clientSide/entities/MasterThiefStates.class \
   clientSide/entities/OrdinaryThiefStates.class \
   dirControlCollectionSite/clientSide/entities
cp commInfra/*.class dirControlCollectionSite/commInfra

echo "  Museum"
rm -rf dirMuseum/serverSide \
       dirMuseum/clientSide \
       dirMuseum/interfaces \
       dirMuseum/commInfra
mkdir -p dirMuseum/serverSide \
         dirMuseum/serverSide/main \
         dirMuseum/serverSide/objects \
         dirMuseum/interfaces \
         dirMuseum/clientSide \
         dirMuseum/clientSide/entities \
         dirMuseum/commInfra
cp serverSide/main/SimulPar.class \
   serverSide/main/ServerHeistToTheMuseumMuseum.class \
   dirMuseum/serverSide/main
cp serverSide/objects/Museum.class dirMuseum/serverSide/objects
cp interfaces/*.class dirMuseum/interfaces
cp clientSide/entities/MasterThiefStates.class \
   clientSide/entities/OrdinaryThiefStates.class \
   dirMuseum/clientSide/entities
cp commInfra/*.class dirMuseum/commInfra

echo "  Client"
rm -rf dirClient/serverSide \
       dirClient/clientSide \
       dirClient/interfaces
mkdir -p dirClient/serverSide \
         dirClient/serverSide/main \
         dirClient/clientSide \
         dirClient/clientSide/main \
         dirClient/clientSide/entities \
         dirClient/interfaces
cp serverSide/main/SimulPar.class dirClient/serverSide/main
cp clientSide/main/ClientHeistToTheMuseum.class dirClient/clientSide/main
cp clientSide/entities/MasterThief.class \
   clientSide/entities/MasterThiefStates.class \
   clientSide/entities/OrdinaryThief.class \
   clientSide/entities/OrdinaryThiefStates.class \
   dirClient/clientSide/entities
cp interfaces/GeneralRepositoryInterface.class \
   interfaces/AssaultPartyInterface.class \
   interfaces/ConcentrationSiteInterface.class \
   interfaces/ControlCollectionSiteInterface.class \
   interfaces/MuseumInterface.class \
   interfaces/ReturnBoolean.class \
   interfaces/ReturnInt.class \
   dirClient/interfaces

echo "Compressing execution environments."

echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry set_rmiregistry_alt.sh

echo "  General Repository"
rm -f  dirGeneralRepository.zip
zip -rq dirGeneralRepository.zip dirGeneralRepository

echo "  Assault Party"
rm -f  dirAssaultParty.zip
zip -rq dirAssaultParty.zip dirAssaultParty

echo "  Concentration Site"
rm -f  dirConcentrationSite.zip
zip -rq dirConcentrationSite.zip dirConcentrationSite

echo "  Control Collection Site"
rm -f  dirControlCollectionSite.zip
zip -rq dirControlCollectionSite.zip dirControlCollectionSite

echo "  Museum"
rm -f  dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum

echo "  Client"
rm -f  dirClient.zip
zip -rq dirClient.zip dirClient

echo "Deploying and decompressing execution environments."

mkdir -p /home/$1/test/HeistToTheMuseum
rm -rf /home/$1/test/HeistToTheMuseum/*
cp dirRegistry.zip /home/$1/test/HeistToTheMuseum
cp dirGeneralRepository.zip /home/$1/test/HeistToTheMuseum
cp dirAssaultParty.zip /home/$1/test/HeistToTheMuseum
cp dirConcentrationSite.zip /home/$1/test/HeistToTheMuseum
cp dirControlCollectionSite.zip /home/$1/test/HeistToTheMuseum
cp dirMuseum.zip /home/$1/test/HeistToTheMuseum
cp dirClient.zip /home/$1/test/HeistToTheMuseum
cd /home/$1/test/HeistToTheMuseum
unzip -q dirRegistry.zip
mv set_rmiregistry_alt.sh /home/$1
unzip -q dirGeneralRepository.zip
unzip -q dirAssaultParty.zip
unzip -q dirConcentrationSite.zip
unzip -q dirControlCollectionSite.zip
unzip -q dirMuseum.zip
unzip -q dirClient.zip
