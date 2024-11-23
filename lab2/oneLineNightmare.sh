#works only on helios because of key -e in echo command
javac -d build/classes -cp external/Pokemon.jar -sourcepath src src/Main.java;echo -e 'Main-Class: Main\nClass-Path: external/Pokemon.jar\n' > build/MANIFEST.MF;jar cfm bob.jar build/MANIFEST.MF -C build/classes .; java -jar bob.jar
