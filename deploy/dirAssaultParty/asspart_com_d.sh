CODEBASE="http://l040101-ws08.ua.pt/"$1"/classes/"
PORT=$((22180 + $2))
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistToTheMuseumAssaultParty $2 $PORT l040101-ws08.ua.pt 22180
