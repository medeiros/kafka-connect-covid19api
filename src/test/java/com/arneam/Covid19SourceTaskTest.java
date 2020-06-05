package com.arneam;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.arneam.model.Country;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Covid19SourceTaskTest {

  List<Country> countries;

  @BeforeEach
  void init() {
    Country afghanistan = new Country();
    afghanistan.setCountry("Afghanistan");
    afghanistan.setNewConfirmed(369);
    afghanistan.setTotalConfirmed(1000);
    afghanistan.setNewDeaths(100);
    afghanistan.setTotalDeaths(100);
    afghanistan.setNewRecovered(369);
    afghanistan.setTotalRecovered(1000);

    Country argentina = new Country();
    argentina.setCountry("Argentina");
    argentina.setNewConfirmed(258);
    argentina.setTotalConfirmed(200);
    argentina.setNewDeaths(200);
    argentina.setTotalDeaths(200);
    argentina.setNewRecovered(200);
    argentina.setTotalRecovered(500);

    Country brazil = new Country();
    brazil.setCountry("Brazil");
    brazil.setNewConfirmed(6638);
    brazil.setTotalConfirmed(300);
    brazil.setNewDeaths(1000);
    brazil.setTotalDeaths(50);
    brazil.setNewRecovered(230);
    brazil.setTotalRecovered(3001);

    Country unitedStates = new Country();
    unitedStates.setCountry("United States of America");
    unitedStates.setNewConfirmed(39418);
    unitedStates.setTotalConfirmed(400);
    unitedStates.setNewDeaths(400);
    unitedStates.setTotalDeaths(400);
    unitedStates.setNewRecovered(400);
    unitedStates.setTotalRecovered(3000);

    this.countries = new ArrayList<>();
    countries.add(afghanistan);
    countries.add(argentina);
    countries.add(brazil);
    countries.add(unitedStates);
  }

  @Test
  void shouldGetNewConfirmed() {
    assertThat(new Covid19SourceTask().brazilRankindNewConfirmed(countries), is(equalTo(2)));
  }

  @Test
  void shouldGetTotalConfirmed() {
    assertThat(new Covid19SourceTask().brazilRankindTotalConfirmed(countries), is(equalTo(3)));
  }

  @Test
  void shouldGetNewDeaths() {
    assertThat(new Covid19SourceTask().brazilRankindNewDeaths(countries), is(equalTo(1)));
  }

  @Test
  void shouldGetTotalDeaths() {
    assertThat(new Covid19SourceTask().brazilRankindTotalDeaths(countries), is(equalTo(4)));
  }

  @Test
  void shouldGetNewRecovered() {
    assertThat(new Covid19SourceTask().brazilRankindNewRecovered(countries), is(equalTo(3)));
  }

  @Test
  void shouldGetTotalRecovered() {
    assertThat(new Covid19SourceTask().brazilRankindTotalRecovered(countries), is(equalTo(1)));
  }

}