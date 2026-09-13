#!/bin/bash

set -e # Shell-скрип.

MAIN_CLASS="ru.nsu.berdyugin.Main"
SRC_PATH="src/main/java"

echo "Cleaning up old files"
rm -rf out
rm -rf docs
rm -f app.jar

echo "Creating a folder for compiled files"
mkdir -p out

echo "Compilation"
find "$SRC_PATH" -name "*.java" > sources.txt
javac -d out @sources.txt

echo "Documentation generation"
javadoc -d docs -quiet @sources.txt
rm sources.txt

echo "Creating a JAR file"
jar cfe app.jar "$MAIN_CLASS" -C out .

echo "Launch"
java -jar app.jar