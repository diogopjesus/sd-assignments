source .env

ret=0

sshpass -f .password ssh $LOGIN@$NODE08 'fuser 22181/tcp' >& /dev/null # check registry
if [ $? == 0 ]; then
  echo "Registry is running."
else
  ret=1
fi

sshpass -f .password ssh $LOGIN@$NODE08 'fuser 22180/tcp' >& /dev/null # check RMIRegistry
if [ $? == 0 ]; then
  echo "RMI Registry is running."
else
  ret=$(($ret + 1))
fi

exit $ret
