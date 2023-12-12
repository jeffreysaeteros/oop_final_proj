# oop_final_proj

A simple playlist system where you will be able to create, view, or delete playlists. Within these playlists, you will be able to add or delete songs.

## Requirements

- Java 17.0.9
- Maven 3.9.6

## Dependencies and Downloads

- Requires JDK / Java Version: 17.0.9
- Requires Maven:
  - Download [here](https://maven.apache.org/download.cgi?.=)
  - MacOS:
    - Download ```apache-maven-3.9.x-bin.zip```
    - Add to it to your PATH:
      - Go to your `./zshrc` file by doing: ```vim ~/.zshrc```
      - Paste```export PATH=/your/path/to/apache-maven-3.9.x/bin:$PATH``` in that file
        - replace `apache-maven-3.9.x` with your version number
        - `/your/path/to` signifies where you have the `apache-maven-3.9.x` folder on your computer
    - Run ```mvn --version``` to confirm it is downloaded correctly

- Go into `/With_Maven` directory: ```cd With_Maven```

- Run ```mvn clean```
- Finally, run.

```sh
mvn compile exec:java -Dexec.mainClass="org.example.MainGUI" -Dmongodb.uri="mongodb+srv://forGrader:grader@cluster0.e6hjphf.mongodb.net/?retryWrites=true&w=majority" -e
```
