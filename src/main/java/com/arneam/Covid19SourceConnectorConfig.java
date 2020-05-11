package com.arneam;

import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.config.ConfigDef.Type;

public class Covid19SourceConnectorConfig extends AbstractConfig {

  public static final String TOPIC_CONFIG = "topic";
  private static final String TOPIC_DOC = "Topic to store Covid19 API data";

  public static final String POLL_INTERVAL_MS_CONFIG = "poll.interval.ms";
  private static final String POLL_INTERVAL_MS_DOC = "Time between two calls to Covid19API";

  public final String topic;
  public final Long pollIntervalMs;

  public Covid19SourceConnectorConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.topic = this.getString(TOPIC_CONFIG);
    this.pollIntervalMs = this.getLong(POLL_INTERVAL_MS_CONFIG);
  }

  public static ConfigDef config() {
    return new ConfigDef().define(ConfigKeyBuilder
        .of(TOPIC_CONFIG, ConfigDef.Type.STRING).documentation(TOPIC_DOC)
        .importance(ConfigDef.Importance.HIGH).build()).define(ConfigKeyBuilder
        .of(POLL_INTERVAL_MS_CONFIG, Type.LONG).defaultValue((long) (24 * 60 * 60 * 1000))
        .documentation(POLL_INTERVAL_MS_DOC)
        .importance(ConfigDef.Importance.HIGH).build());
  }

}
