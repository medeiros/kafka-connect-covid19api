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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.kafka.connect.data.SchemaBuilder;
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
  private Long timestamp;

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
    log.info("-----> Calling poll method...");

    if (timestamp != null) {
      if ((Instant.now().toEpochMilli() - timestamp) < config.pollIntervalMs) {
        log.info("-----> poll is already called. going to sleep for {} ms till next execution",
            config.pollIntervalMs);
        Thread.sleep(config.pollIntervalMs);
        log.info("-----> poll wakened up");
      }
    }
    log.info("-----> Executing poll method...");

    List<SourceRecord> records = new ArrayList<>();

    JSONArray countries = getCovid19APICountries(1);
    log.info("-----> Total of countries: {}", countries.length());

    return recordToTweet(records, countries);

    //get back to this when kafka streams are working
    //return recordsFromCountries(records, countries);
  }

  private List<SourceRecord> recordToTweet(List<SourceRecord> records, JSONArray jsonCountries) {

    List<Country> countries = new ArrayList<>();
    for (Object o : jsonCountries) {
      countries.add(Country.fromJson((JSONObject) o));
    }

    int brazilRankingNewConfirmed = brazilRankindNewConfirmed(countries);
    int brazilRankingTotalConfirmed = brazilRankindTotalConfirmed(countries);
    int brazilRankingNewDeaths = brazilRankindNewDeaths(countries);
    int brazilRankingTotalDeaths = brazilRankindTotalDeaths(countries);
    int brazilRankingNewRecovered = brazilRankindNewRecovered(countries);
    int brazilRankingTotalRecovered = brazilRankindTotalRecovered(countries);

    String tweet = String
        .format("Ranking Covid19 do Brasil no mundo: %so. em novos casos confirmados; %so. em "
                + "total de casos confirmados; %so. em novas mortes; %so. em total de mortes; "
                + "%so. em novos casos recuperados; %so. em total de casos recuperados.",
            brazilRankingNewConfirmed, brazilRankingTotalConfirmed, brazilRankingNewDeaths,
            brazilRankingTotalDeaths, brazilRankingNewRecovered, brazilRankingTotalRecovered);

    log.info("-----> Tweet to send: '{}'", tweet);

    records.add(
        new SourceRecord(sourcePartition(), sourceOffset(null), config.topic,
            SchemaBuilder.string(), tweet));

    this.timestamp = Instant.now().toEpochMilli();

    log.info("-----> Timestamp: {}", timestamp);

    return records;

  }

  int brazilRankindNewConfirmed(List<Country> countries) {
    Collections.sort(countries,
        Collections.reverseOrder(Comparator.comparingInt(Country::getNewConfirmed)));
    Optional<Country> brazil = countries.stream()
        .filter(country -> country.getCountry().equals("Brazil")).findFirst();
    return countries.indexOf(brazil.get()) + 1;
  }

  int brazilRankindTotalConfirmed(List<Country> countries) {
    Collections.sort(countries,
        Collections.reverseOrder(Comparator.comparingInt(Country::getTotalConfirmed)));
    Optional<Country> brazil = countries.stream()
        .filter(country -> country.getCountry().equals("Brazil")).findFirst();
    return countries.indexOf(brazil.get()) + 1;
  }

  int brazilRankindNewDeaths(List<Country> countries) {
    Collections
        .sort(countries, Collections.reverseOrder(Comparator.comparingInt(Country::getNewDeaths)));
    Optional<Country> brazil = countries.stream()
        .filter(country -> country.getCountry().equals("Brazil")).findFirst();
    return countries.indexOf(brazil.get()) + 1;
  }

  int brazilRankindTotalDeaths(List<Country> countries) {
    Collections.sort(countries,
        Collections.reverseOrder(Comparator.comparingInt(Country::getTotalDeaths)));
    Optional<Country> brazil = countries.stream()
        .filter(country -> country.getCountry().equals("Brazil")).findFirst();
    return countries.indexOf(brazil.get()) + 1;
  }

  int brazilRankindNewRecovered(List<Country> countries) {
    Collections.sort(countries,
        Collections.reverseOrder(Comparator.comparingInt(Country::getNewRecovered)));
    Optional<Country> brazil = countries.stream()
        .filter(country -> country.getCountry().equals("Brazil")).findFirst();
    return countries.indexOf(brazil.get()) + 1;
  }

  int brazilRankindTotalRecovered(List<Country> countries) {
    Collections.sort(countries,
        Collections.reverseOrder(Comparator.comparingInt(Country::getTotalRecovered)));
    Optional<Country> brazil = countries.stream()
        .filter(country -> country.getCountry().equals("Brazil")).findFirst();
    return countries.indexOf(brazil.get()) + 1;
  }

  private List<SourceRecord> recordsFromCountries(List<SourceRecord> records, JSONArray countries) {
    for (Object o : countries) {
      Country country = Country.fromJson((JSONObject) o);
      SourceRecord record = generateSourceRecordFrom(country);
      records.add(record);
    }

    log.info("-----> Total of records added: {} ", records.size());

    this.timestamp = Instant.now().toEpochMilli();

    log.info("-----> Timestamp: {}", timestamp);

    return records;
  }

  JSONArray getCovid19APICountries(int currentAttempt) {
    GetRequest request = Unirest.get(COVID19API_ENDPOINT);
    HttpResponse<JsonNode> jsonResponse = null;
    try {
      if (currentAttempt < 10) { // try 10 times
        jsonResponse = request.asJson();
        if (jsonResponse.getStatus() == 200) {
          log.info("-----> Covid19API: information gathered sucessfully");
          return jsonResponse.getBody().getObject().getJSONArray("Countries");
        } else {
          throw new UnirestException("Returned Status code: " + jsonResponse.getStatus());
        }
      }
    } catch (UnirestException e) {
      log.warn("-----> Exception when loading API data: ", e);

      if (jsonResponse.getStatus() == 429) { // HTTP 429: Too many requests
        // try again in 1 second
        log.warn("-----> Covid19API: 429 Too Many Requests; trying again in one second");
      }

      try {
        Thread.sleep(1000);
      } catch (InterruptedException ex) {
        ex.printStackTrace();
      }

      log.info("-----> Trying one more time (current {} out of {}): ", currentAttempt, 10);
      return getCovid19APICountries(++currentAttempt);
    }
    log.warn("-----> Cannot get countries due to API issues");
    return new JSONArray();
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
    map.put("timestamp", String.valueOf(Instant.now().toEpochMilli()));
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

  @Override
  public void stop() {

  }
}
