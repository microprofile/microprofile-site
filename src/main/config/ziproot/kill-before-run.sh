#!/bin/bash

cd /var/app/current
/usr/bin/killall -w -I -u webapp java
/usr/bin/java -Xmx768m -jar ${project.artifactId}-${project.version}-exec.jar 
