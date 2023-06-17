package com.agrotechfields.measureshelter;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.agrotechfields.measureshelter.domain.Isle;
import com.agrotechfields.measureshelter.domain.Measure;
import com.agrotechfields.measureshelter.domain.Role;
import com.agrotechfields.measureshelter.domain.User;
import com.agrotechfields.measureshelter.dto.request.AuthDto;
import com.agrotechfields.measureshelter.dto.request.IsleDto;
import com.agrotechfields.measureshelter.dto.request.MeasureDto;
import com.agrotechfields.measureshelter.dto.request.UserDto;
import com.agrotechfields.measureshelter.dto.response.IsleResponseDto;
import com.agrotechfields.measureshelter.dto.response.MeasureResponseDto;
import com.agrotechfields.measureshelter.dto.response.TokenReponseDto;
import com.agrotechfields.measureshelter.dto.response.UserResponseDto;
import com.agrotechfields.measureshelter.exception.payload.ErrorPayload;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.HttpStatus;

@DisplayName("Unit tests not covered by integration tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MeasureshelterUnitTest {

  @Test
  @Order(1)
  @DisplayName("1. Testing AuthDto by noargs constructor")
  void testingAuthDtoByNoargsConstructor() {
    AuthDto dto = new AuthDto();
    dto.setUsername("somenone");
    dto.setPassword("somepass");

    assertEquals("somenone", dto.getUsername());
    assertEquals("somepass", dto.getPassword());
  }

  @Test
  @Order(2)
  @DisplayName("2. Testing AuthDto by constructor")
  void testingAuthDtoByConstructor() {
    AuthDto dto = new AuthDto("anotherUser", "anotherPass");
    assertEquals("anotherUser", dto.getUsername());
    assertEquals("anotherPass", dto.getPassword());
  }

  @Test
  @Order(3)
  @DisplayName("3. Testing UserDto by noargs constructor")
  void testingUserDtoByNoargsConstructor() {
    UserDto dto = new UserDto();
    dto.setUsername("someone");
    dto.setPassword("somepass");
    dto.setRole(Role.ROLE_ADMIN);

    assertEquals("someone", dto.getUsername());
    assertEquals("somepass", dto.getPassword());
    assertEquals(Role.ROLE_ADMIN, dto.getRole());
  }

  @Test
  @Order(4)
  @DisplayName("4. Testing UserDto by constructor")
  void testingUserDtoByConstructor() {
    UserDto dto = new UserDto("someone", "somepass");

    assertEquals("someone", dto.getUsername());
    assertEquals("somepass", dto.getPassword());
    assertEquals(Role.ROLE_USER, dto.getRole());
  }

  @Test
  @Order(5)
  @DisplayName("5. Testing IsleDto by noargs constructor")
  void testingIsleDtoByNoargsConstructor() {
    IsleDto dto = new IsleDto();
    dto.setSerialNumber("someserial");
    dto.setAltitude(BigDecimal.valueOf(1000));
    dto.setLatitude(BigDecimal.valueOf(20));
    dto.setLongitude(BigDecimal.valueOf(30));
    dto.setIsItWorking(true);
    dto.setSamplingInterval(10);

    assertEquals("someserial", dto.getSerialNumber());
    assertEquals(BigDecimal.valueOf(1000), dto.getAltitude());
    assertEquals(BigDecimal.valueOf(20), dto.getLatitude());
    assertEquals(BigDecimal.valueOf(30), dto.getLongitude());
    assertTrue(dto.getIsItWorking());
    assertEquals(10, dto.getSamplingInterval());
  }

  @Test
  @Order(6)
  @DisplayName("6. Testing IsleDto by constructor")
  void testingIsleDtoByConstructor() {
    IsleDto dto = new IsleDto("someserial", BigDecimal.valueOf(20), BigDecimal.valueOf(30),
        BigDecimal.valueOf(1000), true, 10);

    assertEquals("someserial", dto.getSerialNumber());
    assertEquals(BigDecimal.valueOf(1000), dto.getAltitude());
    assertEquals(BigDecimal.valueOf(20), dto.getLatitude());
    assertEquals(BigDecimal.valueOf(30), dto.getLongitude());
    assertTrue(dto.getIsItWorking());
    assertEquals(10, dto.getSamplingInterval());
  }

  @Test
  @Order(7)
  @DisplayName("7. Testing MeasureDto by noargs constructor")
  void testingMeasureDtoByNoargsConstructor() {
    MeasureDto dto = new MeasureDto();
    dto.setAirTemp(BigDecimal.valueOf(30));
    dto.setGndTemp(BigDecimal.valueOf(20));
    dto.setWindSpeed(BigDecimal.valueOf(5));
    dto.setWindDirection(BigDecimal.valueOf(180));
    dto.setIrradiance(BigDecimal.valueOf(1000));
    dto.setPressure(BigDecimal.valueOf(1014));
    dto.setAirHumidity(BigDecimal.valueOf(90));
    dto.setGndHumidity(BigDecimal.valueOf(80));
    dto.setPrecipitation(BigDecimal.valueOf(10));
    dto.setRainIntensity(BigDecimal.valueOf(2));

    assertEquals(BigDecimal.valueOf(30), dto.getAirTemp());
    assertEquals(BigDecimal.valueOf(20), dto.getGndTemp());
    assertEquals(BigDecimal.valueOf(5), dto.getWindSpeed());
    assertEquals(BigDecimal.valueOf(180), dto.getWindDirection());
    assertEquals(BigDecimal.valueOf(1000), dto.getIrradiance());
    assertEquals(BigDecimal.valueOf(1014), dto.getPressure());
    assertEquals(BigDecimal.valueOf(90), dto.getAirHumidity());
    assertEquals(BigDecimal.valueOf(80), dto.getGndHumidity());
    assertEquals(BigDecimal.valueOf(10), dto.getPrecipitation());
    assertEquals(BigDecimal.valueOf(2), dto.getRainIntensity());
    assertNotNull(dto.getTimestamp());
  }

  @Test
  @Order(8)
  @DisplayName("8. Testing MeasureDto by constructor")
  void testingMeasureDtoByConstructor() {
    MeasureDto dto = new MeasureDto(BigDecimal.valueOf(30), BigDecimal.valueOf(20),
        BigDecimal.valueOf(5), BigDecimal.valueOf(180), BigDecimal.valueOf(1000),
        BigDecimal.valueOf(1014), BigDecimal.valueOf(90), BigDecimal.valueOf(80),
        BigDecimal.valueOf(10), BigDecimal.valueOf(2));

    assertEquals(BigDecimal.valueOf(30), dto.getAirTemp());
    assertEquals(BigDecimal.valueOf(20), dto.getGndTemp());
    assertEquals(BigDecimal.valueOf(5), dto.getWindSpeed());
    assertEquals(BigDecimal.valueOf(180), dto.getWindDirection());
    assertEquals(BigDecimal.valueOf(1000), dto.getIrradiance());
    assertEquals(BigDecimal.valueOf(1014), dto.getPressure());
    assertEquals(BigDecimal.valueOf(90), dto.getAirHumidity());
    assertEquals(BigDecimal.valueOf(80), dto.getGndHumidity());
    assertEquals(BigDecimal.valueOf(10), dto.getPrecipitation());
    assertEquals(BigDecimal.valueOf(2), dto.getRainIntensity());
    assertNotNull(dto.getTimestamp());
  }

  @Test
  @Order(9)
  @DisplayName("9. Testing IsleResponseDto by noargs constructor")
  void testingIsleResponseDtoByNoargsConstructor() {
    IsleResponseDto dto = new IsleResponseDto();
    dto.setSerialNumber("someserial");
    dto.setAltitude(BigDecimal.valueOf(1000));
    dto.setLatitude(BigDecimal.valueOf(20));
    dto.setLongitude(BigDecimal.valueOf(30));
    dto.setIsItWorking(true);
    dto.setSamplingInterval(10);
    dto.setId("someid");

    assertEquals("someserial", dto.getSerialNumber());
    assertEquals(BigDecimal.valueOf(1000), dto.getAltitude());
    assertEquals(BigDecimal.valueOf(20), dto.getLatitude());
    assertEquals(BigDecimal.valueOf(30), dto.getLongitude());
    assertTrue(dto.getIsItWorking());
    assertEquals(10, dto.getSamplingInterval());
    assertEquals("someid", dto.getId());
  }

  @Test
  @Order(10)
  @DisplayName("10. Testing IsleResponseDto by constructor")
  void testingIsleResponseDtoByConstructor() {
    Isle isle = new Isle(new ObjectId(), "someserial", BigDecimal.valueOf(20),
        BigDecimal.valueOf(30), BigDecimal.valueOf(1000), true, 10);

    IsleResponseDto dto = new IsleResponseDto(isle);

    assertEquals("someserial", dto.getSerialNumber());
    assertEquals(BigDecimal.valueOf(1000), dto.getAltitude());
    assertEquals(BigDecimal.valueOf(20), dto.getLatitude());
    assertEquals(BigDecimal.valueOf(30), dto.getLongitude());
    assertTrue(dto.getIsItWorking());
    assertEquals(10, dto.getSamplingInterval());
    assertNotNull(dto.getId());
  }

  @Test
  @Order(11)
  @DisplayName("11. Testing MeasureResponseDto by constructor")
  void testingMeasureResponseDtoByConstructor() {
    Measure measure = new Measure();
    measure.setAirTemp(BigDecimal.valueOf(30));
    measure.setGndTemp(BigDecimal.valueOf(20));
    measure.setWindSpeed(BigDecimal.valueOf(5));
    measure.setWindDirection(BigDecimal.valueOf(180));
    measure.setIrradiance(BigDecimal.valueOf(1000));
    measure.setPressure(BigDecimal.valueOf(1014));
    measure.setAirHumidity(BigDecimal.valueOf(90));
    measure.setGndHumidity(BigDecimal.valueOf(80));
    measure.setPrecipitation(BigDecimal.valueOf(10));
    measure.setRainIntensity(BigDecimal.valueOf(2));
    measure.setTimestamp(LocalDateTime.now());
    measure.setId(new ObjectId());
    measure.setIsleId(new ObjectId());

    MeasureResponseDto dto = new MeasureResponseDto(measure);

    assertEquals(BigDecimal.valueOf(30), dto.getAirTemp());
    assertEquals(BigDecimal.valueOf(20), dto.getGndTemp());
    assertEquals(BigDecimal.valueOf(5), dto.getWindSpeed());
    assertEquals(BigDecimal.valueOf(180), dto.getWindDirection());
    assertEquals(BigDecimal.valueOf(1000), dto.getIrradiance());
    assertEquals(BigDecimal.valueOf(1014), dto.getPressure());
    assertEquals(BigDecimal.valueOf(90), dto.getAirHumidity());
    assertEquals(BigDecimal.valueOf(80), dto.getGndHumidity());
    assertEquals(BigDecimal.valueOf(10), dto.getPrecipitation());
    assertEquals(BigDecimal.valueOf(2), dto.getRainIntensity());
    assertNotNull(dto.getTimestamp());
    assertNotNull(dto.getId());
    assertNotNull(dto.getIsleId());
  }

  @Test
  @Order(12)
  @DisplayName("12. Testing UserResponseDto by constructor")
  void testingUserResponseDtoByConstructor() {
    User user = new User();
    user.setId(new ObjectId());
    user.setUsername("someone");
    user.setPassword("somepass");
    user.setRole(Role.ROLE_ADMIN);

    UserResponseDto dto = new UserResponseDto(user);
    assertEquals("someone", dto.getUsername());
    assertEquals(Role.ROLE_ADMIN, dto.getRole());
    assertNotNull(dto.getId());
    assertTrue(dto.isEnable());
    assertTrue(dto.isAccountNonExpired());
    assertTrue(dto.isAccountNonLocked());
    assertTrue(dto.isCredentialsNonExpired());
  }

  @Test
  @Order(13)
  @DisplayName("13. Testing TokenResponseDto by noargs constructor")
  void testingTokenResponseDtoByNoargsConstructor() {
    TokenReponseDto dto = new TokenReponseDto();
    dto.setToken("token");

    assertEquals("token", dto.getToken());
  }

  @Test
  @Order(14)
  @DisplayName("14. Testing TokenResponseDto by constructor")
  void testingTokenResponseDtoByConstructor() {
    TokenReponseDto dto = new TokenReponseDto("token");

    assertEquals("token", dto.getToken());
  }

  @Test
  @Order(15)
  @DisplayName("15. Testing User by no args constructor")
  void testingUserByConstructor() {
    User user = new User();
    ObjectId id = new ObjectId();

    user.setId(id);
    user.setUsername("someone");
    user.setPassword("somepass");
    user.setRole(Role.ROLE_ADMIN);

    User anotherUser = new User();
    anotherUser.setId(id);

    assertEquals("someone", user.getUsername());
    assertEquals("somepass", user.getPassword());
    assertEquals(Role.ROLE_ADMIN, user.getRole());
    assertNotNull(user.getId());
    assertTrue(user.isEnabled());
    assertTrue(user.isAccountNonExpired());
    assertTrue(user.isAccountNonLocked());
    assertTrue(user.isCredentialsNonExpired());
    assertNotNull(user.getAuthorities());
    assertNotNull(user.hashCode());
    assertTrue(user.equals(user));
    assertEquals(user, anotherUser);
    assertNotEquals(user, 0);
  }

  @Test
  @Order(16)
  @DisplayName("16. Testing User by constructor")
  void testingUserByNoargsConstructor() {
    User user = new User(new ObjectId(), "someone", "somepass", Role.ROLE_ADMIN);
    user.setAccountNonExpired(false);
    user.setAccountNonLocked(false);
    user.setCredentialsNonExpired(false);
    user.setEnabled(false);

    assertEquals("someone", user.getUsername());
    assertEquals("somepass", user.getPassword());
    assertEquals(Role.ROLE_ADMIN, user.getRole());
    assertNotNull(user.getId());
    assertFalse(user.isEnabled());
    assertFalse(user.isAccountNonExpired());
    assertFalse(user.isAccountNonLocked());
    assertFalse(user.isCredentialsNonExpired());
    assertNotNull(user.getAuthorities());
  }

  @Test
  @Order(17)
  @DisplayName("17. Testing Isle by no args constructor")
  void testingIsleByNoargsConstructor() {
    Isle isle = new Isle();
    ObjectId id = new ObjectId();

    isle.setId(id);
    isle.setSerialNumber("someserial");
    isle.setLatidude(BigDecimal.valueOf(20));
    isle.setLongitude(BigDecimal.valueOf(30));
    isle.setAltitude(BigDecimal.valueOf(1000));
    isle.setSamplingInterval(10);
    isle.setIsItWorking(false);

    Isle anotherIsle = new Isle();
    anotherIsle.setSerialNumber("someserial");

    assertEquals("someserial", isle.getSerialNumber());
    assertEquals(BigDecimal.valueOf(20), isle.getLatitude());
    assertEquals(BigDecimal.valueOf(30), isle.getLongitude());
    assertEquals(BigDecimal.valueOf(1000), isle.getAltitude());
    assertEquals(10, isle.getSamplingInterval());
    assertFalse(isle.getIsItWorking());
    assertNotNull(isle.getId());
    assertNotNull(isle.hashCode());
    assertEquals(isle, isle);
    assertEquals(isle, anotherIsle);
    assertNotEquals(isle, 0);
  }

  @Test
  @Order(18)
  @DisplayName("18. Testing Isle by constructor")
  void testingIsleByConstructor() {
    Isle isle = new Isle(new ObjectId(), "someserial", BigDecimal.valueOf(20),
        BigDecimal.valueOf(30), BigDecimal.valueOf(1000), false, 10);

    assertEquals("someserial", isle.getSerialNumber());
    assertEquals(BigDecimal.valueOf(20), isle.getLatitude());
    assertEquals(BigDecimal.valueOf(30), isle.getLongitude());
    assertEquals(BigDecimal.valueOf(1000), isle.getAltitude());
    assertEquals(10, isle.getSamplingInterval());
    assertFalse(isle.getIsItWorking());
    assertNotNull(isle.getId());
  }

  @Test
  @Order(19)
  @DisplayName("19. Testing Measure by no args constructor")
  void testingMeasureByNoargsConstructor() {
    Measure measure = new Measure();
    ObjectId id = new ObjectId();
    measure.setId(id);
    measure.setIsleId(id);
    measure.setAirTemp(BigDecimal.valueOf(30));
    measure.setGndTemp(BigDecimal.valueOf(20));
    measure.setWindSpeed(BigDecimal.valueOf(5));
    measure.setWindDirection(BigDecimal.valueOf(180));
    measure.setIrradiance(BigDecimal.valueOf(1000));
    measure.setPressure(BigDecimal.valueOf(1014));
    measure.setAirHumidity(BigDecimal.valueOf(90));
    measure.setGndHumidity(BigDecimal.valueOf(80));
    measure.setPrecipitation(BigDecimal.valueOf(10));
    measure.setRainIntensity(BigDecimal.valueOf(2));
    measure.setTimestamp(LocalDateTime.now());

    Measure anotherMeasure = new Measure();
    anotherMeasure.setId(id);

    assertEquals(BigDecimal.valueOf(30), measure.getAirTemp());
    assertEquals(BigDecimal.valueOf(20), measure.getGndTemp());
    assertEquals(BigDecimal.valueOf(5), measure.getWindSpeed());
    assertEquals(BigDecimal.valueOf(180), measure.getWindDirection());
    assertEquals(BigDecimal.valueOf(1000), measure.getIrradiance());
    assertEquals(BigDecimal.valueOf(1014), measure.getPressure());
    assertEquals(BigDecimal.valueOf(90), measure.getAirHumidity());
    assertEquals(BigDecimal.valueOf(80), measure.getGndHumidity());
    assertEquals(BigDecimal.valueOf(10), measure.getPrecipitation());
    assertEquals(BigDecimal.valueOf(2), measure.getRainIntensity());
    assertNotNull(measure.getTimestamp());
    assertNotNull(measure.getId());
    assertNotNull(measure.getIsleId());
    assertNotNull(measure.hashCode());
    assertTrue(measure.equals(measure));
    assertEquals(measure, anotherMeasure);
    assertNotEquals(measure, 0);
  }

  @Test
  @Order(20)
  @DisplayName("20. Testing Measure by constructor")
  void testingMeasureByConstructor() {
    Measure measure = new Measure(new ObjectId(), new ObjectId(), BigDecimal.valueOf(30),
        BigDecimal.valueOf(20), BigDecimal.valueOf(5), BigDecimal.valueOf(180),
        BigDecimal.valueOf(1000), BigDecimal.valueOf(1014), BigDecimal.valueOf(90),
        BigDecimal.valueOf(80), BigDecimal.valueOf(10), BigDecimal.valueOf(2), LocalDateTime.now());

    assertEquals(BigDecimal.valueOf(30), measure.getAirTemp());
    assertEquals(BigDecimal.valueOf(20), measure.getGndTemp());
    assertEquals(BigDecimal.valueOf(5), measure.getWindSpeed());
    assertEquals(BigDecimal.valueOf(180), measure.getWindDirection());
    assertEquals(BigDecimal.valueOf(1000), measure.getIrradiance());
    assertEquals(BigDecimal.valueOf(1014), measure.getPressure());
    assertEquals(BigDecimal.valueOf(90), measure.getAirHumidity());
    assertEquals(BigDecimal.valueOf(80), measure.getGndHumidity());
    assertEquals(BigDecimal.valueOf(10), measure.getPrecipitation());
    assertEquals(BigDecimal.valueOf(2), measure.getRainIntensity());
    assertNotNull(measure.getTimestamp());
    assertNotNull(measure.getId());
    assertNotNull(measure.getIsleId());
  }

  @Test
  @Order(21)
  @DisplayName("21. Testing ErrorPayload by constructor")
  void testingErrorPayloadByConstructor() {
    ErrorPayload error = new ErrorPayload("somemessage", HttpStatus.BAD_REQUEST);

    assertEquals("somemessage", error.getMessage());
    assertEquals("BAD_REQUEST", error.getError());
    assertEquals(400, error.getStatus());
    assertNotNull(error.getTimestamp());
  }
}
