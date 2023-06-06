source .env

sshpass -f .password scp $LOGIN@$NODE07:test/HeistToTheMuseum/dirGeneralRepository/stat .

echo "Info: File 'stat' copied from server, located at $(pwd)"
