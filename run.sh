javac -cp .:$(pwd)/lib/genclass.jar */src/*/*.java

for i in $(seq 1 100)
do
echo -e "\nRun n.o " $i
java -cp .:$(pwd)/lib/genclass.jar src.main.HeistToTheMuseum
done