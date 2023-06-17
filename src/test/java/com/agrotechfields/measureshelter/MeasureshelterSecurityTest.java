package com.agrotechfields.measureshelter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.agrotechfields.measureshelter.dto.request.UserDto;
import com.agrotechfields.measureshelter.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasureshelterSecurityTest {
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
  public void beforeEach() {}

  @AfterEach
  public void afterEach() {}

  private static final String ADMIN_USERNAME = "admin";
  private static final String ADMIN_PASSWORD = "password";

  private static final String USER_USERNAME = "user";
  private static final String USER_PASSWORD = "password";

  private static final String ISLE_USERNAME = "ISLE000001";
  private static final String ISLE_PASSWORD = "password";

  private static final HttpHeaders HTTP_HEADERS = new HttpHeaders();

  private static final List<User> USERS = new ArrayList<>();

  private static String token;


  private void insertUsersIntoTheDb() {
    String adminEncodedPass = passwordEncoder.encode(ADMIN_PASSWORD);
    User adminUser = new User(null, ADMIN_USERNAME, adminEncodedPass, Role.ROLE_ADMIN);

    String commomUserEncodedPass = passwordEncoder.encode(USER_PASSWORD);
    User commonUser = new User(null, USER_USERNAME, commomUserEncodedPass, Role.ROLE_USER);

    String isleUserEncodedPass = passwordEncoder.encode(ISLE_PASSWORD);
    User isleUser = new User(null, ISLE_USERNAME, isleUserEncodedPass, Role.ROLE_ISLE);

    userRepository.saveAll(List.of(adminUser, commonUser, isleUser)).forEach(USERS::add);
  }

  private void setHeadersWithTokenByLogin(String username, String password) throws Exception {
    AuthDto authDto = new AuthDto(username, password);
    String body = objectMapper.writeValueAsString(authDto);

    MvcResult mvcResult = mockMvc
        .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body)).andReturn();

    String contentAsString = mvcResult.getResponse().getContentAsString();

    token = JsonPath.parse(contentAsString).read("$.token").toString();

    HTTP_HEADERS.setBearerAuth(token);
  }

  @Test
  @Order(1)
  @DisplayName("1. /actuator - forbidden access for ROLE_USER and ROLE_ISLE")
  void actuatorForbiddenAccessForUserAndIsleRoles() throws Exception {
    insertUsersIntoTheDb();

    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(get("/actuator").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(get("/actuator").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(2)
  @DisplayName("2. /user - POST - forbidden access for ROLE_USER and ROLE_ISLE")
  void userPostForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(post("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(post("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(3)
  @DisplayName("3. /user - GET - forbidden access for ROLE_ISLE")
  void userGetForbiddenForIsleRole() throws Exception {
    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(get("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(4)
  @DisplayName("4. /user/isle - PUT - forbidden access for ROLE_USER and ROLE_ISLE")
  void userIslePutForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(put("/user/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(post("/user/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(5)
  @DisplayName("5. /user - PUT - forbidden access for ROLE_ISLE")
  void userPutForbiddenForIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(post("/user/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(6)
  @DisplayName("6. /user - PATCH - forbidden access for ROLE_USER and ROLE_ISLE")
  void userPatchForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(patch("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(patch("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(7)
  @DisplayName("7. /user - DELETE - forbidden access for ROLE_USER and ROLE_ISLE")
  void userDeleteForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(delete("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(delete("/user").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(8)
  @DisplayName("8. /isle - POST - forbidden access for ROLE_USER and ROLE_ISLE")
  void islePostForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(post("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(post("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(9)
  @DisplayName("9. /isle - PUT - forbidden access for ROLE_USER and ROLE_ISLE")
  void islePutForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(put("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(put("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(10)
  @DisplayName("10. /isle - PATCH - forbidden access for ROLE_ISLE")
  void islePatchForbiddenForIsleRole() throws Exception {
    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(patch("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(11)
  @DisplayName("11. /isle - DELETE - forbidden access for ROLE_USER and ROLE_ISLE")
  void isleDeleteForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(delete("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(delete("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(12)
  @DisplayName("12. /measure - POST - forbidden access for ROLE_USER and ROLE_ADMIN")
  void measurePostForbiddenForUserAndAdminRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(post("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

    mockMvc
    .perform(post("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(13)
  @DisplayName("13. /measure - PUT - forbidden access for ROLE_USER and ROLE_ISLE")
  void measurePutForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(put("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(put("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(14)
  @DisplayName("14. /measure - DELETE - forbidden access for ROLE_USER and ROLE_ISLE")
  void measureDeleteForbiddenForUserAndIsleRoles() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(delete("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());

    setHeadersWithTokenByLogin(ISLE_USERNAME, ISLE_PASSWORD);

    mockMvc
      .perform(delete("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden());
  }

  @Test
  @Order(15)
  @DisplayName("15. /isle - GET - forbidden access for unauthenticated")
  void isleGetForbiddenForUnauthenticated() throws Exception {
    HTTP_HEADERS.setBearerAuth("");

    mockMvc
      .perform(get("/isle").headers(HTTP_HEADERS))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value("Invalid token"));  }

  @Test
  @Order(16)
  @DisplayName("16. /measure - GET - forbidden access for unauthenticated")
  void measureGetForbiddenForUnauthenticated() throws Exception {
    HTTP_HEADERS.setBearerAuth("");

    mockMvc
      .perform(get("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isBadRequest())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value("Invalid token"));
  }

  @Test
  @Order(16)
  @DisplayName("16. /measure - GET - forbidden access for expired token")
  void measureGetForbiddenForExpiredToken() throws Exception {
    HTTP_HEADERS.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"
        + ".eyJpc3MiOiJBZ3JvX1RlY2hmaWVsZHMiLCJpYXQiOjE2Mj"
        + "M4ODU2NjcsInN1YiI6ImFkbWluIiwiZXhwIjoxNjIzOTcyMDY3fQ"
        + ".y9_AtV7RRqpaugqvtNtbfbvb3ZtZeYOhsQVl3G1uQ8I");

    mockMvc
      .perform(get("/measure").headers(HTTP_HEADERS))
      .andExpect(status().isForbidden())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.message").value("The Token has expired on 2021-06-17T23:21:07Z."));
  }

  @Test
  @Order(17)
  @DisplayName("17. /login - POST - Unauthorized access for disabled user")
  void loginPostUnauthorizedForDisabledUser() throws Exception {
    User adminUser = USERS.get(0);
    adminUser.setEnabled(false);
    userRepository.save(adminUser);

    UserDto userDto = new UserDto(ADMIN_USERNAME, ADMIN_PASSWORD);
    
    String body = objectMapper.writeValueAsString(userDto);
    mockMvc
      .perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(body))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isUnauthorized())
      .andExpect(jsonPath("$.message").value("User is disabled"));
  }

  @Test
  @Order(18)
  @DisplayName("18. /user - GET - token with user not found due to being recently deleted")
  void userGetTokenWithUserNotFoundDueToBeingRecentlyDeleted() throws Exception {
    User adminUser = USERS.get(0);
    adminUser.setEnabled(true);
    userRepository.save(adminUser);

    setHeadersWithTokenByLogin(ADMIN_USERNAME, ADMIN_PASSWORD);

    userRepository.delete(adminUser);

    mockMvc
      .perform(get("/user").headers(HTTP_HEADERS))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Username '" + ADMIN_USERNAME + "' provided by this token not found"));
  }

  @Test
  @Order(19)
  @DisplayName("19. /measure - PATCH - method not implemented")
  void measurePatchMethodNotImplemented() throws Exception {
    setHeadersWithTokenByLogin(USER_USERNAME, USER_PASSWORD);

    mockMvc
      .perform(patch("/measure").headers(HTTP_HEADERS))
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isNotImplemented())
      .andExpect(jsonPath("$.message").value("Request method 'PATCH' is not supported"));
  }
}
