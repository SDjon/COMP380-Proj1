## Dependencies
Make sure you have **Java installed** (Java 8+).  
To check your Java version, run:
java -version

## Usage
Run the following commands in your terminal.

1. Compile java files:
javac *.java

2. Create a file called MANIFEST.MF containing:
Main-Class: Proj1
\n

3. Package into a JAR
jar cfm MyApp.jar MANIFEST.MF *.class

4. Run the executable JAR
java -jar MyApp.jar
