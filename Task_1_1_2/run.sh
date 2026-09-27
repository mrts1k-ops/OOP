#!/bin/bash

set -e # Останавливаем скрипт при первой ошибке

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
javac -encoding UTF-8 -d out @sources.txt

echo "Documentation generation"
javadoc -encoding UTF-8 -docencoding UTF-8 -charset UTF-8 -d docs -quiet @sources.txt
rm sources.txt

echo "Creating a JAR file"
jar cfe app.jar "$MAIN_CLASS" -C out .

echo "Launch"
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -jar app.jar
