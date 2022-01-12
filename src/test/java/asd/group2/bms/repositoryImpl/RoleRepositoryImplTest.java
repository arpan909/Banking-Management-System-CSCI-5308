package asd.group2.bms.repositoryImpl;

import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.RoleType;
import asd.group2.bms.repositoryMapper.RoleRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RoleRepositoryImplTest {

  @Mock
  private JdbcTemplate jdbcTemplate;

  @InjectMocks
  private RoleRepositoryImpl roleRepository;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ReflectionTestUtils.setField(roleRepository, "jdbcTemplate", jdbcTemplate);
  }

  @Test
  void findByNameFoundTest() {
    Role role = new Role(1L, RoleType.ROLE_USER);

    when(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(RoleRowMapper.class)))
        .thenReturn(role);

    Optional<Role> roleOptional =
        roleRepository.findByName(RoleType.ROLE_USER);

    assertEquals(RoleType.ROLE_USER, roleOptional.get().getName());
  }

  @Test
  void findByNameNotFoundTest() {
    when(Optional.ofNullable(jdbcTemplate.queryForObject(
        ArgumentMatchers.anyString(),
        ArgumentMatchers.any(),
        ArgumentMatchers.any(RoleRowMapper.class))))
        .thenThrow(new EmptyResultDataAccessException(0));

    Optional<Role> roleOptional =
        roleRepository.findByName(RoleType.ROLE_USER);

    assertEquals(Optional.empty(), roleOptional);
  }

  @Test
  void findRoleByUserIdTest() {
    Long userId = 1L;
    Role role = new Role(1L, RoleType.ROLE_USER);

    List<Role> roles = new ArrayList<>();
    roles.add(role);

    when(jdbcTemplate.query(
        ArgumentMatchers.anyString(),
        (Object[]) ArgumentMatchers.any(),
        ArgumentMatchers.any(RoleRowMapper.class)))
        .thenReturn(roles);

    List<Role> roleList =
        roleRepository.findRoleByUserId(userId);

    assertEquals(RoleType.ROLE_USER, roleList.get(0).getName());
  }

}