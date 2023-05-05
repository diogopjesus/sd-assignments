#!/bin/bash

source .env

echo "Compiling source code."
cd $SRC_PATH
javac --release 8 */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

mkdir -p $BUILD_PATH
cd $BUILD_PATH

echo "  General Repository"
rm -rf dirGeneralRepository
mkdir -p dirGeneralRepository \
         dirGeneralRepository/serverSide dirGeneralRepository/serverSide/main dirGeneralRepository/serverSide/entities dirGeneralRepository/serverSide/sharedRegions \
         dirGeneralRepository/clientSide dirGeneralRepository/clientSide/entities dirGeneralRepository/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumGeneralRepository.class \
   dirGeneralRepository/serverSide/main

cp $SRC_PATH/serverSide/entities/GeneralRepositoryClientProxy.class dirGeneralRepository/serverSide/entities

cp $SRC_PATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRC_PATH/serverSide/sharedRegions/GeneralRepository.class \
   dirGeneralRepository/serverSide/sharedRegions

cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   dirGeneralRepository/clientSide/entities

cp $SRC_PATH/commInfra/Message.class \
   $SRC_PATH/commInfra/MessageType.class \
   $SRC_PATH/commInfra/MessageException.class \
   $SRC_PATH/commInfra/ServerCom.class \
   dirGeneralRepository/commInfra


echo "  Assault Party"
rm -rf dirAssaultParty
mkdir -p dirAssaultParty \
         dirAssaultParty/serverSide dirAssaultParty/serverSide/main dirAssaultParty/serverSide/entities dirAssaultParty/serverSide/sharedRegions \
         dirAssaultParty/clientSide dirAssaultParty/clientSide/entities dirAssaultParty/clientSide/stubs \
         dirAssaultParty/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   dirAssaultParty/serverSide/main

cp $SRC_PATH/serverSide/entities/AssaultPartyClientProxy.class dirAssaultParty/serverSide/entities

cp $SRC_PATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRC_PATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRC_PATH/serverSide/sharedRegions/AssaultParty.class \
   dirAssaultParty/serverSide/sharedRegions

cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/clientSide/entities/MasterThiefCloning.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRC_PATH/clientSide/stubs/GeneralRepositoryStub.class dirAssaultParty/clientSide/stubs

cp $SRC_PATH/commInfra/*.class dirAssaultParty/commInfra


echo "  Concentration Site"
rm -rf dirConcentrationSite
mkdir -p dirConcentrationSite \
         dirConcentrationSite/serverSide dirConcentrationSite/serverSide/main dirConcentrationSite/serverSide/entities dirConcentrationSite/serverSide/sharedRegions \
         dirConcentrationSite/clientSide dirConcentrationSite/clientSide/entities dirConcentrationSite/clientSide/stubs \
         dirConcentrationSite/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumConcentrationSite.class \
   dirConcentrationSite/serverSide/main

cp $SRC_PATH/serverSide/entities/ConcentrationSiteClientProxy.class dirConcentrationSite/serverSide/entities

cp $SRC_PATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRC_PATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ConcentrationSite.class \
   dirConcentrationSite/serverSide/sharedRegions

cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/clientSide/entities/MasterThiefCloning.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRC_PATH/clientSide/stubs/GeneralRepositoryStub.class dirConcentrationSite/clientSide/stubs

cp $SRC_PATH/commInfra/*.class dirConcentrationSite/commInfra


echo "  Control Collection Site"
rm -rf dirControlCollectionSite
mkdir -p dirControlCollectionSite \
         dirControlCollectionSite/serverSide dirControlCollectionSite/serverSide/main dirControlCollectionSite/serverSide/entities dirControlCollectionSite/serverSide/sharedRegions \
         dirControlCollectionSite/clientSide dirControlCollectionSite/clientSide/entities dirControlCollectionSite/clientSide/stubs \
         dirControlCollectionSite/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumControlCollectionSite.class \
   dirControlCollectionSite/serverSide/main

cp $SRC_PATH/serverSide/entities/ControlCollectionSiteClientProxy.class dirControlCollectionSite/serverSide/entities

cp $SRC_PATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRC_PATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ControlCollectionSite.class \
   dirControlCollectionSite/serverSide/sharedRegions

cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/clientSide/entities/MasterThiefCloning.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRC_PATH/clientSide/stubs/GeneralRepositoryStub.class dirControlCollectionSite/clientSide/stubs

cp $SRC_PATH/commInfra/*.class dirControlCollectionSite/commInfra


echo "  Museum"
rm -rf dirMuseum
mkdir -p dirMuseum \
         dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/entities dirMuseum/serverSide/sharedRegions \
         dirMuseum/clientSide dirMuseum/clientSide/entities dirMuseum/clientSide/stubs \
         dirMuseum/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumMuseum.class \
   dirMuseum/serverSide/main

cp $SRC_PATH/serverSide/entities/MuseumClientProxy.class dirMuseum/serverSide/entities

cp $SRC_PATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRC_PATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRC_PATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRC_PATH/serverSide/sharedRegions/Museum.class \
   dirMuseum/serverSide/sharedRegions

cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/clientSide/entities/MasterThiefCloning.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRC_PATH/clientSide/stubs/GeneralRepositoryStub.class dirMuseum/clientSide/stubs

cp $SRC_PATH/commInfra/*.class dirMuseum/commInfra


echo "  Master Thief"
rm -rf dirMasterThief
mkdir -p dirMasterThief \
         dirMasterThief/serverSide dirMasterThief/serverSide/main \
         dirMasterThief/clientSide dirMasterThief/clientSide/main dirMasterThief/clientSide/entities dirMasterThief/clientSide/stubs \
         dirMasterThief/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class dirMasterThief/serverSide/main

cp $SRC_PATH/clientSide/main/ClientHeistToTheMuseumMasterThief.class dirMasterThief/clientSide/main

cp $SRC_PATH/clientSide/entities/MasterThief.class \
   $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   dirMasterThief/clientSide/entities

cp $SRC_PATH/clientSide/stubs/GeneralRepositoryStub.class \
   $SRC_PATH/clientSide/stubs/AssaultPartyStub.class \
   $SRC_PATH/clientSide/stubs/ConcentrationSiteStub.class \
   $SRC_PATH/clientSide/stubs/ControlCollectionSiteStub.class \
   $SRC_PATH/clientSide/stubs/MuseumStub.class \
   dirMasterThief/clientSide/stubs

cp $SRC_PATH/commInfra/Message.class \
   $SRC_PATH/commInfra/MessageType.class \
   $SRC_PATH/commInfra/MessageException.class \
   $SRC_PATH/commInfra/ClientCom.class \
   dirMasterThief/commInfra


echo "  Ordinary Thieves"
rm -rf dirOrdinaryThieves
mkdir -p dirOrdinaryThieves \
         dirOrdinaryThieves/serverSide dirOrdinaryThieves/serverSide/main \
         dirOrdinaryThieves/clientSide dirOrdinaryThieves/clientSide/main dirOrdinaryThieves/clientSide/entities dirOrdinaryThieves/clientSide/stubs \
         dirOrdinaryThieves/commInfra

cp $SRC_PATH/serverSide/main/SimulPar.class dirOrdinaryThieves/serverSide/main

cp $SRC_PATH/clientSide/main/ClientHeistToTheMuseumOrdinaryThief.class dirOrdinaryThieves/clientSide/main

cp $SRC_PATH/clientSide/entities/OrdinaryThief.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   dirOrdinaryThieves/clientSide/entities

cp $SRC_PATH/clientSide/stubs/GeneralRepositoryStub.class \
   $SRC_PATH/clientSide/stubs/AssaultPartyStub.class \
   $SRC_PATH/clientSide/stubs/ConcentrationSiteStub.class \
   $SRC_PATH/clientSide/stubs/ControlCollectionSiteStub.class \
   $SRC_PATH/clientSide/stubs/MuseumStub.class \
   dirOrdinaryThieves/clientSide/stubs

cp $SRC_PATH/commInfra/Message.class \
   $SRC_PATH/commInfra/MessageType.class \
   $SRC_PATH/commInfra/MessageException.class \
   $SRC_PATH/commInfra/ClientCom.class \
   dirOrdinaryThieves/commInfra


echo "Compressing execution environments."
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

echo "  Master Thief"
rm -f  dirMasterThief.zip
zip -rq dirMasterThief.zip dirMasterThief

echo "  Ordinary Thieves"
rm -f  dirOrdinaryThieves.zip
zip -rq dirOrdinaryThieves.zip dirOrdinaryThieves
