PREVIOUS_DIR=$(pwd)

# clear file generated by previous runs
if [ "$1" = "clean" ]; then
    rm -r **/*.class 2> /dev/null
    rm -r logs/ 2> /dev/null
    exit 0;
fi

# check if script is being run at root of project
cd $(pwd)/src/ 2> /dev/null
if [ "$?" -ne 0 ]; then
    echo "Please run script at root of project!"
    cd $PREVIOUS_DIR
    exit 1;
fi

# compile project
javac -cp .:$PREVIOUS_DIR/lib/genclass.jar **/*.java
if [ "$?" -ne 0 ]; then
    echo "Something went wrong! Make sure you are at the root of the project"
    exit 1;
fi


# check if argument is a number
re='^[0-9]+$'
if ! [[ $1 =~ $re || "$1" =~ "" ]] ; then
   echo "Error: Argument passed is not a number" >&2; exit 1
fi

# run project once
if [[ "$1" = 1 || "$1" = "" ]]; then
    java -cp .:$PREVIOUS_DIR/lib/genclass.jar main.HeistToTheMuseum
    exit $?
fi

# run project multiple times
rm -rf ../logs/
mkdir ../logs/
for i in $(seq 1 $1)
do
    echo -e "Run n.o " $i
    echo "../logs/run$i.log" | java -cp .:$PREVIOUS_DIR/lib/genclass.jar main.HeistToTheMuseum > /dev/null
    if [ "$?" -ne 0 ]; then
        exit 1;
    fi
done
