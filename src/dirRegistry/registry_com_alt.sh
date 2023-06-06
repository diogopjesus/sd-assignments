CODEBASE="file:///home/"$1"/test/HeistToTheMuseum/dirRegistry/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerRegisterRemoteObject 22181 localhost 22180
