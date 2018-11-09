#!/bin/bash
clear
Rscript RserverScript.R
mvn -U clean install
mvn clean compile
mvn package
mvn exec:java