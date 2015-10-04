#!bin/bash
sudo su cloudera
hadoop fs -ls /user/cloudera
hadoop fs -mkdir /user/cloudera/project01
hadoop fs -mkdir /user/cloudera/project01/input
hadoop fs -put /home/cloudera/workspace/project01/input.txt /user/cloudera/project01/input

javac -cp '/usr/lib/hadoop/client/*' -sourcepath src -d build src/project/part1/pairs/*.java
javac -cp '/usr/lib/hadoop/client/*' -sourcepath src -d build src/project/part2/stripes/*.java
javac -cp '/usr/lib/hadoop/client/*' -sourcepath src -d build src/project/part3/hybrid/*.java
jar -cvf project.jar -C build/ .

hadoop jar project.jar project.part1.pairs.Driver1 /user/cloudera/project01/input /user/cloudera/project01/output
hadoop fs -cat /user/cloudera/project01/output/*
hadoop fs -rm -r /user/cloudera/project01/output
