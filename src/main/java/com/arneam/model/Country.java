package com.arneam.model;

import static com.arneam.Covid19Schema.COUNTRY_CODE_FIELD;
import static com.arneam.Covid19Schema.DATE_FIELD;
import static com.arneam.Covid19Schema.NEW_CONFIRMED_FIELD;
import static com.arneam.Covid19Schema.NEW_DEATHS_FIELD;
import static com.arneam.Covid19Schema.NEW_RECOVERED_FIELD;
import static com.arneam.Covid19Schema.SLUG_FIELD;
import static com.arneam.Covid19Schema.TOTAL_CONFIRMED_FIELD;
import static com.arneam.Covid19Schema.TOTAL_DEATHS_FIELD;
import static com.arneam.Covid19Schema.TOTAL_RECOVERED_FIELD;

import com.arneam.Covid19Schema;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

@Getter
@Setter
public class Country {

  private String country;
  private String countryCode;
  private String slug;
  private int newConfirmed;
  private int totalConfirmed;
  private int newDeaths;
  private int totalDeaths;
  private int newRecovered;
  private int totalRecovered;
  private Instant date;

  public static Country fromJson(JSONObject jsonObject) {
    Country country = new Country();
    country.setCountry(jsonObject.getString(Covid19Schema.COUNTRY_FIELD));
    country.setCountryCode(jsonObject.getString(COUNTRY_CODE_FIELD));
    country.setSlug(jsonObject.getString(SLUG_FIELD));
    country.setNewConfirmed(jsonObject.getInt(NEW_CONFIRMED_FIELD));
    country.setTotalConfirmed(jsonObject.getInt(TOTAL_CONFIRMED_FIELD));
    country.setNewDeaths(jsonObject.getInt(NEW_DEATHS_FIELD));
    country.setTotalDeaths(jsonObject.getInt(TOTAL_DEATHS_FIELD));
    country.setNewRecovered(jsonObject.getInt(NEW_RECOVERED_FIELD));
    country.setTotalRecovered(jsonObject.getInt(TOTAL_RECOVERED_FIELD));
    country.setDate(Instant.parse(jsonObject.getString(DATE_FIELD)));
    return country;
  }

}
