cd /Users/zhailz/Documents/workspace/sideproject/begine/
mvn clean package
cd /Users/zhailz/Documents/workspace/sideproject/begine/target
scp -P 27298 begine-0.0.1-SNAPSHOT.war root@45.78.3.205:/data/begine
ssh root@45.78.3.205 -p 27298 "cd /data/begine/;./run.sh;exit";


