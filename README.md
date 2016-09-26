# saturnus
A discrete event simulator for parallel file systems.

# Installing the DESMO-J library
We have to download the file called [desmoj-2.5.1c-bin.jar]
(https://sourceforge.net/projects/desmoj/files/desmoj/2.5.1c/desmoj-2.5.1c-bin.jar/download) from the
[DESMO-J website] (http://desmoj.sourceforge.net/home.html).

## Installing to our local maven repository
We should issue the corresponding maven command, similar to that:
```
mvn install:install-file -Dfile=C:\Users\youruser\Downloads\desmoj-2.5.1c-bin.jar
-DgroupId=desmoj -DartifactId=desmoj -Dversion=2.5.1 -Dpackaging=jar  
```  
To install the library correctly, referenced in our pom.xml.
Change the ```-Dfile``` parameter accordingly to your filesystem.
