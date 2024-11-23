#!/usr/bin/bash
javac -d build/classes -cp lib/Pokemon.jar -sourcepath src src/code/Main.java
echo "Main-Class: code.Main\nClass-Path: lib/Pokemon.jar\n" > build/MANIFEST.MF
jar cfm pok.jar build/MANIFEST.MF -C build/classes .
