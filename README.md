# kafka-connect-covid19api

This is a Kafka Source Connector for [Covid19 API](https://api.covid19api.com/summary). 

Created with [jcustenborder Maven archetype](https://github.com/jcustenborder/kafka-connect-archtype).


# Usage

Use the following JSON document to register the connector into the Kafka Cluster: 

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
curl -X POST -d@'<file.json>' -H "Content-type: application/json" localhost:8084
```
