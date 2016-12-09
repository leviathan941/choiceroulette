Choice Roulette
==============
Roulette GUI for random choosing an option from the specified values.

How to build
----------------------------
### Requirements ###

You need to have the following to compile the project:
* [Java 1.8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
* JavaFX 8. It is included to Oracle JDK 8, for OpenJDK you have to install it separately, for example from [here](http://openjdk.java.net/projects/openjfx/).
* [SBT](http://www.scala-sbt.org/) (v. 0.13 or newer).

### Compile and Run using SBT ###

* Open a terminal and go to the project source directory.
* Specify `JAVA_HOME` environment variable.
* Type `sbt "project guiApp" run` to compile and launch the application.
* Type `sbt "project guiApp" assembly` to create _fat_ JAR.

License
-------
Copyright (C) 2016 Alexey Kuzin. All rights reserved.

This project is governed by the Apache 2.0 license. For details see the file
titled LICENSE in the project root folder.
