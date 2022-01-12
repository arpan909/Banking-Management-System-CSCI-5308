package asd.group2.bms.repository;

import asd.group2.bms.model.user.Role;
import asd.group2.bms.model.user.RoleType;

import java.util.Optional;

public interface IRoleRepository {

  /**
   * @param roleName: role of the user
   * @return This will return the role of the user
   */
  Optional<Role> findByName(RoleType roleName);

}
