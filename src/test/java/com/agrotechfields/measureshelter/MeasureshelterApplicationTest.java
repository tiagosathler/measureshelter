package com.agrotechfields.measureshelter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import org.aspectj.apache.bcel.generic.INVOKEINTERFACE;
import org.junit.jupiter.api.AfterEach;
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
import com.agrotechfields.measureshelter.dto.request.IsleUserDto;
import com.agrotechfields.measureshelter.dto.request.MeasureDto;
import com.agrotechfields.measureshelter.dto.request.UserDto;
import com.agrotechfields.measureshelter.dto.response.IsleResponseDto;
import com.agrotechfields.measureshelter.dto.response.TokenReponseDto;
import com.agrotechfields.measureshelter.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

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
  public void beforeEach() {
  }

  @AfterEach
  public void afterEach() {
  }

  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "password";

  private static final String USER_USERNAME = "user";
  private static final String USER_PASSWORD = "password";

  private static final String ISLE_USERNAME = "ISLE000001";
  private static final String ISLE_PASSWORD = "password";

  private static final String ANOTHER_ISLE = "ISLE000003";

  private static final String ISLE_USER_ID = "isleUserId";

  private static final String MEASURE = "MEASURE";

  private static final String NONEXISTING_ID = "648a5072cbe534d1d321f28d";
  private static final String INVALID_ID = "648a5072cbe";

  private static final MeasureDto MEASURE_DTO = new MeasureDto();

  private static final Map<String, String> ids = new HashMap<>();

  private static String token;

  private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

  private void insertAnAdminUserIntoTheDb() {
    String encodedPassword = passwordEncoder.encode(ADMIN_PASSWORD);
    User user = new User(null, ADMIN_USERNAME, encodedPassword, Role.ROLE_ADMIN);
    userRepository.save(user);
    ids.put(ADMIN_USERNAME, user.getId().toHexString());
  }

  private void setHeadersWithTokenByLogin(String username, String password)
      throws Exception {
    AuthDto authDto = new AuthDto(username, password);
    String body = objectMapper.writeValueAsString(authDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();
    
    token = JsonPath.parse(contentAsString).read("$.token").toString();

    HTTP_HEADERS.setBearerAuth(token);
  }

  private void resetMeasureDto() {
    MEASURE_DTO.setAirTemp(BigDecimal.valueOf(27.8));
    MEASURE_DTO.setGndTemp(BigDecimal.valueOf(31.2));
    MEASURE_DTO.setWindSpeed(BigDecimal.valueOf(4.2));
    MEASURE_DTO.setWindDirection(BigDecimal.valueOf(45));
    MEASURE_DTO.setIrradiance(BigDecimal.valueOf(1000.0));
    MEASURE_DTO.setPressure(BigDecimal.valueOf(1024.0));
    MEASURE_DTO.setAirHumidity(BigDecimal.valueOf(85.2));
    MEASURE_DTO.setGndHumidity(BigDecimal.valueOf(60.0));
    MEASURE_DTO.setPrecipitation(BigDecimal.valueOf(1.2));
    MEASURE_DTO.setRainIntensity(BigDecimal.valueOf(0.2));
  }

  @Test
  @Order(1)
  @DisplayName("1. Login - POST using a valid username")
  void loginUsingAValidUsername() throws Exception {
    insertAnAdminUserIntoTheDb();

    AuthDto authDto = new AuthDto(ADMIN_USERNAME, ADMIN_PASSWORD);
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  @Order(2)
  @DisplayName("2. Login - POST using an invalid username")
  void loginUsingAnInvalidUsername() throws Exception {
    AuthDto authDto = new AuthDto("wrongusername", ADMIN_PASSWORD);
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Bad credentials"));
  }

  @Test
  @Order(3)
  @DisplayName("3. Login - POST using an invalid password")
  void loginUsingAnInvalidPassword() throws Exception {
    AuthDto authDto = new AuthDto(ADMIN_USERNAME, "wrongpassword");
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.message").value("Bad credentials"));
  }

  @Test
  @Order(4)
  @DisplayName("4. Login - POST without an username")
  void loginWithoutAnUsername() throws Exception {
    AuthDto authDto = new AuthDto(null, ADMIN_PASSWORD);
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("username: must not be empty"));
  }

  @Test
  @Order(5)
  @DisplayName("5. Login - POST without a password")
  void loginWithoutAPassword() throws Exception {
    AuthDto authDto = new AuthDto(ADMIN_USERNAME, null);
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: must not be empty"));
  }

  @Test
  @Order(6)
  @DisplayName("6. Isle - POST a valid Isle")
  void postAValidIsle() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);
    setHeadersWithTokenByLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

    MvcResult mvcResult = mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isString())
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();

    ids.put(ISLE_USERNAME, id);
  }

  @Test
  @Order(7)
  @DisplayName("7. Isle - POST with an invalid serial number")
  void postWithAnInvalidSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("000000001");
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("serialNumber: must be 10 digits including numbers and capital letters"));
  }

  @Test
  @Order(8)
  @DisplayName("8. Isle - POST without a serial number")
  void postWithoutASerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("serialNumber: must not be empty"));
  }

  @Test
  @Order(9)
  @DisplayName("9. Isle - POST with a latitude greater than the limit")
  void postWithALatitudeGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(90));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("latitude: must be less than 90"));
  }

  @Test
  @Order(10)
  @DisplayName("10. Isle - POST with a latitude less than the limit")
  void postWithALatitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-90));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("latitude: must be greater than -90"));
  }

  @Test
  @Order(11)
  @DisplayName("11. Isle - POST without a latitude")
  void postWithoutALatitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("latitude: must not be null"));
  }

  @Test
  @Order(12)
  @DisplayName("12. Isle - POST with a longitude greater than the limit")
  void postWithALongitudeGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(180.01));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("longitude: must be less than or equal to 180"));
  }

  @Test
  @Order(13)
  @DisplayName("13. Isle - POST with a longitude less than the limit")
  void postWithALongitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(-180.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("longitude: must be greater than -180"));
  }

  @Test
  @Order(14)
  @DisplayName("14. Isle - POST without a longitude")
  void postWithoutALongitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("longitude: must not be null"));
  }

  @Test
  @Order(15)
  @DisplayName("15. Isle - POST with an altitude less than the limit")
  void postWithAnAltitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(0));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("altitude: must be greater than 0"));
  }

  @Test
  @Order(16)
  @DisplayName("16. Isle - POST without an altitude")
  void postWithoutAnAltitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(5);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("altitude: must not be null"));
  }

  @Test
  @Order(17)
  @DisplayName("17. Isle - POST with a sampling interval greater than the limit")
  void postWithASamplingIntervalGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.01));
    isleDto.setAltitude(BigDecimal.valueOf(1000));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(3601);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("samplingInterval: must be less than or equal to 3600"));
  }

  @Test
  @Order(18)
  @DisplayName("18. Isle - POST with a sampling interval less than the limit")
  void postWithASamplingIntervalLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.01));
    isleDto.setAltitude(BigDecimal.valueOf(3601));
    isleDto.setIsItWorking(true);
    isleDto.setSamplingInterval(0);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message")
            .value("samplingInterval: must be greater than or equal to 1"));
  }

  @Test
  @Order(19)
  @DisplayName("19. Isle - POST with an existing serial number")
  void postWithAnExistingSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(-21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(100));
    isleDto.setIsItWorking(true);

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message")
            .value("Isle already exists"));
  }

  @Test
  @Order(20)
  @DisplayName("20. Isle - GET all isles")
  void getAllIsles() throws Exception {
    mockMvc
        .perform(get("/isle").headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].serialNumber").value(ISLE_USERNAME))
        .andExpect(jsonPath("$[0].id").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(21)
  @DisplayName("21. Isle - GET isle by serial number")
  void getIsleBySerialNumber() throws Exception {
    mockMvc
        .perform(get("/isle/serial/" + ISLE_USERNAME).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.serialNumber").value(ISLE_USERNAME))
        .andExpect(jsonPath("$.id").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(22)
  @DisplayName("22. Isle - GET isle by nonexistent serial number")
  void getIsleByNonexistingSerialNumber() throws Exception {
    mockMvc
        .perform(get("/isle/serial/9999999999").headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(23)
  @DisplayName("23. Isle - GET isle by id")
  void getIsleById() throws Exception {
    mockMvc
        .perform(get("/isle/" + ids.get(ISLE_USERNAME)).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.serialNumber").value(ISLE_USERNAME))
        .andExpect(jsonPath("$.id").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(24)
  @DisplayName("24. Isle - GET isle by nonexisting id")
  void getIsleByNonexistingId() throws Exception {
    mockMvc
        .perform(get("/isle/" + NONEXISTING_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(25)
  @DisplayName("25. Isle - GET isle invalid id")
  void getIsleByInvalidId() throws Exception {
    mockMvc
        .perform(get("/isle/" + INVALID_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_ID + " is invalid Id"));
  }

  @Test
  @Order(26)
  @DisplayName("26. Isle - PUT by isle id with same serial number")
  void putByIsleIdWithSameSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(100));

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(put("/isle/" + ids.get(ISLE_USERNAME))
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id").value(ids.get(ISLE_USERNAME)))
        .andExpect(jsonPath("$.serialNumber").value(ISLE_USERNAME))
        .andExpect(jsonPath("$.altitude").value("100"));
  }

  @Test
  @Order(27)
  @DisplayName("27. Isle - PUT by isle id with existing serial number")
  void putByIsleIdWithExistingSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("ISLE000002");
    isleDto.setLatitude(BigDecimal.valueOf(14.00));
    isleDto.setLongitude(BigDecimal.valueOf(-10.00));
    isleDto.setAltitude(BigDecimal.valueOf(200));

    String body = objectMapper.writeValueAsString(isleDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andReturn();

    mockMvc
        .perform(put("/isle/" + ids.get(ISLE_USERNAME))
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Isle already exists"));

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();

    ids.put("SECOND_ISLE", id);
  }

  @Test
  @Order(28)
  @DisplayName("28. Isle - PUT by nonexisting isle id")
  void putByNonexistingIsleId() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ISLE_USERNAME);
    isleDto.setLatitude(BigDecimal.valueOf(21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(100));

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(put("/isle/" + NONEXISTING_ID)
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(29)
  @DisplayName("29. Isle - PUT by invalid isle id")
  void putByInvalidIsleId() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(100));

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(put("/isle/" + INVALID_ID)
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_ID + " is invalid Id"));
  }

  @Test
  @Order(30)
  @DisplayName("30. Isle - DELETE by isle id")
  void deleteByIsleId() throws Exception {
    mockMvc
        .perform(delete("/isle/" + ids.get("SECOND_ISLE")).headers(HTTP_HEADERS))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(31)
  @DisplayName("31. Isle - DELETE by nonexisting isle id")
  void deleteByNonexistingIsleId() throws Exception {
    mockMvc
        .perform(delete("/isle/" + NONEXISTING_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(32)
  @DisplayName("32. Isle - DELETE by invalid isle id")
  void deleteByInvalidIsleId() throws Exception {
    mockMvc
        .perform(delete("/isle/" + INVALID_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_ID + " is invalid Id"));
  }

  @Test
  @Order(33)
  @DisplayName("33. Isle - PATCH by isle id toggling working to false")
  void patchByIsleIdTogglingWorkingToFalse() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/" + ids.get(ISLE_USERNAME)).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.isItWorking").value("false"));
  }

  @Test
  @Order(34)
  @DisplayName("34. Isle - PATCH by isle id toggling working to true")
  void patchByIsleIdTogglingWorkingToTrue() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/" + ids.get(ISLE_USERNAME)).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.isItWorking").value("true"));
  }

  @Test
  @Order(35)
  @DisplayName("35. Isle - PATCH by nonexisting isle id")
  void patchByNonexistingIsleId() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/" + NONEXISTING_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(36)
  @DisplayName("36. Isle - PATCH by invalid isle id")
  void patchByInvalidIsleId() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/" + INVALID_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_ID + " is invalid Id"));
  }

  @Test
  @Order(37)
  @DisplayName("37. User - POST a valid common user")
  void postAValidCommonUser() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(USER_USERNAME);
    userDto.setPassword(USER_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.role").value(Role.ROLE_USER.name()))
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();

    ids.put(USER_USERNAME, id);
  }

  @Test
  @Order(38)
  @DisplayName("38. User - POST with an existing user")
  void postWithAnExistingUser() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(USER_USERNAME);
    userDto.setPassword(USER_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("User already exists"));
  }

  @Test
  @Order(39)
  @DisplayName("39. User - POST with an username less than the limit")
  void postWithAnUsernameLessThanTheLimit() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername("123");
    userDto.setPassword(USER_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("username: size must be between 4 and 10"));
  }

  @Test
  @Order(40)
  @DisplayName("40. User - POST with an username greater than the limit")
  void postWithAnUsernameGreaterThanTheLimit() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername("00123456789");
    userDto.setPassword(USER_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("username: size must be between 4 and 10"));
  }

  @Test
  @Order(41)
  @DisplayName("41. User - POST without an username")
  void postWithoutAnUsername() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setPassword(USER_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("username: must not be blank"));
  }

  @Test
  @Order(42)
  @DisplayName("42. User - POST with an username using the serial number pattern")
  void postWithAnUsernameUsingTheSerialNumberPattern() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(ISLE_USERNAME);
    userDto.setPassword(USER_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("username: must not be a isle serial number"));
  }

  @Test
  @Order(43)
  @DisplayName("43. User - POST with a password less than the limit")
  void postWithAPasswordLessThanTheLimit() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(USER_USERNAME);
    userDto.setPassword("12345");

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: size must be between 6 and 14"));
  }

  @Test
  @Order(44)
  @DisplayName("44. User - POST with a password greater than the limit")
  void postWithAPasswordGreaterThanTheLimit() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(USER_USERNAME);
    userDto.setPassword("123456789012345");

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: size must be between 6 and 14"));
  }

  @Test
  @Order(45)
  @DisplayName("45. User - POST without a password")
  void postWithoutAPassword() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(USER_USERNAME);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: must not be blank"));
  }

  @Test
  @Order(46)
  @DisplayName("46. User - POST a valid admin user")
  void postAValidAdminUser() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername("admin2");
    userDto.setPassword("password");

    String body = objectMapper.writeValueAsString(userDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/user?isAdmin=true")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.role").value(Role.ROLE_ADMIN.name()))
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();

    ids.put("SECOND_ADMIN", id);
  }

  @Test
  @Order(47)
  @DisplayName("47. User - POST registering an existing isle")
  void postRegisteringAnExistingIsle() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ISLE_USERNAME);
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.role").value(Role.ROLE_ISLE.name()))
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();

    ids.put(ISLE_USER_ID, id);
  }

  @Test
  @Order(48)
  @DisplayName("48. User - POST registering an existing isle already registered")
  void postRegisteringAnExistingIsleAlreadyRegistered() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ISLE_USERNAME);
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Isle user by the serial number '"+ ISLE_USERNAME +"' already exists"));
  }

  @Test
  @Order(49)
  @DisplayName("49. User - POST registering a nonexisting isle")
  void postRegisteringANonexistingIsle() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber("ISLE567890");
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(50)
  @DisplayName("50. User - POST registering an isle with invalid pattern for serial number")
  void postRegisteringAnIsleWithInvalidPatternForSerialNumber() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(USER_USERNAME);
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("serialNumber: must be 10 digits including numbers and capital letters"));
  }

  @Test
  @Order(51)
  @DisplayName("51. User - POST registering an isle without serial number")
  void postRegisteringAnIsleWithoutSerialNumber() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("serialNumber: must not be blank"));
  }

  @Test
  @Order(52)
  @DisplayName("52. User - POST registering an isle with a password less than the limit")
  void postRegisteringAnIsleWithAPasswordLessThanTheLimit() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ISLE_USERNAME);
    isleUserDto.setPassword("1234");

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: size must be between 5 and 14"));
  }

  @Test
  @Order(53)
  @DisplayName("53. User - POST registering an isle with a password greater than the limit")
  void postRegisteringAnIsleWithAPasswordGreaterThanTheLimit() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ISLE_USERNAME);
    isleUserDto.setPassword("123456789012345");

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: size must be between 5 and 14"));
  }

  @Test
  @Order(54)
  @DisplayName("54. User - POST registering an isle without a password")
  void postRegisteringAnIsleWithoutAPassword() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ISLE_USERNAME);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("password: must not be blank"));
  }

  @Test
  @Order(55)
  @DisplayName("55. User - GET all users")
  void getAllUsers() throws Exception {
    mockMvc
        .perform(get("/user").headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ids.get(ADMIN_USERNAME)))
        .andExpect(jsonPath("$[0].username").value(ADMIN_USERNAME))
        .andExpect(jsonPath("$[1].id").value(ids.get(USER_USERNAME)))
        .andExpect(jsonPath("$[1].username").value(USER_USERNAME))
        .andExpect(jsonPath("$[3].id").value(ids.get(ISLE_USER_ID)))
        .andExpect(jsonPath("$[3].username").value(ISLE_USERNAME));
  }

  @Test
  @Order(56)
  @DisplayName("56. User - GET user by valid id")
  void getUserByValidId() throws Exception {
    mockMvc
        .perform(get("/user/" + ids.get(ISLE_USER_ID)).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ids.get(ISLE_USER_ID)))
        .andExpect(jsonPath("$.username").value(ISLE_USERNAME));
  }

  @Test
  @Order(57)
  @DisplayName("57. User - GET user by nonexisting id")
  void getUserByValidNonexistingId() throws Exception {
    mockMvc
        .perform(get("/user/" + NONEXISTING_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("User not found"));
  }

  @Test
  @Order(58)
  @DisplayName("57. User - GET user by invalid id")
  void getUserByInvalidId() throws Exception {
    mockMvc
        .perform(get("/user/" + INVALID_ID).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(INVALID_ID + " is invalid Id"));
  }

  @Test
  @Order(59)
  @DisplayName("59. User - PUT updating yourself")
  void putUpdatingYourself() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(ADMIN_USERNAME);
    userDto.setPassword(ADMIN_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(put("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ids.get(ADMIN_USERNAME)));
  }

  @Test
  @Order(60)
  @DisplayName("60. User - PUT trying to change to an existing username")
  void putTryingToChangeToAnExistingUsername() throws Exception {
    UserDto userDto = new UserDto();
    userDto.setUsername(USER_USERNAME);
    userDto.setPassword(ADMIN_PASSWORD);

    String body = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(put("/user")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("User already exists"));
  }

  @Test
  @Order(61)
  @DisplayName("61. User - PUT updating an existing isle user")
  void putUpdatingAnExistingIsleUser() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ISLE_USERNAME);
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(put("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ids.get(ISLE_USER_ID)));
  }

  @Test
  @Order(62)
  @DisplayName("62. User - PUT updating a nonexisting isle user")
  void putUpdatingANonexistingIsleUser() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber("ISLE567890");
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(put("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(63)
  @DisplayName("63. User - PUT trying to update an unregistered isle user")
  void putTryingToUpdateAnUnregisteredIsleUser() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber(ANOTHER_ISLE);
    isleDto.setLatitude(BigDecimal.valueOf(14.00));
    isleDto.setLongitude(BigDecimal.valueOf(-10.00));
    isleDto.setAltitude(BigDecimal.valueOf(200));

    String body = objectMapper.writeValueAsString(isleDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andReturn();

    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ANOTHER_ISLE);
    isleUserDto.setPassword(ISLE_PASSWORD);

    body = objectMapper.writeValueAsString(isleUserDto);

    mockMvc
        .perform(put("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Register this isle first. Isle as user not found"));

    String contextAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contextAsString).read("$.id").toString();

    ids.put(ANOTHER_ISLE, id);
  }

  @Test
  @Order(64)
  @DisplayName("64. User - PATCH toggling user role by its id to admin role")
  void patchTogglingUserRoleByItsIdToAdminRole() throws Exception {
    mockMvc
        .perform(patch("/user/" + ids.get(USER_USERNAME) + "/toggle/role")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.role").value(Role.ROLE_ADMIN.name()));
  }

  @Test
  @Order(65)
  @DisplayName("65. User - PATCH toggling user role by its id to user role")
  void patchTogglingUserRoleByItsIdToUserRole() throws Exception {
    mockMvc
        .perform(patch("/user/" + ids.get(USER_USERNAME) + "/toggle/role")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.role").value(Role.ROLE_USER.name()));
  }

  @Test
  @Order(66)
  @DisplayName("66. User - PATCH trying to toggle role of a isle user by its id")
  void patchTryingToggleRoleOfAnIsleUserByItsId() throws Exception {
    mockMvc
        .perform(patch("/user/" + ids.get(ISLE_USER_ID) + "/toggle/role")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.message").value("User '" + ISLE_USERNAME + "' with role ROLE_ISLE does not have permission to do this"));
  }

  @Test
  @Order(67)
  @DisplayName("67. User - PATCH trying to toggle role by nonexisting id")
  void patchTryingToggleRoleByNonexistingId() throws Exception {
    mockMvc
        .perform(patch("/user/" + NONEXISTING_ID + "/toggle/role")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("User not found"));
  }

  @Test
  @Order(68)
  @DisplayName("68. User - PATCH toggling user enabled by its id to false")
  void patchTogglingUserEnabledByItsIdToFalse() throws Exception {
    mockMvc
        .perform(patch("/user/" + ids.get(USER_USERNAME) + "/toggle/enable")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isEnable").value("false"));
  }

  @Test
  @Order(69)
  @DisplayName("69. User - PATCH toggling disabled user by its id")
  void patchTogglingDisabledUserByItsId() throws Exception {
    mockMvc
        .perform(patch("/user/" + ids.get(USER_USERNAME) + "/toggle/enable")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isEnable").value("true"));
  }

  @Test
  @Order(70)
  @DisplayName("69. User - PATCH trying to toggle enable user by nonexisting id")
  void patchTryingToToggleEnableUserByNonexistingId() throws Exception {
    mockMvc
        .perform(patch("/user/" + NONEXISTING_ID + "/toggle/enable")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("User not found"));
  }

  @Test
  @Order(71)
  @DisplayName("71. User - DELETE by user id")
  void deleteByUserId() throws Exception {
    IsleUserDto isleUserDto = new IsleUserDto();
    isleUserDto.setSerialNumber(ANOTHER_ISLE);
    isleUserDto.setPassword(ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(isleUserDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/user/isle")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andReturn();

    String contextAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contextAsString).read("$.id").toString();

    mockMvc
        .perform(delete("/user/" + id)
            .headers(HTTP_HEADERS))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(72)
  @DisplayName("72. User - DELETE by nonexisting id")
  void deleteByNonexistingId() throws Exception {
    mockMvc
        .perform(delete("/user/" + NONEXISTING_ID)
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("User not found"));
  }

  @Test
  @Order(73)
  @DisplayName("73. Measure - POST creating a new measure")
  void postCreatingANewMeasure() throws Exception {
    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    resetMeasureDto();

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    MvcResult mvcResult = mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.timestamp").isNotEmpty())
        .andExpect(jsonPath("$.isleId").value(ids.get(ISLE_USERNAME)))
        .andExpect(jsonPath("$.airTemp").value(MEASURE_DTO.getAirTemp().toString()))
        .andExpect(jsonPath("$.gndTemp").value(MEASURE_DTO.getGndTemp().toString()))
        .andExpect(jsonPath("$.windSpeed").value(MEASURE_DTO.getWindSpeed().toString()))
        .andExpect(jsonPath("$.windDirection").value(MEASURE_DTO.getWindDirection().toString()))
        .andExpect(jsonPath("$.irradiance").value(MEASURE_DTO.getIrradiance().toString()))
        .andExpect(jsonPath("$.pressure").value(MEASURE_DTO.getPressure().toString()))
        .andExpect(jsonPath("$.airHumidity").value(MEASURE_DTO.getAirHumidity().toString()))
        .andExpect(jsonPath("$.gndHumidity").value(MEASURE_DTO.getGndHumidity().toString()))
        .andExpect(jsonPath("$.precipitation").value(MEASURE_DTO.getPrecipitation().toString()))
        .andExpect(jsonPath("$.rainIntensity").value(MEASURE_DTO.getRainIntensity().toString()))
        .andExpect(jsonPath("$.windSpeed").value(MEASURE_DTO.getWindSpeed().toString()))
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();
    
    ids.put(MEASURE, id);
  }

  @Test
  @Order(74)
  @DisplayName("74. Measure - POST with airTemp less than the limit")
  void postWithAirTempLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setAirTemp(BigDecimal.valueOf(-20.9));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("airTemp: must be greater than or equal to -20"));
  }

  @Test
  @Order(75)
  @DisplayName("75. Measure - POST with airTemp greater than the limit")
  void postWithAirTempGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setAirTemp(BigDecimal.valueOf(50.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("airTemp: must be less than or equal to 50"));
  }

  @Test
  @Order(76)
  @DisplayName("76. Measure - POST without airTemp")
  void postWithoutAirTemp() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setAirTemp(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("airTemp: must not be null"));
  }

  @Test
  @Order(77)
  @DisplayName("77. Measure - POST with gndTemp less than the limit")
  void postWithGndTempLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setGndTemp(BigDecimal.valueOf(-30.9));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("gndTemp: must be greater than or equal to -30"));
  }

  @Test
  @Order(78)
  @DisplayName("78. Measure - POST with gndTemp greater than the limit")
  void postWithGndTempGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setGndTemp(BigDecimal.valueOf(61.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("gndTemp: must be less than or equal to 60"));
  }

  @Test
  @Order(79)
  @DisplayName("79. Measure - POST without gndTemp")
  void postWithoutGndTemp() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setGndTemp(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("gndTemp: must not be null"));
  }

  @Test
  @Order(80)
  @DisplayName("80. Measure - POST with windSpeed less than the limit")
  void postWithWindSpeedLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setWindSpeed(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("windSpeed: must be greater than or equal to 0"));
  }

  @Test
  @Order(81)
  @DisplayName("81. Measure - POST with windSpeed greater than the limit")
  void postWithWindSpeedGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setWindSpeed(BigDecimal.valueOf(30.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("windSpeed: must be less than or equal to 30"));
  }

  @Test
  @Order(82)
  @DisplayName("82. Measure - POST without windSpeed")
  void postWithoutWindSpeed() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setWindSpeed(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("windSpeed: must not be null"));
  }

  @Test
  @Order(83)
  @DisplayName("83. Measure - POST with windDirection less than the limit")
  void postWithWindDirectionLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setWindDirection(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("windDirection: must be greater than or equal to 0"));
  }

  @Test
  @Order(84)
  @DisplayName("84. Measure - POST with windDirection greater than the limit")
  void postWithWindDirectionGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setWindDirection(BigDecimal.valueOf(360));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("windDirection: must be less than 360"));
  }

  @Test
  @Order(85)
  @DisplayName("85. Measure - POST without windDirection")
  void postWithoutWindDirection() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setWindDirection(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("windDirection: must not be null"));
  }

  @Test
  @Order(86)
  @DisplayName("86. Measure - POST with irradiance less than the limit")
  void postWithIrradianceLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setIrradiance(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("irradiance: must be greater than or equal to 0"));
  }

  @Test
  @Order(87)
  @DisplayName("87. Measure - POST with irradiance greater than the limit")
  void postWithIrradianceGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setIrradiance(BigDecimal.valueOf(1500.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("irradiance: must be less than or equal to 1500"));
  }

  @Test
  @Order(88)
  @DisplayName("88. Measure - POST without irradiance")
  void postWithoutIrradiance() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setIrradiance(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("irradiance: must not be null"));
  }

  @Test
  @Order(89)
  @DisplayName("89. Measure - POST with pressure less than the limit")
  void postWithPressureLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setPressure(BigDecimal.valueOf(99.9));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("pressure: must be greater than or equal to 100"));
  }

  @Test
  @Order(90)
  @DisplayName("90. Measure - POST with pressure greater than the limit")
  void postWithPressureGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setPressure(BigDecimal.valueOf(1200.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("pressure: must be less than or equal to 1200"));
  }

  @Test
  @Order(91)
  @DisplayName("91. Measure - POST without pressure")
  void postWithoutPressure() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setPressure(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("pressure: must not be null"));
  }

  @Test
  @Order(92)
  @DisplayName("92. Measure - POST with airHumidity less than the limit")
  void postWithAirHumidityLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setAirHumidity(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("airHumidity: must be greater than or equal to 0"));
  }

  @Test
  @Order(93)
  @DisplayName("93. Measure - POST with airHumidity greater than the limit")
  void postWithAirHumidityGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setAirHumidity(BigDecimal.valueOf(100.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("airHumidity: must be less than or equal to 100"));
  }

  @Test
  @Order(94)
  @DisplayName("94. Measure - POST without airHumidity")
  void postWithoutAirHumidity() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setAirHumidity(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("airHumidity: must not be null"));
  }

  @Test
  @Order(95)
  @DisplayName("95. Measure - POST with gndHumidity less than the limit")
  void postWithGndHumidityLessThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setGndHumidity(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("gndHumidity: must be greater than or equal to 0"));
  }

  @Test
  @Order(96)
  @DisplayName("96. Measure - POST with gndHumidity greater than the limit")
  void postWithGndHumidityGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setGndHumidity(BigDecimal.valueOf(100.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("gndHumidity: must be less than or equal to 100"));
  }

  @Test
  @Order(97)
  @DisplayName("97. Measure - POST without gndHumidity")
  void postWithoutGndHumidity() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setGndHumidity(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("gndHumidity: must not be null"));
  }

  @Test
  @Order(98)
  @DisplayName("98. Measure - POST with precipitation less than the limit")
  void postWithPrecipitationThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setPrecipitation(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("precipitation: must be greater than or equal to 0"));
  }

  @Test
  @Order(99)
  @DisplayName("99. Measure - POST with precipitation greater than the limit")
  void postWithPrecipitationGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setPrecipitation(BigDecimal.valueOf(1000.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("precipitation: must be less than or equal to 1000"));
  }

  @Test
  @Order(100)
  @DisplayName("100. Measure - POST without precipitation")
  void postWithoutPrecipitation() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setPrecipitation(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("precipitation: must not be null"));
  }

  @Test
  @Order(101)
  @DisplayName("101. Measure - POST with rainIntensity less than the limit")
  void postWithRainIntensityThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setRainIntensity(BigDecimal.valueOf(-0.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("rainIntensity: must be greater than or equal to 0"));
  }

  @Test
  @Order(102)
  @DisplayName("102. Measure - POST with rainIntensity greater than the limit")
  void postWithRainIntensityGreaterThanTheLimit() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setRainIntensity(BigDecimal.valueOf(1000.1));

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("rainIntensity: must be less than or equal to 1000"));
  }

  @Test
  @Order(103)
  @DisplayName("103. Measure - POST without rainIntensity")
  void postWithoutRainIntensity() throws Exception {
    resetMeasureDto();
    MEASURE_DTO.setRainIntensity(null);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnprocessableEntity())
        .andExpect(jsonPath("$.message").value("rainIntensity: must not be null"));
  }

  @Test
  @Order(104)
  @DisplayName("104. Measure - GET all measures")
  void getAllMeasures() throws Exception {
    mockMvc
        .perform(get("/measure")
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ids.get(MEASURE)))
        .andExpect(jsonPath("$[0].isleId").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(105)
  @DisplayName("105. Measure - GET measure by id")
  void getMeasureById() throws Exception {
    mockMvc
        .perform(get("/measure/" + ids.get(MEASURE))
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ids.get(MEASURE)))
        .andExpect(jsonPath("$.isleId").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(106)
  @DisplayName("106. Measure - GET measure by nonexisting id")
  void getMeasureByNonexistingId() throws Exception {
    mockMvc
        .perform(get("/measure/" + NONEXISTING_ID)
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Measure not found"));
  }

  @Test
  @Order(107)
  @DisplayName("107. Measure - GET all measures by isle id")
  void getAllMeasuresByIsleId() throws Exception {
    mockMvc
        .perform(get("/measure/isle/" + ids.get(ISLE_USERNAME))
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(ids.get(MEASURE)))
        .andExpect(jsonPath("$[0].isleId").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(108)
  @DisplayName("108. Measure - GET all measures by nonexisting isle id")
  void getAllMeasuresByNonexistingIsleId() throws Exception {
    mockMvc
        .perform(get("/measure/isle/" + NONEXISTING_ID)
            .headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(109)
  @DisplayName("109. Measure - PUT updating measure by its id")
  void putUpdatingMeasureByItsId() throws Exception {
    setHeadersWithTokenByLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

    resetMeasureDto();

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    mockMvc
        .perform(put("/measure/" + ids.get(MEASURE))
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(ids.get(MEASURE)))
        .andExpect(jsonPath("$.isleId").value(ids.get(ISLE_USERNAME)));
  }

  @Test
  @Order(110)
  @DisplayName("110. Measure - DELETE by id")
  void deleteById() throws Exception {
    resetMeasureDto();

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    String body = objectMapper.writeValueAsString(MEASURE_DTO);

    MvcResult mvcResult = mockMvc
        .perform(post("/measure")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.isleId").value(ids.get(ISLE_USERNAME)))
        .andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    String id = JsonPath.parse(contentAsString).read("$.id").toString();

    setHeadersWithTokenByLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

    mockMvc
        .perform(delete("/measure/" + id).headers(HTTP_HEADERS))
        .andExpect(status().isNoContent());
  }
}
