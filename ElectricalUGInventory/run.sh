#!/bin/bash
set -e
mkdir -p out
javac -d out src/com/electrical/inventory/*.java
echo "Compiled. Running application..."
java -cp out com.electrical.inventory.Main