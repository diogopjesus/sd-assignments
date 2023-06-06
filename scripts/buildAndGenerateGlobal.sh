source .env

echo "Compiling source code."
javac $SRC_PATH/*/*.java $SRC_PATH/*/*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  RMI registry"
rm -rf $SRC_PATH/dirRMIRegistry/interfaces
mkdir -p $SRC_PATH/dirRMIRegistry/interfaces
cp $SRC_PATH/interfaces/*.class $SRC_PATH/dirRMIRegistry/interfaces

echo "  Register Remote Objects"
rm -rf $SRC_PATH/dirRegistry/serverSide $SRC_PATH/dirRegistry/interfaces
mkdir -p $SRC_PATH/dirRegistry/serverSide \
         $SRC_PATH/dirRegistry/serverSide/main \
         $SRC_PATH/dirRegistry/serverSide/objects \
         $SRC_PATH/dirRegistry/interfaces
cp $SRC_PATH/serverSide/main/ServerRegisterRemoteObject.class $SRC_PATH/dirRegistry/serverSide/main
cp $SRC_PATH/serverSide/objects/RegisterRemoteObject.class $SRC_PATH/dirRegistry/serverSide/objects
cp $SRC_PATH/interfaces/Register.class $SRC_PATH/dirRegistry/interfaces

echo "  General Repository"
rm -rf $SRC_PATH/dirGeneralRepository/serverSide $SRC_PATH/dirGeneralRepository/clientSide $SRC_PATH/dirGeneralRepository/interfaces
mkdir -p $SRC_PATH/dirGeneralRepository/serverSide \
         $SRC_PATH/dirGeneralRepository/serverSide/main \
         $SRC_PATH/dirGeneralRepository/serverSide/objects \
         $SRC_PATH/dirGeneralRepository/interfaces \
         $SRC_PATH/dirGeneralRepository/clientSide \
         $SRC_PATH/dirGeneralRepository/clientSide/entities
cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumGeneralRepository.class \
   $SRC_PATH/dirGeneralRepository/serverSide/main
cp $SRC_PATH/serverSide/objects/GeneralRepository.class $SRC_PATH/dirGeneralRepository/serverSide/objects
cp $SRC_PATH/interfaces/Register.class \
   $SRC_PATH/interfaces/GeneralRepositoryInterface.class \
   $SRC_PATH/dirGeneralRepository/interfaces
cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/dirGeneralRepository/clientSide/entities

echo "  Assault Party"
rm -rf $SRC_PATH/dirAssaultParty/serverSide \
       $SRC_PATH/dirAssaultParty/clientSide \
       $SRC_PATH/dirAssaultParty/interfaces \
       $SRC_PATH/dirAssaultParty/commInfra
mkdir -p $SRC_PATH/dirAssaultParty/serverSide \
         $SRC_PATH/dirAssaultParty/serverSide/main \
         $SRC_PATH/dirAssaultParty/serverSide/objects \
         $SRC_PATH/dirAssaultParty/interfaces \
         $SRC_PATH/dirAssaultParty/clientSide \
         $SRC_PATH/dirAssaultParty/clientSide/entities \
         $SRC_PATH/dirAssaultParty/commInfra
cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumAssaultParty.class \
   $SRC_PATH/dirAssaultParty/serverSide/main
cp $SRC_PATH/serverSide/objects/AssaultParty.class $SRC_PATH/dirAssaultParty/serverSide/objects
cp $SRC_PATH/interfaces/*.class $SRC_PATH/dirAssaultParty/interfaces
cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/dirAssaultParty/clientSide/entities
cp $SRC_PATH/commInfra/*.class $SRC_PATH/dirAssaultParty/commInfra

echo "  Concentration Site"
rm -rf $SRC_PATH/dirConcentrationSite/serverSide \
       $SRC_PATH/dirConcentrationSite/clientSide \
       $SRC_PATH/dirConcentrationSite/interfaces \
       $SRC_PATH/dirConcentrationSite/commInfra
mkdir -p $SRC_PATH/dirConcentrationSite/serverSide \
         $SRC_PATH/dirConcentrationSite/serverSide/main \
         $SRC_PATH/dirConcentrationSite/serverSide/objects \
         $SRC_PATH/dirConcentrationSite/interfaces \
         $SRC_PATH/dirConcentrationSite/clientSide \
         $SRC_PATH/dirConcentrationSite/clientSide/entities \
         $SRC_PATH/dirConcentrationSite/commInfra
cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumConcentrationSite.class \
   $SRC_PATH/dirConcentrationSite/serverSide/main
cp $SRC_PATH/serverSide/objects/ConcentrationSite.class $SRC_PATH/dirConcentrationSite/serverSide/objects
cp $SRC_PATH/interfaces/*.class $SRC_PATH/dirConcentrationSite/interfaces
cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/dirConcentrationSite/clientSide/entities
cp $SRC_PATH/commInfra/*.class $SRC_PATH/dirConcentrationSite/commInfra

echo "  Control Collection Site"
rm -rf $SRC_PATH/dirControlCollectionSite/serverSide \
       $SRC_PATH/dirControlCollectionSite/clientSide \
       $SRC_PATH/dirControlCollectionSite/interfaces \
       $SRC_PATH/dirControlCollectionSite/commInfra
mkdir -p $SRC_PATH/dirControlCollectionSite/serverSide \
         $SRC_PATH/dirControlCollectionSite/serverSide/main \
         $SRC_PATH/dirControlCollectionSite/serverSide/objects \
         $SRC_PATH/dirControlCollectionSite/interfaces \
         $SRC_PATH/dirControlCollectionSite/clientSide \
         $SRC_PATH/dirControlCollectionSite/clientSide/entities \
         $SRC_PATH/dirControlCollectionSite/commInfra
cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumControlCollectionSite.class \
   $SRC_PATH/dirControlCollectionSite/serverSide/main
cp $SRC_PATH/serverSide/objects/ControlCollectionSite.class $SRC_PATH/dirControlCollectionSite/serverSide/objects
cp $SRC_PATH/interfaces/*.class $SRC_PATH/dirControlCollectionSite/interfaces
cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/dirControlCollectionSite/clientSide/entities
cp $SRC_PATH/commInfra/*.class $SRC_PATH/dirControlCollectionSite/commInfra

echo "  Museum"
rm -rf $SRC_PATH/dirMuseum/serverSide \
       $SRC_PATH/dirMuseum/clientSide \
       $SRC_PATH/dirMuseum/interfaces \
       $SRC_PATH/dirMuseum/commInfra
mkdir -p $SRC_PATH/dirMuseum/serverSide \
         $SRC_PATH/dirMuseum/serverSide/main \
         $SRC_PATH/dirMuseum/serverSide/objects \
         $SRC_PATH/dirMuseum/interfaces \
         $SRC_PATH/dirMuseum/clientSide \
         $SRC_PATH/dirMuseum/clientSide/entities \
         $SRC_PATH/dirMuseum/commInfra
cp $SRC_PATH/serverSide/main/SimulPar.class \
   $SRC_PATH/serverSide/main/ServerHeistToTheMuseumMuseum.class \
   $SRC_PATH/dirMuseum/serverSide/main
cp $SRC_PATH/serverSide/objects/Museum.class $SRC_PATH/dirMuseum/serverSide/objects
cp $SRC_PATH/interfaces/*.class $SRC_PATH/dirMuseum/interfaces
cp $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/dirMuseum/clientSide/entities
cp $SRC_PATH/commInfra/*.class $SRC_PATH/dirMuseum/commInfra

echo "  Client"
rm -rf $SRC_PATH/dirClient/serverSide \
       $SRC_PATH/dirClient/clientSide \
       $SRC_PATH/dirClient/interfaces
mkdir -p $SRC_PATH/dirClient/serverSide \
         $SRC_PATH/dirClient/serverSide/main \
         $SRC_PATH/dirClient/clientSide \
         $SRC_PATH/dirClient/clientSide/main \
         $SRC_PATH/dirClient/clientSide/entities \
         $SRC_PATH/dirClient/interfaces
cp $SRC_PATH/serverSide/main/SimulPar.class $SRC_PATH/dirClient/serverSide/main
cp $SRC_PATH/clientSide/main/ClientHeistToTheMuseum.class $SRC_PATH/dirClient/clientSide/main
cp $SRC_PATH/clientSide/entities/MasterThief.class \
   $SRC_PATH/clientSide/entities/MasterThiefStates.class \
   $SRC_PATH/clientSide/entities/OrdinaryThief.class \
   $SRC_PATH/clientSide/entities/OrdinaryThiefStates.class \
   $SRC_PATH/dirClient/clientSide/entities
cp $SRC_PATH/interfaces/GeneralRepositoryInterface.class \
   $SRC_PATH/interfaces/AssaultPartyInterface.class \
   $SRC_PATH/interfaces/ConcentrationSiteInterface.class \
   $SRC_PATH/interfaces/ControlCollectionSiteInterface.class \
   $SRC_PATH/interfaces/MuseumInterface.class \
   $SRC_PATH/interfaces/ReturnBoolean.class \
   $SRC_PATH/interfaces/ReturnInt.class \
   $SRC_PATH/dirClient/interfaces

echo "Compressing execution environments."

mkdir -p $EXPORT_PATH
cd $SRC_PATH

echo "  RMI registry"
rm -f  $EXPORT_PATH/dirRMIRegistry.zip
zip -rq $EXPORT_PATH/dirRMIRegistry.zip ./dirRMIRegistry

echo "  Register Remote Objects"
rm -f  $EXPORT_PATH/dirRegistry.zip
zip -rq $EXPORT_PATH/dirRegistry.zip ./dirRegistry

echo "  General Repository"
rm -f  $EXPORT_PATH/dirGeneralRepository.zip
zip -rq $EXPORT_PATH/dirGeneralRepository.zip ./dirGeneralRepository

echo "  Assault Party"
rm -f  $EXPORT_PATH/dirAssaultParty.zip
zip -rq $EXPORT_PATH/dirAssaultParty.zip ./dirAssaultParty

echo "  Concentration Site"
rm -f  $EXPORT_PATH/dirConcentrationSite.zip
zip -rq $EXPORT_PATH/dirConcentrationSite.zip ./dirConcentrationSite

echo "  Control Collection Site"
rm -f  $EXPORT_PATH/dirControlCollectionSite.zip
zip -rq $EXPORT_PATH/dirControlCollectionSite.zip ./dirControlCollectionSite

echo "  Museum"
rm -f  $EXPORT_PATH/dirMuseum.zip
zip -rq $EXPORT_PATH/dirMuseum.zip ./dirMuseum

echo "  Client"
rm -f  $EXPORT_PATH/dirClient.zip
zip -rq $EXPORT_PATH/dirClient.zip ./dirClient
