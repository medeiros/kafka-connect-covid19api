# kafka-connect-covid19api

This is a Kafka Source Connector for [Covid19 API](https://api.covid19api.com/summary). 

Created with [jcustenborder Maven archetype](https://github.com/jcustenborder/kafka-connect-archtype).


# Usage

Use the following JSON document to register the connector in Kafka Cluster: 

```json
{
	"name": "kafka-connect-covid19api", 
	"config": {
		"connector.class": "com.arneam.Covid19SourceConnector",
		"topic": "covid-input"
	}
}
```

The cURL command to register the JSON (as a file) follows:

```bash
curl -X POST -d@'<file.json>' -H "Content-type: application/json" localhost:8084/connectors | jq
```

## Using Confluence Schema Registry

No code change is required in this connector in order to adopt Confluent Schema Registry/Avro. 
That's because the compatibility between Connect Schemas and Confluent Schema Registry configuration can be 
set at Worker level; it would also be set at connector level, but for this solution the Worker level would be 
ideal ([documentation](https://docs.confluent.io/current/schema-registry/connect.html)).

In order to perform this configuration, one must, in the Confluent Kafka Distribution, add the following 
lines into `/etc/kafka/connect-distributed.properties` file:

```
key.converter=io.confluent.connect.avro.AvroConverter
key.converter.schema.registry.url=http://localhost:8081
key.converter.enhanced.avro.schema.support=true
value.converter=io.confluent.connect.avro.AvroConverter
value.converter.schema.registry.url=http://localhost:8081
value.converter.enhanced.avro.schema.support=true
``` 

Additionally, it is also necessary Schema Registry is up and running at port 8081 (default):

```
$ schema-registry-start -daemon ~/<confluent distro relat path>/etc/schema-registry/schema-registry.properties
```

Considering that Schema Registry is running in daemon mode, it would be nice to set its output to specific log
directory location, in order to allow further troubleshooting by file analysis. This can be done by setting the 
following environment variable:

```
$ vim ~/.bashrc
export LOG_DIR=<path to kafka logs directory of choosing>
<ESC>:wq
```

After that, all Kafka components (core, zookeeper, schema registry, connectors, etc) will start to log data as 
files in this directory.

## Appendix: Twitter registration

This is a source connector for "Brazil ranking of Covid19" solution (plus [kafka-streams-covid19api](https://github.com/medeiros/kafka-streams-covid19api)). 
In order to make the solution complete, it is required that some Twitter sink connector is adopted as well.

For the sake of reference, the following JSON document would be used to register the [Eneco Twitter Connector](https://github.com/Eneco/kafka-connect-twitter) in Kafka Cluster: 

```json
{
  "name": "twitter-sink",
  "config": {
      "connector.class": "com.eneco.trading.kafka.connect.twitter.TwitterSinkConnector",
      "tasks.max": "1",
      "topics": "covid-output",
      "twitter.consumerkey": "<CONSUMER_KEY>",
      "twitter.consumersecret": "<CONSUMER_SECRET>",
      "twitter.token": "<TOKEN>",
      "twitter.secret": "<SECRET>",
      "output.format": "string",
      "key.converter": "org.apache.kafka.connect.storage.StringConverter",
      "value.converter": "org.apache.kafka.connect.storage.StringConverter"
  }
}
```

