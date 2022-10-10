package service;

import data.UsersRepository;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    UserServiceImpl userService;
    @Mock
    UsersRepository usersRepository;
    @Mock
    EmailVerificationService emailVerificationService;

    String firstName;
    String lastName;
    String email;
    String password;
    String repeatPassword;

    @BeforeEach
    void init() {
//        userService = new UserServiceImpl();
        lastName = "Stoba";
        firstName = "Mike";
        email = "chigov@gmail.com";
        password = "138500";
        repeatPassword = "138500";
    }

    @DisplayName("User object created")
    @Test
    void testCreateUser_whenUserDetailsProvided_returnUserObject() {
//        arrange
//        Mockito.when(usersRepository.save(Mockito.any(User.class))).thenReturn(true);
        Mockito.when(usersRepository.save(any(User.class))).thenReturn(true);
//        act
        User user = userService.createUser(firstName, lastName, email, password, repeatPassword);

//        assert
        assertNotNull(user, "createUser method should not have return null");

        assertEquals(firstName, user.getFirstName(), "User's first name is incorrect.");
        assertEquals(lastName, user.getLastName(), "User's last name is incorrect.");
        assertEquals(email, user.getEmail(), "User's email is incorrect.");
        assertNotNull(user.getUserId(), "userId should not have return null");
        Mockito.verify(usersRepository, times(1)).save(any(User.class));

    }

    @DisplayName("First name is missing -> IllegalArgumentException")
    @Test
    void testCreateUser_whenOneArgumentIsMissing_ThrowException() {
        //arrange
        firstName = "";
        String expectedException = "User's first name is empty";

        //act and assert
        IllegalArgumentException thrownException = Assertions.assertThrows(IllegalArgumentException.class, () -> {

            userService.createUser(firstName, lastName, email, password, repeatPassword);

        }, "Empty first name should have caused an exception");

        //assert
        assertEquals(expectedException, thrownException.getMessage(), "Exception error message is not correct");
    }

    @DisplayName("Last name is missing -> IllegalArgumentException")
    @Test
    void testCreateUser_whenLastNameIsMissing_ThrowException() {
        //arrange
        lastName = "";
        String expectedException = "User's last name is empty";

        //act and assert
        IllegalArgumentException thrownException = Assertions.assertThrows(IllegalArgumentException.class, () -> {

            userService.createUser(firstName, lastName, email, password, repeatPassword);

        }, "Empty last name should have caused an exception");

        //assert
        assertEquals(expectedException, thrownException.getMessage(), "Exception error message is not correct");
    }

    @DisplayName("if save() causes RuntimeException, UserServiceException is thrown")
    @Test
    void testCreateUser_whenSaveMethodThrowsException_thenThrowsUserServiceException() {

        //arrange
        when(usersRepository.save(any(User.class))).thenThrow(RuntimeException.class);

        //act and assert
        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatPassword);
        }, "Should have thrown UserServiceException instead");

    }

    @DisplayName("EmailNotificationException is handled")
    @Test
    void testCreateUser_whenEmailNotificationExceptionThrown_throwUserException() {
        //arrange
        when(usersRepository.save(any(User.class))).thenReturn(true);
//        when(emailVerificationService.scheduleEmailConfirmation(any(User.class))).thenThrow(); //when does not work with void

        doThrow(EmailVerificationServiceException.class)
                .when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

//        doNothing().when(emailVerificationService).scheduleEmailConfirmation(any(User.class));

        //act and assert
        assertThrows(UserServiceException.class, () -> {
            userService.createUser(firstName, lastName, email, password, repeatPassword);
        }, "Should have thrown UserServiceException");

        verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
    }


}
