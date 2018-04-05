package entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "userInHousehold")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "UserInHousehold.findAll", query = "SELECT u FROM UserInHousehold u"),
  @NamedQuery(name = "UserInHousehold.findById", query = "SELECT u FROM UserInHousehold u WHERE u.id = :id"),
  @NamedQuery(name = "UserInHousehold.findByUserId", query = "SELECT u FROM UserInHousehold u WHERE u.user = :user"),
  @NamedQuery(name = "UserInHousehold.findByHouseholdId", query = "SELECT u FROM UserInHousehold u WHERE u.household = :household")
})
public class UserInHousehold implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @JoinColumn(name = "user", referencedColumnName = "id") @ManyToOne
  private User user;
  @JoinColumn(name = "household", referencedColumnName = "id") @ManyToOne
  private Household household;
  //

  public UserInHousehold() {
  }

  public UserInHousehold(Integer id) {
    this.id = id;
  }

  public UserInHousehold(User user, Household household) {
    this.user = user;
    this.household = household;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Household getHousehold() {
    return household;
  }

  public void setHousehold(Household household) {
    this.household = household;
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
    if (!(object instanceof UserInHousehold)) {
      return false;
    }
    UserInHousehold other = (UserInHousehold) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.UserInHousehold[ id=" + id + " ]";
  }

}
