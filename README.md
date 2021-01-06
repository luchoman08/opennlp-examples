# Previous steps
Prerequisites: you should have maven installed in your system
and java runtime 
<br>
Downlad the spanish ner recognition model with the command
```bash
wget http://opennlp.sourceforge.net/models-1.5/es-ner-person.bin
```

Write your input in the file `raw_phrases.txt`, one phrase per line

Build the project
```bash
mvn package
```

Run the app

```bash
mvn exec:java -Dexec.mainClass="com.truora.comprehend.App"
```

The output will be in `output.csv` file

