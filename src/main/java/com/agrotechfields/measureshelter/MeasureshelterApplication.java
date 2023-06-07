package com.agrotechfields.measureshelter;

import java.util.Locale;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The Class MeasureshelterApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.agrotechfields.measureshelter"})
public class MeasureshelterApplication {

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    Locale.setDefault(Locale.US);
    SpringApplication.run(MeasureshelterApplication.class, args);
  }

}
