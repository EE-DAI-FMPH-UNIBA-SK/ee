package entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "User.control", query = "SELECT u FROM User u WHERE u.name = :name and u.password = :password"),
  @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u"),
  @NamedQuery(name = "User.findById", query = "SELECT u FROM User u WHERE u.id = :id"),
  @NamedQuery(name = "User.findByName", query = "SELECT u FROM User u WHERE u.name = :name"),
  @NamedQuery(name = "User.findByEmail", query = "SELECT u FROM User u WHERE u.email = :email"),
  @NamedQuery(name = "User.findByPassword", query = "SELECT u FROM User u WHERE u.password = :password")})
public class User implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
  @Size(max = 30) @Column(name = "email")
  private String email;
  @Size(max = 20) @Column(name = "password")
  private String password;
  @OneToMany(mappedBy = "admin")
  private List<Household> householdCollection;
  @OneToMany(mappedBy = "user")
  private List<UserInHousehold> userInHouseholdCollection;
  //

  public User() {
  }

  public User(Integer id) {
    this.id = id;
  }

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @XmlTransient
  public List<Household> getHouseholdCollection() {
    return householdCollection;
  }

  public void setHouseholdCollection(List<Household> householdCollection) {
    this.householdCollection = householdCollection;
  }

  @XmlTransient
  public List<UserInHousehold> getUserInHouseholdCollection() {
    return userInHouseholdCollection;
  }

  public void setUserInHouseholdCollection(List<UserInHousehold> userInHouseholdCollection) {
    this.userInHouseholdCollection = userInHouseholdCollection;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof User)) {
      return false;
    }
    User other = (User) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.User[ id=" + id + " ]";
  }

}
