package asd.group2.bms.model.user;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * @description: This will create roles table in the database
 */
@Entity
@Table(name = "roles")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @NaturalId
  @Column(length = 60)
  private RoleType name;

  public Role() {
  }

  public Role(Long id, RoleType name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RoleType getName() {
    return name;
  }

  public void setName(RoleType name) {
    this.name = name;
  }

}
