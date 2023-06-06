sshpass -f password ssh sd108@l040101-ws02.ua.pt 'fuser -k 22181/tcp' # kill assault party 1
sshpass -f password ssh sd108@l040101-ws03.ua.pt 'fuser -k 22182/tcp' # kill assault party 2
sshpass -f password ssh sd108@l040101-ws09.ua.pt 'fuser -k 22183/tcp' # kill concentration site
sshpass -f password ssh sd108@l040101-ws05.ua.pt 'fuser -k 22184/tcp' # kill control collection site
sshpass -f password ssh sd108@l040101-ws06.ua.pt 'fuser -k 22185/tcp' # kill museum
sshpass -f password ssh sd108@l040101-ws07.ua.pt 'fuser -k 22186/tcp' # kill general repository
sshpass -f password ssh sd108@l040101-ws08.ua.pt 'fuser -k 22181/tcp' # kill registry
sshpass -f password ssh sd108@l040101-ws08.ua.pt 'fuser -k 22180/tcp' # kill RMIRegistry
