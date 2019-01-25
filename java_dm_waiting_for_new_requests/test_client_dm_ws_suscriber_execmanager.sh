#!/bin/bash
#Author: J.M. Montañana 2019

#1-first we COMPILE the Java client files
	if [ -e demo_websocket_execmanager ]; then
		if [ -e demo_websocket_execmanager/dm_ws_susc_execmanager.class ]; then
		rm  demo_websocket_execmanager/dm_ws_susc_execmanager.class;
		fi;
	fi;
	if [ ! -d demo_websocket_execmanager ]; then
		mkdir demo_websocket_execmanager;
	fi;
	
	javac -classpath org.json.jar:java_websocket.jar:. dm_ws_susc_execmanager.java -d .;
	java -classpath org.json.jar:apache-httpcomponents-httpcore.jar:java_websocket.jar:.  demo_websocket_execmanager/dm_ws_susc_execmanager
	
