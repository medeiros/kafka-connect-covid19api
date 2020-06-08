package com.arneam;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;
import org.apache.kafka.connect.data.Timestamp;

public class Covid19Schema {

  public static final String COUNTRY_FIELD = "Country";
  public static final String COUNTRY_CODE_FIELD = "CountryCode";
  public static final String SLUG_FIELD = "Slug";
  public static final String NEW_CONFIRMED_FIELD = "NewConfirmed";
  public static final String TOTAL_CONFIRMED_FIELD = "TotalConfirmed";
  public static final String NEW_DEATHS_FIELD = "NewDeaths";
  public static final String TOTAL_DEATHS_FIELD = "TotalDeaths";
  public static final String NEW_RECOVERED_FIELD = "NewRecovered";
  public static final String TOTAL_RECOVERED_FIELD = "TotalRecovered";
  public static final String DATE_FIELD = "Date";

  public static final String SCHEMA_KEY = "com.arneam.covid19.CountryKey";
  public static final String SCHEMA_VALUE = "com.arneam.covid19.CountryValue";

  public static final Schema KEY_SCHEMA = SchemaBuilder.struct().name(SCHEMA_KEY).version(1)
      .field(COUNTRY_CODE_FIELD, Schema.STRING_SCHEMA).build();
  public static final Schema VALUE_SCHEMA = SchemaBuilder.struct().name(SCHEMA_VALUE).version(2)
      .field(COUNTRY_FIELD, Schema.STRING_SCHEMA)
      .field(COUNTRY_CODE_FIELD, Schema.STRING_SCHEMA)
      .field(SLUG_FIELD, Schema.STRING_SCHEMA)
      .field(NEW_CONFIRMED_FIELD, Schema.INT32_SCHEMA)
      .field(TOTAL_CONFIRMED_FIELD, Schema.INT32_SCHEMA)
      .field(NEW_DEATHS_FIELD, Schema.INT32_SCHEMA)
      .field(TOTAL_DEATHS_FIELD, Schema.INT32_SCHEMA)
      .field(NEW_RECOVERED_FIELD, Schema.INT32_SCHEMA)
      .field(TOTAL_RECOVERED_FIELD, Schema.INT32_SCHEMA)
      .field(DATE_FIELD, Schema.STRING_SCHEMA)
      .build();

}
