package com.arneam;

import com.arneam.model.Country;
import com.github.jcustenborder.kafka.connect.utils.VersionUtil;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Covid19SourceTask extends SourceTask {

  private static Logger log = LoggerFactory.getLogger(Covid19SourceTask.class);
  private static String COVID19API_ENDPOINT = "https://api.covid19api.com/summary";
  private Covid19SourceConnectorConfig config;

  @Override
  public String version() {
    return VersionUtil.version(this.getClass());
  }

  @Override
  public void start(Map<String, String> map) {
    config = new Covid19SourceConnectorConfig(map);
  }

  @Override
  public List<SourceRecord> poll() throws InterruptedException {
    List<SourceRecord> records = new ArrayList<>();

    JSONArray countries = getCovid19APICountries();

    for (Object o : countries) {
      Country country = Country.fromJson((JSONObject) o);
      SourceRecord record = generateSourceRecordFrom(country);
      records.add(record);
    }

    return records;
  }

  JSONArray getCovid19APICountries() {
    GetRequest request = Unirest.get(COVID19API_ENDPOINT);
    HttpResponse<JsonNode> jsonResponse;
    try {
      jsonResponse = request.asJson();
      return jsonResponse.getBody().getObject().getJSONArray("Countries");
    } catch (UnirestException e) {
      return new JSONArray();
    }
  }

  private SourceRecord generateSourceRecordFrom(Country country) {
    return new SourceRecord(sourcePartition(), sourceOffset(country.getDate()), config.topic, null,
        Covid19Schema.KEY_SCHEMA, buildRecordKey(country), Covid19Schema.VALUE_SCHEMA,
        buildRecordValue(country), country.getDate().toEpochMilli());
  }

  private Map<String, String> sourcePartition() {
    Map<String, String> map = new HashMap<>();
    map.put("partition", "single");
    return map;
  }

  private Map<String, String> sourceOffset(Instant date) {
    Map<String, String> map = new HashMap<>();
    map.put(Covid19Schema.DATE_FIELD, date.toString());
    return map;
  }

  private Struct buildRecordKey(Country country) {
    return new Struct(Covid19Schema.KEY_SCHEMA)
        .put(Covid19Schema.COUNTRY_CODE_FIELD, country.getCountryCode());
  }

  private Struct buildRecordValue(Country country) {
    return new Struct(Covid19Schema.VALUE_SCHEMA)
        .put(Covid19Schema.COUNTRY_FIELD, country.getCountry())
        .put(Covid19Schema.SLUG_FIELD, country.getSlug())
        .put(Covid19Schema.NEW_CONFIRMED_FIELD, country.getNewConfirmed())
        .put(Covid19Schema.TOTAL_CONFIRMED_FIELD, country.getTotalConfirmed())
        .put(Covid19Schema.NEW_DEATHS_FIELD, country.getNewDeaths())
        .put(Covid19Schema.TOTAL_DEATHS_FIELD, country.getTotalDeaths())
        .put(Covid19Schema.NEW_RECOVERED_FIELD, country.getNewRecovered())
        .put(Covid19Schema.TOTAL_RECOVERED_FIELD, country.getTotalRecovered())
        .put(Covid19Schema.DATE_FIELD, Date.from(country.getDate()));
  }

  HttpResponse<JsonNode> getData() throws UnirestException {
    GetRequest request = Unirest.get(COVID19API_ENDPOINT);
    return request.asJson();
  }

  @Override
  public void stop() {

  }
}
