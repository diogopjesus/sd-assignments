PREVIOUS_DIR=$(pwd)

cd $(pwd)/src/ 2>/dev/null
if [ "$?" -ne 0 ]; then
    echo "Please run script at root of project!"
    cd $PREVIOUS_DIR
    exit 1;
fi

#
if [ "$1" = "clean" ]; then
    rm -r **/*.class
    exit 0;
fi

# compile project
javac -cp .:$PREVIOUS_DIR/lib/genclass.jar **/*.java # 2>/dev/null
if [ "$?" -ne 0 ]; then
    echo "Something went wrong! Make sure you are at the root of the project"
    exit 1;
fi


# run project
NUMBER_OF_RUNS=100
re='^[0-9]+$'
if ! [[ $1 =~ $re ]] ; then
   echo "Error: Argument passed is not a number" >&2; exit 1
fi

if [ "$1" -eq 1 ]; then
    java -cp .:$PREVIOUS_DIR/lib/genclass.jar main.HeistToTheMuseum
    exit $?
fi

rm -rf ../logs/
mkdir ../logs/
for i in $(seq 1 100)
do
    echo -e "Run n.o " $i
    echo "../logs/run$i.log" | java -cp .:$PREVIOUS_DIR/lib/genclass.jar main.HeistToTheMuseum > /dev/null
    if [ "$?" -ne 0 ]; then
        exit 1;
    fi
done
