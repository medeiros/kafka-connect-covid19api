package com.arneam;

import com.github.jcustenborder.kafka.connect.utils.config.ConfigKeyBuilder;
import java.util.Map;
import org.apache.kafka.common.config.AbstractConfig;
import org.apache.kafka.common.config.ConfigDef;

public class Covid19SourceConnectorConfig extends AbstractConfig {

  public static final String TOPIC_CONFIG = "covid19.data";
  private static final String TOPIC_DOC = "Topic to sore Covid19 API data";

  public final String topic;

  public Covid19SourceConnectorConfig(Map<?, ?> originals) {
    super(config(), originals);
    this.topic = this.getString(TOPIC_CONFIG);
  }

  public static ConfigDef config() {
    return new ConfigDef().define(ConfigKeyBuilder
        .of(TOPIC_CONFIG, ConfigDef.Type.STRING).documentation(TOPIC_DOC)
        .importance(ConfigDef.Importance.HIGH).build());
  }

}
