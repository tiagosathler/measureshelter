package com.agrotechfields.measureshelter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.request.AuthDto;
import com.agrotechfields.measureshelter.dto.request.IsleDto;
import com.agrotechfields.measureshelter.dto.response.TokenReponseDto;
import com.agrotechfields.measureshelter.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * The Class MeasureshelterApplicationTests.
 */
@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasureshelterApplicationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  @SpyBean
  private UserRepository userRepository;

  @Container
  static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:5");

  @DynamicPropertySource
  static void mongoDbProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
  }

  @BeforeEach
  public void setup() {
    userRepository.deleteAll();;
  }

  @Test
  @Order(1)
  @DisplayName("1. Login using a valid username")
  void loginUsingAValidUsername() throws Exception {
    insertUser();

    AuthDto authDto = new AuthDto("admin", "pass");
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isString());
  }

  @Test
  @Order(2)
  @DisplayName("2. Login using an invalid username")
  void loginUsingAnInvalidUsername() throws Exception {
    insertUser();

    AuthDto authDto = new AuthDto("root", "pass");
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Bad credentials"));
  }

  @Test
  @Order(3)
  @DisplayName("3. Login using an invalid password")
  void loginUsingAnInvalidPassword() throws Exception {
    insertUser();

    AuthDto authDto = new AuthDto("admin", "wrongpass");
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Bad credentials"));
  }

  @Test
  @Order(4)
  @DisplayName("4. Isle - create a valid Isle")
  void createAValidIsle() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString());
  }

  @Test
  @Order(5)
  @DisplayName("5. Isle - post with an invalid serial number")
  void postWithAnInvalidSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("serialNumber: must be 10 digits including numbers and capital letters"));
  }

  @Test
  @Order(6)
  @DisplayName("6. Isle - post without a serial number")
  void postWithoutASerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("serialNumber: must not be empty"));
  }

  @Test
  @Order(7)
  @DisplayName("7. Isle - post with a latitude greater than the limit")
  void postWithALatitudeGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(90));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("latitude: must be less than 90"));
  }

  @Test
  @Order(8)
  @DisplayName("8. Isle - post with a latitude less than the limit")
  void postWithALatitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-90));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("latitude: must be greater than -90"));
  }

  @Test
  @Order(9)
  @DisplayName("9. Isle - post without a latitude")
  void postWithoutALatitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("latitude: must not be null"));
  }

  @Test
  @Order(10)
  @DisplayName("10. Isle - post with a longitude greater than the limit")
  void postWithALongitudeGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(180.01));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("longitude: must be less than or equal to 180"));
  }

  @Test
  @Order(11)
  @DisplayName("11. Isle - post with a longitude less than the limit")
  void postWithALongitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(-180.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("longitude: must be greater than -180"));
  }

  @Test
  @Order(12)
  @DisplayName("12. Isle - post without a longitude")
  void postWithoutALongitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("longitude: must not be null"));
  }

  @Test
  @Order(13)
  @DisplayName("13. Isle - post with an altitude less than the limit")
  void postWithAnAltitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(0));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("altitude: must be greater than 0"));
  }

  @Test
  @Order(14)
  @DisplayName("14. Isle - post without an altitude")
  void postWithoutAnAltitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("altitude: must not be null"));
  }

  @Test
  @Order(15)
  @DisplayName("15. Isle - post with a sampling interval greater than the limit")
  void postWithASamplingIntervalGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.01));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(0);

    String body = objectMapper.writeValueAsString(isleDto);
    HttpHeaders httpHeaders = getHeadersWithTokenByLoggingWithAdminUser();

    mockMvc
        .perform(post("/isle")
            .headers(httpHeaders)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("samplingInterval: must be greater than or equal to 1"));
  }

  private void insertUser() {
    String encodedPassword = passwordEncoder.encode("pass");
    User user = new User(null, "admin", encodedPassword, Role.ROLE_ADMIN);
    userRepository.save(user);
  }

  private HttpHeaders getHeadersWithTokenByLoggingWithAdminUser() throws Exception {
    insertUser();

    AuthDto authDto = new AuthDto("admin", "pass");
    String body = objectMapper.writeValueAsString(authDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    TokenReponseDto tokenDto = objectMapper.readValue(contentAsString, TokenReponseDto.class);
    String token = tokenDto.getToken();

    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(token);
    return httpHeaders;
  }
}
