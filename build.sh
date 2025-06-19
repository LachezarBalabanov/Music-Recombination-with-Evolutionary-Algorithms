#!/bin/bash

clear

set classpath=

javac.exe -d ./bin ./src/eu/veldsoft/mididermi/base/*.java -classpath ./bin
javac.exe -d ./bin ./src/eu/veldsoft/mididermi/common/*.java -classpath ./bin
javac.exe -d ./bin ./src/eu/veldsoft/mididermi/database/*.java -classpath ./bin
javac.exe -d ./bin ./src/eu/veldsoft/mididermi/providers/*.java -classpath ./bin
javac.exe -d ./bin ./src/eu/veldsoft/mididermi/server/*.java -classpath ./bin
javac.exe -d ./bin ./src/eu/veldsoft/mididermi/client/*.java -classpath ./bin

cpy ./src/client.html ./bin/client.html
cpy ./src/database.properties ./bin/database.properties

cd ./bin

rmiregistry

echo RMI registry started ...

java -cp .;../lib/postgresql-8.3-603.jdbc4.jar -Djava.rmi.server.codebase=file:/tmp/ -Djava.rmi.server.hostname=127.0.0.1 -Djava.security.policy=../wideopen.policy eu.veldsoft.mididermi.server.MIDIDERMIServer -MINPOOL 2 -MAXPOOL 30 -MINEPOCHS 1 -MAXEPOCHS 5 -LR 1 -LT 1 -LD -LF -SD -SF

echo RMI server application registered ...

pause

appletviewer -J-Djava.rmi.server.codebase=http://127.0.0.1/ -J-Djava.security.policy=../wideopen.policy client.html

echo RMI clinet application(s) started ...

pause

cd ./eu/veldsoft/mididermi/client
rm *.class
cd ..
rmdir client

cd server
rm *.class
cd ..
rmdir server

cd common
rm *.class
cd ..
rmdir common

cd providers
rm *.class
cd ..
rmdir providers

cd database
rm *.class
cd ..
rmdir database

cd base
rm *.class
cd ..
rmdir base

cd ..
rmdir mididermi
cd ..
rmdir veldsoft
cd ..
rmdir eu
rmclient.html
rmdatabase.properties
cd ..

echo Binary directory clean ...
