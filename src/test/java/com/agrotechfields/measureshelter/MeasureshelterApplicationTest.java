package com.agrotechfields.measureshelter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * The Class MeasureshelterApplicationTests.
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasureshelterApplicationTest {

  /**
   * Context loads.
   */
  @Test
  @DisplayName("ContextLoads")
  void contextLoads() {
    assertEquals(1, 1);
  }

}
