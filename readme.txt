In project root:
================
	mkdir lib

Download JDBC JAR:
==================
	wget -P lib http://central.maven.org/maven2/org/apache/derby/derby/10.11.1.1/derby-10.11.1.1.jar

	** OR **

	wget -P lib http://central.maven.org/maven2/org/xerial/sqlite-jdbc/3.8.7/sqlite-jdbc-3.8.7.jar

Compile:
==================
  cp src/gui/*.jpg output/gui/
  cp src/gui/*.png output/gui/
  javac -d output src/base/*.java src/controllers/*.java src/exceptions/*.java src/mock/*.java src/simulation/*.java src/callbacks/*.java src/data/*.java src/data/impl/*.java src/gui/*.java src/PlanetSim/*.java

Run:
==================
  java -cp lib/derby-10.11.1.1.jar:output/ PlanetSim.Demo