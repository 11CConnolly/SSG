# Security Suite Generator

Automatic Test Suite Generator used to investigate Automatic Software Repair of Security Vulnerabilities in SECDefects. This was presented as the third year dissertation for my BSc Computer Science at the University of Southampton and recieved First Class honors.

## Instructions
To build, use `mvn clean package -DskipTests=true`

Once that has been done, you can then run the jar file to build for the files you want.

To run, use `java -jar target/jparser-0.1.0-SNAPSHOT.jar <file location>.java <defect type>`

#### Example Usage

Two example files have been included for demonstrative: ExampleInteger.java and ExampleString.java.

To run SSG on ExampleInteger.java you can use:

`java -jar target/jparser-0.1.0-SNAPSHOT.jar src\main\java\com\example\ExampleInteger.java INTEGER_ATTACK`

To run SSG on ExampleString.java generating SQL Injection tests

`java -jar target/jparser-0.1.0-SNAPSHOT.jar src\main\java\com\example\ExampleInteger.java STRING_SQL_INJECTION`

To run SSG on ExampleString.java generating Path Traversal attack tests

`java -jar target/jparser-0.1.0-SNAPSHOT.jar src\main\java\com\example\ExampleInteger.java STRING_PATH_TRAVERSAL`

## Acknowledgements
Project utlizes JavaParser and JavaPoet. Fuzzing lists used are adapted from 1N3@Crowshields lists.
