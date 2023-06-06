echo "Transfering data to the control collection site node."
sshpass -f password ssh sd108@l040101-ws05.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd108@l040101-ws05.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirControlCollectionSite.zip sd108@l040101-ws05.ua.pt:test/HeistToTheMuseum

echo "Decompressing data sent to the control collection site node."
sshpass -f password ssh sd108@l040101-ws05.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirControlCollectionSite.zip'

echo "Executing program at the control collection site node."
sshpass -f password ssh sd108@l040101-ws05.ua.pt 'cd test/HeistToTheMuseum/dirControlCollectionSite ; ./contcolsite_com_d.sh sd108'
