package com.arneam;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class Covid19SourceTaskTest {

  @Test
  void shouldGetCovid19APICountries() {
    Covid19SourceTask task = new Covid19SourceTask();
    assertNotNull(task.getCovid19APICountries());
  }

}