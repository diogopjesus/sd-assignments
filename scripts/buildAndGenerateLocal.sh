#!/bin/bash

ROOTPATH="$(pwd)/../"
SRCPATH="$ROOTPATH/src/"
LIBPATH="$ROOTPATH/lib/"
SCRIPTPATH="$ROOTPATH/scripts/"
BUILDPATH="$ROOTPATH/build/"
TESTPATH="$ROOTPATH/test/"

export CLASSPATH="$CLASSPATH:$LIBPATH/genclass.jar"

echo "Compiling source code."
cd $SRCPATH
javac */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

mkdir -p $BUILDPATH
cd $BUILDPATH

echo "  General Repository"
rm -rf dirGeneralRepository
mkdir -p dirGeneralRepository \
         dirGeneralRepository/serverSide dirGeneralRepository/serverSide/main dirGeneralRepository/serverSide/entities dirGeneralRepository/serverSide/sharedRegions \
         dirGeneralRepository/clientSide dirGeneralRepository/clientSide/entities dirGeneralRepository/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class \
   $SRCPATH/serverSide/main/ServerHeistToTheMuseumGeneralRepository.class \
   dirGeneralRepository/serverSide/main

cp $SRCPATH/serverSide/entities/GeneralRepositoryClientProxy.class dirGeneralRepository/serverSide/entities

cp $SRCPATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRCPATH/serverSide/sharedRegions/GeneralRepository.class \
   dirGeneralRepository/serverSide/sharedRegions

cp $SRCPATH/clientSide/entities/MasterThiefStates.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefStates.class \
   dirGeneralRepository/clientSide/entities

cp $SRCPATH/commInfra/Message.class \
   $SRCPATH/commInfra/MessageType.class \
   $SRCPATH/commInfra/MessageException.class \
   $SRCPATH/commInfra/ServerCom.class \
   dirGeneralRepository/commInfra


echo "  Assault Party"
rm -rf dirAssaultParty
mkdir -p dirAssaultParty \
         dirAssaultParty/serverSide dirAssaultParty/serverSide/main dirAssaultParty/serverSide/entities dirAssaultParty/serverSide/sharedRegions \
         dirAssaultParty/clientSide dirAssaultParty/clientSide/entities dirAssaultParty/clientSide/stubs \
         dirAssaultParty/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class \
   $SRCPATH/serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   dirAssaultParty/serverSide/main

cp $SRCPATH/serverSide/entities/AssaultPartyClientProxy.class dirAssaultParty/serverSide/entities

cp $SRCPATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRCPATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRCPATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRCPATH/serverSide/sharedRegions/AssaultParty.class \
   dirAssaultParty/serverSide/sharedRegions

cp $SRCPATH/clientSide/entities/MasterThiefStates.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRCPATH/clientSide/entities/MasterThiefCloning.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRCPATH/clientSide/stubs/GeneralRepositoryStub.class dirAssaultParty/clientSide/stubs

cp $SRCPATH/commInfra/*.class dirAssaultParty/commInfra


echo "  Concentration Site"
rm -rf dirConcentrationSite
mkdir -p dirConcentrationSite \
         dirConcentrationSite/serverSide dirConcentrationSite/serverSide/main dirConcentrationSite/serverSide/entities dirConcentrationSite/serverSide/sharedRegions \
         dirConcentrationSite/clientSide dirConcentrationSite/clientSide/entities dirConcentrationSite/clientSide/stubs \
         dirConcentrationSite/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class \
   $SRCPATH/serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   dirConcentrationSite/serverSide/main

cp $SRCPATH/serverSide/entities/ConcentrationSiteClientProxy.class dirConcentrationSite/serverSide/entities

cp $SRCPATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRCPATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRCPATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRCPATH/serverSide/sharedRegions/ConcentrationSite.class \
   dirConcentrationSite/serverSide/sharedRegions

cp $SRCPATH/clientSide/entities/MasterThiefStates.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRCPATH/clientSide/entities/MasterThiefCloning.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRCPATH/clientSide/stubs/GeneralRepositoryStub.class dirConcentrationSite/clientSide/stubs

cp $SRCPATH/commInfra/*.class dirConcentrationSite/commInfra


echo "  Control Collection Site"
rm -rf dirControlCollectionSite
mkdir -p dirControlCollectionSite \
         dirControlCollectionSite/serverSide dirControlCollectionSite/serverSide/main dirControlCollectionSite/serverSide/entities dirControlCollectionSite/serverSide/sharedRegions \
         dirControlCollectionSite/clientSide dirControlCollectionSite/clientSide/entities dirControlCollectionSite/clientSide/stubs \
         dirControlCollectionSite/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class \
   $SRCPATH/serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   dirControlCollectionSite/serverSide/main

cp $SRCPATH/serverSide/entities/ControlCollectionSiteClientProxy.class dirControlCollectionSite/serverSide/entities

cp $SRCPATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRCPATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRCPATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRCPATH/serverSide/sharedRegions/ControlCollectionSite.class \
   dirControlCollectionSite/serverSide/sharedRegions

cp $SRCPATH/clientSide/entities/MasterThiefStates.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRCPATH/clientSide/entities/MasterThiefCloning.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRCPATH/clientSide/stubs/GeneralRepositoryStub.class dirControlCollectionSite/clientSide/stubs

cp $SRCPATH/commInfra/*.class dirControlCollectionSite/commInfra


echo "  Museum"
rm -rf dirMuseum
mkdir -p dirMuseum \
         dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/entities dirMuseum/serverSide/sharedRegions \
         dirMuseum/clientSide dirMuseum/clientSide/entities dirMuseum/clientSide/stubs \
         dirMuseum/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class \
   $SRCPATH/serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   dirMuseum/serverSide/main

cp $SRCPATH/serverSide/entities/MuseumClientProxy.class dirMuseum/serverSide/entities

cp $SRCPATH/serverSide/sharedRegions/GeneralRepositoryInterface.class \
   $SRCPATH/serverSide/sharedRegions/AssaultPartyInterface.class \
   $SRCPATH/serverSide/sharedRegions/ConcentrationSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/ControlCollectionSiteInterface.class \
   $SRCPATH/serverSide/sharedRegions/MuseumInterface.class \
   $SRCPATH/serverSide/sharedRegions/Museum.class \
   dirMuseum/serverSide/sharedRegions

cp $SRCPATH/clientSide/entities/MasterThiefStates.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRCPATH/clientSide/entities/MasterThiefCloning.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty/clientSide/entities

cp $SRCPATH/clientSide/stubs/GeneralRepositoryStub.class dirMuseum/clientSide/stubs

cp $SRCPATH/commInfra/*.class dirMuseum/commInfra


echo "  Master Thief"
rm -rf dirMasterThief
mkdir -p dirMasterThief \
         dirMasterThief/serverSide dirMasterThief/serverSide/main \
         dirMasterThief/clientSide dirMasterThief/clientSide/main dirMasterThief/clientSide/entities dirMasterThief/clientSide/stubs \
         dirMasterThief/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class dirMasterThief/serverSide/main

cp $SRCPATH/clientSide/main/ClientHeistToTheMuseumMasterThief.class dirMasterThief/clientSide/main

cp $SRCPATH/clientSide/entities/MasterThief.class \
   $SRCPATH/clientSide/entities/MasterThiefStates.class \
   dirMasterThief/clientSide/entities

cp $SRCPATH/clientSide/stubs/GeneralRepositoryStub.class \
   $SRCPATH/clientSide/stubs/AssaultPartyStub.class \
   $SRCPATH/clientSide/stubs/ConcentrationSiteStub.class \
   $SRCPATH/clientSide/stubs/ControlCollectionSiteStub.class \
   $SRCPATH/clientSide/stubs/MuseumStub.class \
   dirMasterThief/clientSide/stubs

cp $SRCPATH/commInfra/Message.class \
   $SRCPATH/commInfra/MessageType.class \
   $SRCPATH/commInfra/MessageException.class \
   $SRCPATH/commInfra/ClientCom.class \
   dirMasterThief/commInfra


echo "  Ordinary Thieves"
rm -rf dirOrdinaryThieves
mkdir -p dirOrdinaryThieves \
         dirOrdinaryThieves/serverSide dirOrdinaryThieves/serverSide/main \
         dirOrdinaryThieves/clientSide dirOrdinaryThieves/clientSide/main dirOrdinaryThieves/clientSide/entities dirOrdinaryThieves/clientSide/stubs \
         dirOrdinaryThieves/commInfra

cp $SRCPATH/serverSide/main/SimulPar.class dirOrdinaryThieves/serverSide/main

cp $SRCPATH/clientSide/main/ClientHeistToTheMuseumOrdinaryThief.class dirOrdinaryThieves/clientSide/main

cp $SRCPATH/clientSide/entities/OrdinaryThief.class \
   $SRCPATH/clientSide/entities/OrdinaryThiefStates.class \
   dirOrdinaryThieves/clientSide/entities

cp $SRCPATH/clientSide/stubs/GeneralRepositoryStub.class \
   $SRCPATH/clientSide/stubs/AssaultPartyStub.class \
   $SRCPATH/clientSide/stubs/ConcentrationSiteStub.class \
   $SRCPATH/clientSide/stubs/ControlCollectionSiteStub.class \
   $SRCPATH/clientSide/stubs/MuseumStub.class \
   dirOrdinaryThieves/clientSide/stubs

cp $SRCPATH/commInfra/Message.class \
   $SRCPATH/commInfra/MessageType.class \
   $SRCPATH/commInfra/MessageException.class \
   $SRCPATH/commInfra/ClientCom.class \
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


echo "Deploying and decompressing execution environments."
mkdir -p $TESTPATH/HeistToTheMuseum
rm -rf $TESTPATH/HeistToTheMuseum/*

cp dirGeneralRepository.zip $TESTPATH/HeistToTheMuseum
cp dirAssaultParty.zip $TESTPATH/HeistToTheMuseum
cp dirConcentrationSite.zip $TESTPATH/HeistToTheMuseum
cp dirControlCollectionSite.zip $TESTPATH/HeistToTheMuseum
cp dirMuseum.zip $TESTPATH/HeistToTheMuseum
cp dirMasterThief.zip $TESTPATH/HeistToTheMuseum
cp dirOrdinaryThieves.zip $TESTPATH/HeistToTheMuseum

cd $TESTPATH/HeistToTheMuseum
unzip -q dirGeneralRepository.zip
unzip -q dirAssaultParty.zip
unzip -q dirConcentrationSite.zip
unzip -q dirControlCollectionSite.zip
unzip -q dirMuseum.zip
unzip -q dirMasterThief.zip
unzip -q dirOrdinaryThieves.zip
