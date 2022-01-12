package asd.group2.bms.service;

import asd.group2.bms.model.user.AccountStatus;
import asd.group2.bms.model.user.User;
import asd.group2.bms.payload.request.SignUpRequest;
import asd.group2.bms.payload.request.UpdateProfileRequest;
import asd.group2.bms.payload.response.ApiResponse;
import asd.group2.bms.payload.response.UserProfile;
import asd.group2.bms.security.UserPrincipal;
import org.springframework.http.ResponseEntity;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface IUserService {

  Boolean isEmailAvailable(String email);

  Boolean isUsernameAvailable(String username);

  ResponseEntity<?> createUser(SignUpRequest signUpRequest);

  Boolean setUserAccountStatus(String email, AccountStatus accountStatus) throws MessagingException, UnsupportedEncodingException;

  ApiResponse changePassword(String oldPassword, String newPassword, UserPrincipal currentUser);

  ResponseEntity<?> resetPassword(String newPassword,
                                  String confirmNewPassword, User user);

  void updateResetForgotPasswordToken(String token, String email);

  User getUserByEmail(String email);

  User getUserByToken(String token);

  UserProfile getUserProfileByUsername(String username);

  Boolean updateUserProfileByUsername(UserPrincipal currentUser,
                                      UpdateProfileRequest updateProfileRequest);

}
