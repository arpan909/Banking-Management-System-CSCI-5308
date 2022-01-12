package asd.group2.bms.security;

import asd.group2.bms.model.user.User;
import asd.group2.bms.repositoryImpl.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  UserRepositoryImpl userRepository;

  /**
   * @param usernameOrEmail: username or email of the user
   * @return This will return user details.
   * @throws UsernameNotFoundException: return UsernameNotFoundException error
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    // Let people login with either username or email
    User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
        .orElseThrow(() -> new UsernameNotFoundException(
            "User not found with username or email : " + usernameOrEmail));

    return UserPrincipal.create(user);
  }

  /**
   * @param id: user id
   * @return This method is used by JWTAuthenticationFilter.
   */
  @Transactional
  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UsernameNotFoundException("User not found with id : " + id));

    return UserPrincipal.create(user);
  }

}
