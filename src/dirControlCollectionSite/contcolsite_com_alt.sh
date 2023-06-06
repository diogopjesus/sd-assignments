CODEBASE="file:///home/"$1"/test/HeistToTheMuseum/dirControlCollectionSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistToTheMuseumControlCollectionSite 22184 localhost 22180
