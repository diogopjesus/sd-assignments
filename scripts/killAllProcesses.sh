source .env

sshpass -f .password ssh $LOGIN@$NODE02 'fuser -k 22181/tcp' >& /dev/null # kill assault party 1
if [ $? == 0 ]; then
    echo "Assault Party 1 killed successfully."
fi
sshpass -f .password ssh $LOGIN@$NODE03 'fuser -k 22182/tcp' >& /dev/null # kill assault party 2
if [ $? == 0 ]; then
    echo "Assault Party 2 killed successfully."
fi

sshpass -f .password ssh $LOGIN@$NODE09 'fuser -k 22183/tcp' >& /dev/null # kill concentration site
if [ $? == 0 ]; then
    echo "Concentration Site killed successfully."
fi

sshpass -f .password ssh $LOGIN@$NODE05 'fuser -k 22184/tcp' >& /dev/null # kill control collection site
if [ $? == 0 ]; then
    echo "Control Collection Site killed successfully."
fi

sshpass -f .password ssh $LOGIN@$NODE06 'fuser -k 22185/tcp' >& /dev/null # kill museum
if [ $? == 0 ]; then
    echo "Museum killed successfully."
fi

sshpass -f .password ssh $LOGIN@$NODE07 'fuser -k 22186/tcp' >& /dev/null # kill general repository
if [ $? == 0 ]; then
    echo "General Repository killed successfully."
fi

sshpass -f .password ssh $LOGIN@$NODE08 'fuser -k 22181/tcp' >& /dev/null # kill registry
if [ $? == 0 ]; then
    echo "Registry killed successfully."
fi

sshpass -f .password ssh $LOGIN@$NODE08 'fuser -k 22180/tcp' >& /dev/null # kill RMIRegistry
if [ $? == 0 ]; then
    echo "RMI Registry killed successfully."
fi
