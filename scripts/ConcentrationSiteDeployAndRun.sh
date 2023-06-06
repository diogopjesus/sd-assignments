echo "Transfering data to the concentration site node."
sshpass -f password ssh sd108@l040101-ws09.ua.pt 'mkdir -p test/HeistToTheMuseum'
sshpass -f password ssh sd108@l040101-ws09.ua.pt 'rm -rf test/HeistToTheMuseum/*'
sshpass -f password scp dirConcentrationSite.zip sd108@l040101-ws09.ua.pt:test/HeistToTheMuseum

echo "Decompressing data sent to the concentration site node."
sshpass -f password ssh sd108@l040101-ws09.ua.pt 'cd test/HeistToTheMuseum ; unzip -uq dirConcentrationSite.zip'

echo "Executing program at the concentration site node."
sshpass -f password ssh sd108@l040101-ws09.ua.pt 'cd test/HeistToTheMuseum/dirConcentrationSite ; ./concsite_com_d.sh sd108'
