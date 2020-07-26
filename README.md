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

