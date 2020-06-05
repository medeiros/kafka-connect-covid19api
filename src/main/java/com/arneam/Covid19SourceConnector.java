package com.arneam;

import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import com.github.jcustenborder.kafka.connect.utils.config.Description;
import com.github.jcustenborder.kafka.connect.utils.config.DocumentationImportant;
import com.github.jcustenborder.kafka.connect.utils.config.DocumentationNote;
import com.github.jcustenborder.kafka.connect.utils.config.DocumentationTip;
import com.github.jcustenborder.kafka.connect.utils.config.Title;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceConnector;

@Description("Connect to Covid19API to get Covid19 data of all countries in the world")
@DocumentationImportant("")
@DocumentationTip("")
@Title("Kafka-Connect-Covid19API")
@DocumentationNote("Note: use it in conjunction with 'kafka-streams-covid19api' application")
public class Covid19SourceConnector extends SourceConnector {

  private Covid19SourceConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.version(this.getClass());
  }

  @Override
  public void start(Map<String, String> map) {
    this.config = new Covid19SourceConnectorConfig(map);
  }

  @Override
  public Class<? extends Task> taskClass() {
    return Covid19SourceTask.class;
  }

  @Override
  public List<Map<String, String>> taskConfigs(int i) {
    List<Map<String, String>> configs = new ArrayList<>(1);
    configs.add(config.originalsStrings());
    return configs;
  }

  @Override
  public void stop() {

  }

  @Override
  public ConfigDef config() {
    return Covid19SourceConnectorConfig.config();
  }

}
