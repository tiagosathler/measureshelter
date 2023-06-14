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
        .andExpect(status().isCreated()).andExpect(jsonPath("$.id").isString());
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