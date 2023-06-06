CODEBASE="file:///home/"$1"/test/HeistToTheMuseum/dirMuseum/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistToTheMuseumMuseum 22185 localhost 22180
