echo 'Removing .class files'
rm -rf */*.class */*/*.class */*/*/*.class

echo 'Removing .zip files'
rm -rf dirAssaultParty.zip dirConcentrationSite.zip dirControlCollectionSite.zip dirGeneralRepository.zip dirMuseum.zip dirClient.zip dirRegistry.zip dirRMIRegistry.zip

echo 'Removing doc/ directory'
rm -rf doc/

echo 'Removing stat file'
rm -rf stat
