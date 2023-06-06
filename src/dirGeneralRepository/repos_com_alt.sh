CODEBASE="file:///home/"$1"/test/HeistToTheMuseum/dirGeneralRepository/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistToTheMuseumGeneralRepository 22186 localhost 22180
