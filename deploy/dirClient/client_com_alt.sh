CODEBASE="file:///home/"$1"/test/HeistToTheMuseum/dirClient/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientHeistToTheMuseum localhost 22180 stat
