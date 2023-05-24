CODEBASE="file:///home/"$1"/test/HeistToTheMuseum/dirAssaultParty/"
PORT=$((22180 + $2))
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistToTheMuseumAssaultParty $2 $PORT localhost 22180
