# TDT4145 Project

This repository contains the Java implementation of the TDT4145 Database project.

## Running the application

After compiling the application as a jar file, it can be executed using
```bash
$ java -jar TDT4145Project.jar
```

You can also specify the database credentials by doing
```bash
$ java -jar TDT4145Project.jar <url> <username> <password>
```

Example:
```bash
$ java -jar TDT4145Project.jar jdbc:mysql://localhost/database root 123
```

You may also authenticate using the `dbconnect` command, or use enviornment variables (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).
