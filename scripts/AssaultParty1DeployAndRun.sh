echo "Transfering data to the assault party 1 node."
sshpass -f password ssh sd108@l040101-ws02.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd108@l040101-ws02.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirAssaultParty.zip sd108@l040101-ws02.ua.pt:test/HeistToTheMuseum

echo "Decompressing data sent to the assault party 1 node."
sshpass -f password ssh sd108@l040101-ws02.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirAssaultParty.zip'

echo "Executing program at the assault party 1 node."
sshpass -f password ssh sd108@l040101-ws02.ua.pt 'cd test/HeistToTheMuseum/dirAssaultParty ; ./asspart_com_d.sh sd108 1'
