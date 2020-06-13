package com.arneam.model;


import static com.arneam.Covid19Schema.COUNTRY_CODE_FIELD;
import static com.arneam.Covid19Schema.COUNTRY_FIELD;
import static com.arneam.Covid19Schema.DATE_FIELD;
import static com.arneam.Covid19Schema.NEW_CONFIRMED_FIELD;
import static com.arneam.Covid19Schema.NEW_DEATHS_FIELD;
import static com.arneam.Covid19Schema.NEW_RECOVERED_FIELD;
import static com.arneam.Covid19Schema.SLUG_FIELD;
import static com.arneam.Covid19Schema.TOTAL_CONFIRMED_FIELD;
import static com.arneam.Covid19Schema.TOTAL_DEATHS_FIELD;
import static com.arneam.Covid19Schema.TOTAL_RECOVERED_FIELD;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CountryTest {

  private static String JSONSchema = "{\n"
      + "      \"" + COUNTRY_FIELD + "\": \"%1$s\",\n"
      + "      \"" + COUNTRY_CODE_FIELD + "\": \"%2$s\",\n"
      + "      \"" + SLUG_FIELD + "\": \"%3$s\",\n"
      + "      \"" + NEW_CONFIRMED_FIELD + "\": %4$d,\n"
      + "      \"" + TOTAL_CONFIRMED_FIELD + "\": %5$d,\n"
      + "      \"" + NEW_DEATHS_FIELD + "\": %6$d,\n"
      + "      \"" + TOTAL_DEATHS_FIELD + "\": %7$d,\n"
      + "      \"" + NEW_RECOVERED_FIELD + "\": %8$d,\n"
      + "      \"" + TOTAL_RECOVERED_FIELD + "\": %9$d,\n"
      + "      \"" + DATE_FIELD + "\": \"%10$s\"\n"
      + "    }";

  @ParameterizedTest
  @CsvSource(value = {"Bosnia and Herzegovina,BA,bosnia-and-herzegovina,23,2181,3,120,60,1228,"
      + "2020-02-02T02:02:02Z"})
  void shouldCreateCountryByJsonInput(String countryName, String countryCode, String slug,
      int newConfirmed, int totalConfirmed, int newDeaths, int totalDeaths, int newRecovered,
      int totalRecovered, String date) {
    String json = String.format(JSONSchema, countryName, countryCode, slug, newConfirmed,
        totalConfirmed, newDeaths, totalDeaths, newRecovered, totalRecovered, date);
    Country country = Country.fromJson(new JSONObject(json));

    assertThat(country.getCountry(), is(equalTo(countryName)));
    assertThat(country.getCountryCode(), is(equalTo(countryCode)));
    assertThat(country.getSlug(), is(equalTo(slug)));
    assertThat(country.getNewConfirmed(), is(equalTo(newConfirmed)));
    assertThat(country.getTotalConfirmed(), is(equalTo(totalConfirmed)));
    assertThat(country.getNewDeaths(), is(equalTo(newDeaths)));
    assertThat(country.getTotalDeaths(), is(equalTo(totalDeaths)));
    assertThat(country.getNewRecovered(), is(equalTo(newRecovered)));
    assertThat(country.getTotalRecovered(), is(equalTo(totalRecovered)));
    assertThat(country.getDate(), is(equalTo("2020-02-02T00:00:00Z")));
  }

  @Test
  void shoudlTruncateDate() {
    String truncatedDate = Country.truncateDateInDay(Instant.now().toString());
    assertThat(truncatedDate, is(equalTo(Instant.now().truncatedTo(ChronoUnit.DAYS).toString())));
  }

}