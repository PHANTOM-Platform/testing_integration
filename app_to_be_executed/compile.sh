#!/bin/bash

make clean;
if [ ! -d bin ]; then mkdir bin ; fi;

#!! please update with the correct path where you have the Mf_api
cp ../Monitoring_client/src/util/src/mf_util.h src;
cp ../Monitoring_client/src/api/src/mf_api.h src;
cp ../Monitoring_client/src/publisher/src/publisher.h src;
cp ../Monitoring_client/src/parser/src/mf_parser.h src;
cp ../Monitoring_client/src/core/mf_debug.h src;

#The compilation process:
make VERBOSE=1 --makefile=./Makefile
