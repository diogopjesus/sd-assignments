echo "Transfering data to the general repository node."
sshpass -f password ssh sd108@l040101-ws07.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd108@l040101-ws07.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirGeneralRepository.zip sd108@l040101-ws07.ua.pt:test/HeistToTheMuseum

echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd108@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirGeneralRepository.zip'

echo "Executing program at the general repository node."
sshpass -f password ssh sd108@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum/dirGeneralRepository ; ./repos_com_d.sh sd108'

echo "Server shutdown."
sshpass -f password ssh sd108@l040101-ws07.ua.pt 'cd test/HeistToTheMuseum/dirGeneralRepository ; less stat'
