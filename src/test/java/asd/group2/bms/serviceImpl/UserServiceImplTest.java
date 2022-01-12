package asd.group2.bms.serviceImpl;

import asd.group2.bms.model.account.AccountType;
import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.RoleType;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.request.SignUpRequest;
import asd.group2.bms.payload.request.UpdateProfileRequest;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.UserProfile;
import asd.group2.bms.repository.IRoleRepository;
import asd.group2.bms.repository.IUserRepository;
import asd.group2.bms.security.UserPrincipal;
import asd.group2.bms.service.ICustomEmail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  IRoleRepository roleRepository;

  @Mock
  IUserRepository userRepository;

  @Mock
  PasswordEncoder passwordEncoder;

  @Mock
  ICustomEmail customEmail;

  @InjectMocks
  UserServiceImpl userService;

  @BeforeEach
  public void setup() {
    HttpServletRequest mockRequest = new MockHttpServletRequest();
    ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockRequest);
    RequestContextHolder.setRequestAttributes(servletRequestAttributes);
    MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  public void teardown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void isEmailAvailableTrueTest() {
    String email = "random@dal.ca";

    when(userRepository.existsByEmail(email)).thenReturn(false);

    Boolean isAvailable = userService.isEmailAvailable(email);

    assertTrue(isAvailable, "False was returned");
  }

  @Test
  void isEmailAvailableFalseTest() {
    String email = "harsh.bhatt@dal.ca";

    when(userRepository.existsByEmail(email)).thenReturn(true);

    Boolean isAvailable = userService.isEmailAvailable(email);

    assertFalse(isAvailable, "True was returned");
  }

  @Test
  void isUsernameAvailableTrueTest() {
    String username = "random_name";

    when(userRepository.existsByUsername(username)).thenReturn(false);

    Boolean isAvailable = userService.isUsernameAvailable(username);

    assertTrue(isAvailable, "False was returned");
  }

  @Test
  void isUsernameAvailableFalseTest() {
    String username = "harsh";

    when(userRepository.existsByUsername(username)).thenReturn(true);

    Boolean isAvailable = userService.isUsernameAvailable(username);

    assertFalse(isAvailable, "True was returned");
  }

  @Test
  void createUserTest() {
    String username = "test__harsh";
    String email = "test__harsh.bhatt@dal.ca";

    Role role = new Role();

    when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(java.util.Optional.of(role));

    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setFirstName("f_name");
    signUpRequest.setLastName("l_name");
    signUpRequest.setUsername(username);
    signUpRequest.setEmail(email);
    signUpRequest.setBirthday(new Date());
    signUpRequest.setPhone("9876543210");
    signUpRequest.setAddress("demo address");
    signUpRequest.setCity("city");
    signUpRequest.setState("state");
    signUpRequest.setZipCode("369852");
    signUpRequest.setPassword("demo1234");
    signUpRequest.setRequestedAccountType(AccountType.SAVINGS);
    signUpRequest.setRole(RoleType.ROLE_USER);

    when(userRepository.save(any(User.class))).then(returnsFirstArg());

    ResponseEntity<?> responseEntity = userService.createUser(signUpRequest);

    assertEquals(HttpStatus.CREATED.toString(),
        responseEntity.getStatusCode().toString(),
        "User does not created");
  }

  @Test
  void createUserTestUsernameDoesNotExist() {
    String username = "test__harsh";
    String email = "test__harsh.bhatt@dal.ca";

    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setUsername(username);
    signUpRequest.setEmail(email);
    signUpRequest.setRole(RoleType.ROLE_USER);

    when(userRepository.existsByUsername(username)).thenReturn(true);

    ResponseEntity<?> responseEntity = userService.createUser(signUpRequest);

    assertEquals(HttpStatus.BAD_REQUEST.toString(),
        responseEntity.getStatusCode().toString(),
        "User find by username test failed");
  }

  @Test
  void createUserTestEmailDoesNotExist() {
    String username = "test__harsh";
    String email = "test__harsh.bhatt@dal.ca";

    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setUsername(username);
    signUpRequest.setEmail(email);
    signUpRequest.setRole(RoleType.ROLE_USER);

    when(userRepository.existsByEmail(email)).thenReturn(true);

    ResponseEntity<?> responseEntity = userService.createUser(signUpRequest);

    assertEquals(HttpStatus.BAD_REQUEST.toString(),
        responseEntity.getStatusCode().toString(),
        "User find by email test failed");
  }

  @Test
  void createUserRoleNullTest() {
    String username = "test__harsh";
    String email = "test__harsh.bhatt@dal.ca";

    Role role = new Role();

    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setUsername(username);
    signUpRequest.setEmail(email);

    when(roleRepository.findByName(RoleType.ROLE_USER)).thenReturn(java.util.Optional.of(role));
    when(userRepository.existsByUsername(username)).thenReturn(false);
    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(userRepository.save(any(User.class))).then(returnsFirstArg());

    ResponseEntity<?> responseEntity = userService.createUser(signUpRequest);

    assertEquals(HttpStatus.CREATED.toString(),
        responseEntity.getStatusCode().toString(),
        "User without role test failed");
  }

  @Test
  void createUserWithBankRoleTest() {
    String username = "test__harsh";
    String email = "test__harsh.bhatt@dal.ca";

    Role role = new Role();

    SignUpRequest signUpRequest = new SignUpRequest();
    signUpRequest.setUsername(username);
    signUpRequest.setEmail(email);
    signUpRequest.setRole(RoleType.ROLE_EMPLOYEE);

    when(roleRepository.findByName(RoleType.ROLE_EMPLOYEE)).thenReturn(java.util.Optional.of(role));
    when(userRepository.existsByUsername(username)).thenReturn(false);
    when(userRepository.existsByEmail(email)).thenReturn(false);
    when(userRepository.save(any(User.class))).then(returnsFirstArg());

    ResponseEntity<?> responseEntity = userService.createUser(signUpRequest);

    assertEquals(HttpStatus.CREATED.toString(),
        responseEntity.getStatusCode().toString(),
        "User with admin role test failed");
  }

  @Test
  void setUserAccountStatusSuccessTest() throws MessagingException,
      UnsupportedEncodingException {
    String email = "harsh.bhatt@dal.ca";
    AccountStatus accountStatus = AccountStatus.PENDING;

    User user = new User();
    user.setEmail(email);
    user.setAccountStatus(accountStatus);

    when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));

    User fetchedUser = userService.getUserByEmail(email);

    when(userRepository.update(fetchedUser)).thenReturn(true);

    Boolean isUpdated = userService.setUserAccountStatus(email, accountStatus);

    assertTrue(isUpdated, "Account status is not updated");
  }

  @Test
  void setUserAccountStatusFailureTest() throws MessagingException,
      UnsupportedEncodingException {
    String email = "harsh.bhatt@dal.ca";
    AccountStatus accountStatus = AccountStatus.PENDING;

    User user = new User();
    user.setEmail(email);
    user.setAccountStatus(accountStatus);

    when(userRepository.findByEmail(email)).thenReturn(Optional.ofNullable(user));

    User fetchedUser = userService.getUserByEmail(email);

    when(userRepository.update(fetchedUser)).thenReturn(false);

    Boolean isUpdated = userService.setUserAccountStatus(email, accountStatus);

    assertFalse(isUpdated, "Account status mistakenly updated");
  }

  @Test
  void changePasswordTrueTest() {
    String oldPassword = "abc";
    String newPassword = "def";
    String email = "harsh.bhatt@dal.ca";

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);
    userPrincipal.setPassword("hash_password");
    userPrincipal.setEmail(email);

    User user = new User();
    user.setEmail(email);

    when(passwordEncoder.matches(oldPassword, userPrincipal.getPassword())).thenReturn(true);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    User fetchedUser = userService.getUserByEmail(email);

    when(userRepository.update(fetchedUser)).thenReturn(true);

    ApiResponse apiResponse = userService.changePassword(oldPassword,
        newPassword, userPrincipal);

    assertTrue(apiResponse.getSuccess(), "False was returned");
  }

  @Test
  void changePasswordFalseTest() {
    String oldPassword = "abc";
    String newPassword = "abc";
    String email = "harsh.bhatt@dal.ca";

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);
    userPrincipal.setPassword("hash_password");
    userPrincipal.setEmail(email);

    User user = new User();
    user.setEmail(email);

    when(passwordEncoder.matches(oldPassword, userPrincipal.getPassword())).thenReturn(false);

    ApiResponse apiResponse = userService.changePassword(oldPassword,
        newPassword, userPrincipal);

    assertFalse(apiResponse.getSuccess(), "True was returned");
  }

  @Test
  void changePasswordNotUpdatedTest() {
    String oldPassword = "abc";
    String newPassword = "def";
    String email = "harsh.bhatt@dal.ca";

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);
    userPrincipal.setPassword("hash_password");
    userPrincipal.setEmail(email);

    User user = new User();
    user.setEmail(email);

    when(passwordEncoder.matches(oldPassword, userPrincipal.getPassword())).thenReturn(true);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    User fetchedUser = userService.getUserByEmail(email);

    when(userRepository.update(fetchedUser)).thenReturn(false);

    ApiResponse apiResponse = userService.changePassword(oldPassword,
        newPassword, userPrincipal);

    assertFalse(apiResponse.getSuccess(), "False was returned");
  }

  @Test
  void resetPasswordTrueTest() {
    String newPassword = "abc";
    String confirmNewPassword = "abc";
    String email = "harsh.bhatt@dal.ca";

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);
    userPrincipal.setPassword("hash_password");
    userPrincipal.setEmail(email);

    User user = new User();
    user.setEmail(email);

    when(userRepository.update(user)).thenReturn(true);

    ResponseEntity<?> responseEntity = userService.resetPassword(newPassword,
        confirmNewPassword, user);

    assertEquals(HttpStatus.OK.toString(),
        responseEntity.getStatusCode().toString(),
        "Correct status code not received");
  }

  @Test
  void resetPasswordFalseTest() {
    String newPassword = "same";
    String confirmNewPassword = "same";
    String email = "harsh.bhatt@dal.ca";

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);
    userPrincipal.setPassword("hash_password");
    userPrincipal.setEmail(email);

    User user = new User();
    user.setEmail(email);

    when(userRepository.update(user)).thenReturn(false);

    ResponseEntity<?> responseEntity = userService.resetPassword(newPassword,
        confirmNewPassword, user);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
        responseEntity.getStatusCode().toString(),
        "Correct status code not received");
  }

  @Test
  void resetPasswordNotUpdatedTest() {
    String newPassword = "abcd";
    String confirmNewPassword = "abc";
    String email = "harsh.bhatt@dal.ca";

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);
    userPrincipal.setPassword("hash_password");
    userPrincipal.setEmail(email);

    User user = new User();
    user.setEmail(email);

    ResponseEntity<?> responseEntity = userService.resetPassword(newPassword,
        confirmNewPassword, user);

    assertEquals(HttpStatus.BAD_REQUEST.toString(),
        responseEntity.getStatusCode().toString(),
        "Correct status code not received");
  }

  @Test
  void updateResetForgotPasswordTokenTest() {
    String email = "harsh.bhatt@dal.ca";
    String token = "dummy";

    User user = new User();
    user.setForgotPasswordToken(token);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    userService.updateResetForgotPasswordToken(token, email);

    verify(userRepository, times(1)).findByEmail(any());
    verify(userRepository, times(1)).update(any());
  }

  @Test
  void getUserByEmailTest() {
    String email = "harsh.bhatt@dal.ca";

    User user = new User();
    user.setEmail(email);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

    User fetchedUser = userService.getUserByEmail(email);

    assertEquals(email, fetchedUser.getEmail(), "Wrong user was returned");
  }

  @Test
  void getUserByTokenTest() {
    String token = "dummy_token";

    User user = new User();
    user.setForgotPasswordToken(token);

    when(userRepository.findByForgotPasswordToken(token)).thenReturn(Optional.ofNullable(user));

    User fetchedUser = userService.getUserByToken(token);

    assertEquals(token, fetchedUser.getForgotPasswordToken(), "Wrong user was" +
        " returned");
  }

  @Test
  void getUserProfileByUsernameTest() {
    String username = "harsh";

    UserProfile userProfile = new UserProfile();
    userProfile.setUsername(username);

    User user = new User();
    user.setUsername(username);

    when(userRepository.findByUsername(username)).thenReturn(Optional.ofNullable(user));

    UserProfile fetchedUserProfile =
        userService.getUserProfileByUsername(username);

    assertEquals(username, fetchedUserProfile.getUsername(), "Wrong " +
        "user was returned");
  }

  @Test
  void updateUserProfileByUsernameTrueTest() {
    User user = new User();
    user.setFirstName("Test Name");
    user.setId(1L);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userRepository.update(any(User.class))).thenReturn(true);

    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
    updateProfileRequest.setFirstName("Test");
    updateProfileRequest.setLastName("Last");
    updateProfileRequest.setAddress("Demo Update");
    updateProfileRequest.setBirthday(new Date());
    updateProfileRequest.setCity("Demo City");
    updateProfileRequest.setState("Demo State");
    updateProfileRequest.setZipCode("987654");

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1L);

    Boolean updatedUser =
        userService.updateUserProfileByUsername(userPrincipal,
            updateProfileRequest);

    assertTrue(updatedUser, "False was returned");
  }

  @Test
  void updateUserProfileByUsernameFalseTest() {
    User user = new User();
    user.setFirstName("Test Name");
    user.setId(1000L);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userRepository.update(any(User.class))).thenReturn(false);

    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest();
    updateProfileRequest.setFirstName("Test");
    updateProfileRequest.setLastName("Last");
    updateProfileRequest.setAddress("Demo Update");
    updateProfileRequest.setBirthday(new Date());
    updateProfileRequest.setCity("Demo City");
    updateProfileRequest.setState("Demo State");
    updateProfileRequest.setZipCode("987654");

    UserPrincipal userPrincipal = new UserPrincipal();
    userPrincipal.setId(1000L);

    Boolean updatedUser =
        userService.updateUserProfileByUsername(userPrincipal,
            updateProfileRequest);

    assertFalse(updatedUser, "False was returned");
  }

}