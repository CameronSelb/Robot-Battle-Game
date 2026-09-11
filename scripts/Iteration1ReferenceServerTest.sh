#!/bin/bash


echo "Starting compile"
mvn compile


java -jar libs/reference-server-0.1.0.jar -p 5000 -s 1 &
PID1=$!

#java -jar libs/reference-server-0.1.0.jar -p 4500 -s 2 &
PID2=$!

#java -jar libs/reference-server-0.1.0.jar -p 4000 -s 2 -o 0,1 &
PID3=$!

sleep 5

echo "Testing against reference server"
mvn test -Dtest="za.co.wethinkcode.robots.acceptancetest.*Test"
echo "Closing reference servers"
kill $PID1
kill $PID2
kill $PID3

sleep 2
if ps -p $PID1 > /dev/null; then
    echo "Java program (PID1: $PID1) did not terminate gracefully. Forcefully killing..."
    kill -9 $PID1
fi

if ps -p $PID2 > /dev/null; then
    echo "Java program (PID2: $PID2) did not terminate gracefully. Forcefully killing..."
    kill -9 $PID2
fi

if ps -p $PID3 > /dev/null; then
    echo "Java program (PID: $PID3) did not terminate gracefully. Forcefully killing..."
    kill -9 $PID3
fi

echo "script finished"
