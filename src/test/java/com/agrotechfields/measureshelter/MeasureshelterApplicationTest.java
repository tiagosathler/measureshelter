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

  static final String ADMIN_USERNAME = "admin";
  static final String ADMIN_PASSWORD = "pass";
  static String id;
  static String nextId;

  static private String token;
  static private final HttpHeaders HTTP_HEADERS = new HttpHeaders();

  private void insertAnAdminUserIntoTheDb() {
    String encodedPassword = passwordEncoder.encode(ADMIN_PASSWORD);
    User user = new User(null, ADMIN_USERNAME, encodedPassword, Role.ROLE_ADMIN);
    userRepository.save(user);
  }

  private void setHeadersWithTokenByLogin(String username, String password)
      throws Exception {
    AuthDto authDto = new AuthDto(username, password);
    String body = objectMapper.writeValueAsString(authDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    TokenReponseDto tokenResponseDto = objectMapper.readValue(contentAsString, TokenReponseDto.class);
    token = tokenResponseDto.getToken();

    System.out.println("======>> test token: " + token);

    HTTP_HEADERS.setBearerAuth(token);
  }

  @Test
  @Order(1)
  @DisplayName("1. Login using a valid username")
  void loginUsingAValidUsername() throws Exception {
    insertAnAdminUserIntoTheDb();

    AuthDto authDto = new AuthDto(ADMIN_USERNAME, ADMIN_PASSWORD);
    String body = objectMapper.writeValueAsString(authDto);

    mockMvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
        .andExpect(jsonPath("$.payload").isNotEmpty());
  }

  @Test
  @Order(2)
  @DisplayName("2. Login using an invalid username")
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
  @DisplayName("3. Login using an invalid password")
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
  @DisplayName("4. Login without an username")
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
  @DisplayName("5. Login without a password")
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
  @DisplayName("6. Isle - create a valid Isle")
  void createAValidIsle() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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

    IsleResponseDto isleResponseDto =
        objectMapper.readValue(contentAsString, IsleResponseDto.class);

    id = isleResponseDto.getId();
  }

  @Test
  @Order(7)
  @DisplayName("7. Isle - post with an invalid serial number")
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
  @DisplayName("8. Isle - post without a serial number")
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
  @DisplayName("9. Isle - post with a latitude greater than the limit")
  void postWithALatitudeGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("10. Isle - post with a latitude less than the limit")
  void postWithALatitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("11. Isle - post without a latitude")
  void postWithoutALatitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("12. Isle - post with a longitude greater than the limit")
  void postWithALongitudeGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("13. Isle - post with a longitude less than the limit")
  void postWithALongitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("14. Isle - post without a longitude")
  void postWithoutALongitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("15. Isle - post with an altitude less than the limit")
  void postWithAnAltitudeLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("16. Isle - post without an altitude")
  void postWithoutAnAltitude() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("17. Isle - post with a sampling interval greater than the limit")
  void postWithASamplingIntervalGreaterThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("18. Isle - post with a sampling interval less than the limit")
  void postWithASamplingIntervalLessThanTheLimit() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
  @DisplayName("19. Isle - post with an existing serial number")
  void postWithAnExistingSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
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
        .andExpect(jsonPath("$[0].serialNumber").value("0000000001"))
        .andExpect(jsonPath("$[0].id").isNotEmpty());
  }

  @Test
  @Order(21)
  @DisplayName("21. Isle - GET isle by serial number")
  void getIsleBySerialNumber() throws Exception {
    mockMvc
        .perform(get("/isle/serial/0000000001").headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.serialNumber").value("0000000001"))
        .andExpect(jsonPath("$.id").isNotEmpty());
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
        .perform(get("/isle/" + id).headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.serialNumber").value("0000000001"))
        .andExpect(jsonPath("$.id").isNotEmpty());
  }

  @Test
  @Order(24)
  @DisplayName("24. Isle - GET isle by nonexisting id")
  void getIsleByNonexistingId() throws Exception {
    mockMvc
        .perform(get("/isle/648a5072cbe534d1d321f28d").headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(25)
  @DisplayName("25. Isle - GET isle invalid id")
  void getIsleByInvalidId() throws Exception {
    mockMvc
        .perform(get("/isle/648a5072c").headers(HTTP_HEADERS))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("648a5072c is invalid Id"));
  }

  @Test
  @Order(26)
  @DisplayName("26. Isle - PUT by isle id with same serial number")
  void putByIsleIdWithSameSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(100));

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(put("/isle/" + id)
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.serialNumber").value("0000000001"))
        .andExpect(jsonPath("$.altitude").value("100"));
  }

  @Test
  @Order(27)
  @DisplayName("27. Isle - PUT by isle id with existing serial number")
  void putByIsleIdWithExistingSerialNumber() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000002");
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
        .perform(put("/isle/" + id)
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Isle already exists"));

    String contentAsString = mvcResult.getResponse().getContentAsString();

    IsleResponseDto isleResponseDto =
        objectMapper.readValue(contentAsString, IsleResponseDto.class);

    nextId = isleResponseDto.getId();
  }

  @Test
  @Order(28)
  @DisplayName("28. Isle - PUT by nonexisting isle id")
  void putByNonexistingIsleId() throws Exception {
    IsleDto isleDto = new IsleDto();
    isleDto.setSerialNumber("0000000001");
    isleDto.setLatitude(BigDecimal.valueOf(21.00));
    isleDto.setLongitude(BigDecimal.valueOf(20.00));
    isleDto.setAltitude(BigDecimal.valueOf(100));

    String body = objectMapper.writeValueAsString(isleDto);

    mockMvc
        .perform(put("/isle/648a5072cbe534d1d321f28d")
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
        .perform(put("/isle/648a5072")
            .headers(HTTP_HEADERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("648a5072 is invalid Id"));
  }

  @Test
  @Order(30)
  @DisplayName("30. Isle - DELETE by isle id")
  void deleteByIsleId() throws Exception {
    mockMvc
        .perform(delete("/isle/" + nextId).headers(HTTP_HEADERS))
        .andExpect(status().isNoContent());
  }

  @Test
  @Order(31)
  @DisplayName("31. Isle - DELETE by nonexisting isle id")
  void deleteByNonexistingIsleId() throws Exception {
    mockMvc
        .perform(delete("/isle/648a5072cbe534d1d321f28d").headers(HTTP_HEADERS))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(32)
  @DisplayName("32. Isle - DELETE by invalid isle id")
  void deleteByInvalidIsleId() throws Exception {
    mockMvc
        .perform(delete("/isle/648a5072").headers(HTTP_HEADERS))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("648a5072 is invalid Id"));
  }

  @Test
  @Order(33)
  @DisplayName("33. Isle - PATCH by isle id toggling working to false")
  void patchByIsleIdTogglingWorkingToFalse() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/" + id).headers(HTTP_HEADERS))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.isItWorking").value("false"));
  }

  @Test
  @Order(34)
  @DisplayName("34. Isle - PATCH by isle id toggling working to true")
  void patchByIsleIdTogglingWorkingToTrue() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/" + id).headers(HTTP_HEADERS))
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.isItWorking").value("true"));
  }

  @Test
  @Order(35)
  @DisplayName("35. Isle - PATCH by nonexisting isle id")
  void patchByNonexistingIsleId() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/648a5072cbe534d1d321f28d").headers(HTTP_HEADERS))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Isle not found"));
  }

  @Test
  @Order(36)
  @DisplayName("36. Isle - PATCH by invalid isle id")
  void patchByInvalidIsleId() throws Exception {
    mockMvc
        .perform(patch("/isle/toggle/647912").headers(HTTP_HEADERS))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("647912 is invalid Id"));
  }
}
