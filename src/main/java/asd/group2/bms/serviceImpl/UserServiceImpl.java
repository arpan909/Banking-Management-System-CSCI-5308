package asd.group2.bms.serviceImpl;

import asd.group2.bms.exception.BMSException;
import asd.group2.bms.exception.ResourceNotFoundException;
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
import asd.group2.bms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Collections;

@Service
public class UserServiceImpl implements IUserService {

  @Autowired
  IUserRepository userRepository;

  @Autowired
  IRoleRepository roleRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  ICustomEmail customEmail;

  /**
   * @param email: email
   * @return true or false based on email availability
   */
  @Override
  public Boolean isEmailAvailable(String email) {
    return !userRepository.existsByEmail(email);
  }

  /**
   * @param username: username
   * @return true or false based on username availability
   */
  @Override
  public Boolean isUsernameAvailable(String username) {
    return !userRepository.existsByUsername(username);
  }

  /**
   * @param signUpRequest: Signup related data (username, email, name, phone etc.)
   * @return success or failure response with appropriate message
   */
  @Override
  public ResponseEntity<?> createUser(SignUpRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
          HttpStatus.BAD_REQUEST);
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return new ResponseEntity<>(new ApiResponse(false, "Email Address already in use!"),
          HttpStatus.BAD_REQUEST);
    }

    // Creating user's account
    User user = new User(
        signUpRequest.getFirstName(),
        signUpRequest.getLastName(),
        signUpRequest.getUsername(),
        signUpRequest.getEmail(),
        signUpRequest.getBirthday(),
        signUpRequest.getPhone(),
        signUpRequest.getPassword(),
        signUpRequest.getAddress(),
        signUpRequest.getCity(),
        signUpRequest.getState(),
        signUpRequest.getZipCode(),
        AccountStatus.PENDING,
        signUpRequest.getRequestedAccountType()
    );

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    Role userRole;

    RoleType role = signUpRequest.getRole();

    if (role == null) {
      userRole = roleRepository.findByName(RoleType.ROLE_USER)
          .orElseThrow(() -> new BMSException("User Role not set."));
    } else {
      if (role.equals(RoleType.ROLE_USER)) {
        user.setAccountStatus(AccountStatus.PENDING);
      } else {
        user.setAccountStatus(AccountStatus.ACTIVE);
      }
      userRole = roleRepository.findByName(role)
          .orElseThrow(() -> new BMSException("User Role not set."));
    }

    user.setRoles(Collections.singleton(userRole));

    User result = userRepository.save(user);

    URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}")
        .buildAndExpand(result.getUsername()).toUri();

    return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
  }

  /**
   * @param email:         email id of the user
   * @param accountStatus: ACTIVE, REJECTED, PENDING, CLOSED
   * @return the updated status of the user having email - email
   */
  @Override
  public Boolean setUserAccountStatus(String email, AccountStatus accountStatus) throws MessagingException, UnsupportedEncodingException {
    User user = getUserByEmail(email);
    user.setAccountStatus(accountStatus);
    customEmail.sendUserAccountStatusChangeMail(email, user.getFirstName(), accountStatus.toString());
    return userRepository.update(user);
  }

  /**
   * @param oldPassword: old password of the user account
   * @param newPassword: new password of the user account
   * @param currentUser: current logged in user
   * @return success or failure response with appropriate message
   */
  @Override
  public ApiResponse changePassword(String oldPassword, String newPassword, UserPrincipal currentUser) {
    if (passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
      User user = getUserByEmail(currentUser.getEmail());
      user.setPassword(passwordEncoder.encode(newPassword));

      Boolean isUpdated = userRepository.update(user);
      if (isUpdated) {
        return new ApiResponse(true, "Password changed successfully!");
      } else {
        return new ApiResponse(false, "Something went wrong while changing " +
            "password");
      }
    } else {
      return new ApiResponse(false, "Current password is wrong!");
    }
  }

  /**
   * @param newPassword:        new password to be replaced
   * @param confirmNewPassword: new password to be replaced
   * @param user:               User model object
   * @return success or failure response with appropriate message
   */
  @Override
  public ResponseEntity<?> resetPassword(String newPassword, String confirmNewPassword, User user) {
    if (newPassword.equals(confirmNewPassword)) {
      user.setPassword(passwordEncoder.encode(newPassword));
      user.setForgotPasswordToken(null);
      Boolean isUpdated = userRepository.update(user);
      if (isUpdated) {
        return ResponseEntity.ok(new ApiResponse(true, "Password reset successfully"));
      } else {
        return new ResponseEntity<>(new ApiResponse(false, "Something went " +
            "wrong while resetting password"), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return new ResponseEntity<>(new ApiResponse(false, "New passwords are not same"), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * @param token: Reset token received via email
   * @param email: email of the user
   */
  @Override
  public void updateResetForgotPasswordToken(String token, String email) {
    User user = getUserByEmail(email);
    user.setForgotPasswordToken(token);
    userRepository.update(user);
  }

  /**
   * Get the user by user email
   *
   * @param email: email of the user
   * @return a user based on email
   */
  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Email", "email", email));
  }

  /**
   * Get the user by user token
   *
   * @param token: token of the user
   * @return a user based on token
   */
  @Override
  public User getUserByToken(String token) {
    return userRepository.findByForgotPasswordToken(token).orElseThrow(() -> new ResourceNotFoundException("Reset Password Token", "token", token));
  }

  /**
   * Get the user profile by username
   *
   * @param username: username of the user
   * @return a user profile based on username
   */
  @Override
  public UserProfile getUserProfileByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

    return new UserProfile(
        user.getId(),
        user.getFirstName(),
        user.getLastName(),
        user.getUsername(),
        user.getBirthday(),
        user.getEmail(),
        user.getPhone(),
        user.getAddress(),
        user.getCity(),
        user.getState(),
        user.getZipCode(),
        user.getCreatedAt()
    );
  }

  /**
   * @param currentUser:          current logged in user
   * @param updateProfileRequest: required foields to update the data of the user
   * @return true or false based on update status
   */
  @Override
  public Boolean updateUserProfileByUsername(UserPrincipal currentUser, UpdateProfileRequest updateProfileRequest) {
    User user = userRepository.findById(currentUser.getId())
        .orElseThrow(() -> new ResourceNotFoundException("User", "username", currentUser.getUsername()));

    user.setFirstName(updateProfileRequest.getFirstName());
    user.setLastName(updateProfileRequest.getLastName());
    user.setBirthday(updateProfileRequest.getBirthday());
    user.setPhone(updateProfileRequest.getPhone());
    user.setAddress(updateProfileRequest.getAddress());
    user.setCity(updateProfileRequest.getCity());
    user.setState(updateProfileRequest.getState());
    user.setZipCode(updateProfileRequest.getZipCode());

    return userRepository.update(user);
  }

}
