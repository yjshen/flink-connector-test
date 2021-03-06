A Pulsar Flink connector example that write typed data to Pulsar and read it out.

This example contains two flink streaming jobs,
`StreamWrite` to populate data into Pulsar and `StreamRead` to read data out in a streaming fashion.

The steps to run the example:

1. Start Pulsar in Docker.
    
    ```bash
    docker run -it \
      -p 6650:6650 \
      -p 8080:8080 \
      -v $PWD/data:/pulsar/data \
      apachepulsar/pulsar:2.4.0 \
      bin/pulsar standalone
    ```

2. Start Flink locally.
    
    You can follow the [instructions](https://ci.apache.org/projects/flink/flink-docs-release-1.9/getting-started/tutorials/local_setup.html) to download and start Flink.
    
    ```bash
    $ ./bin/start-cluster.sh
    ```

3. Build the example.

    ```bash
    $ mvn clean package -DskipTests
    ```
   
4. Run `StreamWrite` to produce NASA data to Pulsar topic _TOPIC_NAME_.

    ```bash
    $ ./bin/flink run -c com.example.StreamWrite ${Example_project}/target/flink-connector-test-1.0-SNAPSHOT.jar [TOPIC_NAME]
    ```
   
5. Run `StreamRead` to read NASA data out from _TOPIC_NAME_.

    ```bash
    $ ./bin/flink run -c com.exmple.StreamRead ${Example_project}/target/flink-connector-test-1.0-SNAPSHOT.jar [TOPIC_NAME]
    ```
   
   You will see sample output for above application as follows:
   
    ```
    1,Mercury program,1959
    2,Apollo program,1961
    3,Gemini program,1963
    4,Skylab,1973
    5,Apollo–Soyuz Test Project,1975 
    ```