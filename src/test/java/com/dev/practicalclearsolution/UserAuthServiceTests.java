package com.dev.practicalclearsolution;

import com.dev.practicalclearsolution.entity.User;
import com.dev.practicalclearsolution.exception.ValidationException;
import com.dev.practicalclearsolution.repository.UserRepository;
import com.dev.practicalclearsolution.service.UserService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserAuthServiceTests {
    @Autowired
    UserService userService;
    @Value("${user.register.age.min}")
    int minAge;
    @Mock
    UserRepository userRepository;
    AutoCloseable mockClose;

    @BeforeAll
    void contextLoads() {
        mockClose = openMocks(this);
    }

    @Test
    void saveUser_AgeLessThanMinAge_ThrowValidationExceptionWithMessage() {
        User user = getValidUser();
        user.setDate(LocalDate.now().minusYears(minAge - 1));
        doReturn(user).when(userRepository).save(user);
        Exception validationException = assertThrows(ValidationException.class,
                () -> userService.save(user));
        String expectedMessage = "Age should be " + minAge + "+";
        String actualMessage = validationException.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void saveUser_ValidUser_ReturnSameUserWithId() {
        User user = getValidUser();
        User userWithId = getValidUser();
        userWithId.setId(1L);
        doReturn(userWithId).when(userRepository).save(user);
        ResponseEntity<User> save = userService.save(user);
        HttpStatusCode statusCode = save.getStatusCode();
        assertEquals(statusCode, HttpStatusCode.valueOf(200));
        User responseUser = save.getBody();
        assertNotNull(responseUser);
        assertEquals(responseUser.toString(), userWithId.toString());
    }

    @Test
    void saveUser_nullDateUser_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        user.setDate(null);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.save(user));
        assertEquals("Date should be not null", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void saveUser_nullNameUser_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        user.setName(null);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.save(user));
        assertEquals("Name should be not null", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void saveUser_nullSurnameUser_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        user.setSurname(null);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.save(user));
        assertEquals("Surname should be not null", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void saveUser_nullEmailUser_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        user.setEmail(null);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.save(user));
        assertEquals("Email should be not null", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void saveUser_notValidEmailUser_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        user.setEmail("e@com");
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.save(user));
        assertEquals("Email should be valid", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void saveUser_futureNotValidDateUser_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        user.setDate(LocalDate.now().plusDays(5));
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.save(user));
        assertEquals("Date should be at past time", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void updateUser_userNotExist_ThrowValidationExceptionMessageAndStatusBadRequest() {
        User user = getValidUser();
        doReturn(Optional.empty()).when(userRepository).findById(1L);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.update(1, user));
        assertEquals("User with id: 1 not found", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void deleteUser_userNotExist_ThrowValidationExceptionMessageAndStatusBadRequest() {
        doReturn(Optional.empty()).when(userRepository).findById(1L);
        ValidationException validationException =
                assertThrows(ValidationException.class, () -> userService.delete(1));
        assertEquals("User with id: 1 not found", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void findAllUsersByBirthdateInRange_date1FutureThanDate2_ThrowValidationExceptionMessageAndStatusBadRequest() {
        LocalDate future = LocalDate.now().plusDays(10);
        LocalDate now = LocalDate.now();
        ValidationException validationException =
                assertThrows(ValidationException.class,
                        () -> userService.findAllUsersByBirthdateInRange(future, now));
        assertEquals("Date 1 should be before Date 2", validationException.getMessage());
        int statusCodeFromSaveValue = validationException.getHttpStatus().value();
        int badRequestStatusCodeValue = HttpStatus.BAD_REQUEST.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    @Test
    void findAllUsersByBirthdateInRange_date1BeforeDate2_statusOk() {
        LocalDate future = LocalDate.now().plusDays(10);
        LocalDate now = LocalDate.now();
        doReturn(new ArrayList<User>())
                .when(userRepository)
                .findAllByDateBetween(now, future);
        ResponseEntity<List<User>> allUsersByBirthdateInRangeResponse =
                userService.findAllUsersByBirthdateInRange(now, future);
        int statusCodeFromSaveValue = allUsersByBirthdateInRangeResponse.getStatusCode().value();
        int badRequestStatusCodeValue = HttpStatus.OK.value();
        assertEquals(statusCodeFromSaveValue, badRequestStatusCodeValue);
    }

    private User getValidUser() {
        User user = new User();
        user.setEmail("test.mail@gmail.com");
        user.setDate(LocalDate.now().minusYears(minAge + 10));
        user.setAddress("Ukraine, Kyiv");
        user.setName("Viktor");
        user.setSurname("Shelest");
        user.setPhoneNumber("+380991234567");
        return user;
    }

    @AfterAll
    public void releaseMocks() throws Exception {
        mockClose.close();
    }
}
