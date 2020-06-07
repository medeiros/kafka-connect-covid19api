package com.arneam;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.source.SourceRecord;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This test can be used for integration testing with the system you are integrating with. For example
 * take a look at https://github.com/jcustenborder/docker-compose-junit-extension to launch docker
 * containers for your testing.
 */
public class Covid19SourceTaskIT {

  public static final int EXPECTED_TOTAL_COUNTRIES = 186;
  public static final String TOPIC_NAME = "covid-input";
  Covid19SourceTask task;

  @BeforeEach
  void init() {
    this.task = new Covid19SourceTask();
    Map<String, String> map = new HashMap<>();
    map.put(Covid19SourceConnectorConfig.TOPIC_CONFIG, TOPIC_NAME);
    this.task.start(map);
  }

  @Test
  void shouldPollProperly() throws InterruptedException {
    List<SourceRecord> records = task.poll();
    assertNotNull(records);
    assertThat(records.size(), is(equalTo(EXPECTED_TOTAL_COUNTRIES)));
  }

}