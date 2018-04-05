package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.stream.Collectors;

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
@Table(name = "household")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Household.findAll", query = "SELECT h FROM Household h"),
  @NamedQuery(name = "Household.findById", query = "SELECT h FROM Household h WHERE h.id = :id"),
  @NamedQuery(name = "Household.findByAdmin", query = "SELECT h FROM Household h WHERE h.admin = :admin"),
  @NamedQuery(name = "Household.findByName", query = "SELECT h FROM Household h WHERE h.name = :name")})
public class Household implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @OneToMany(mappedBy = "household")
  private Collection<ShoppingList> shoppingListCollection;
  @JoinColumn(name = "admin", referencedColumnName = "id") @ManyToOne
  private User admin;
  @OneToMany(mappedBy = "household")
  private Collection<UserInHousehold> userInHouseholdCollection;
  //

  public Household() {
  }

  public Household(Integer id) {
    this.id = id;
  }

  public Household(String name, User user) {
    this.name = name;
    this.admin = user;
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

  @XmlTransient
  public Collection<ShoppingList> getShoppingListCollection() {
    return shoppingListCollection.stream().collect(Collectors.toList());
  }

  public void setShoppingListCollection(Collection<ShoppingList> shoppingListCollection) {
    this.shoppingListCollection = shoppingListCollection;
  }

  public User getAdmin() {
    return admin;
  }

  public void setAdmin(User admin) {
    this.admin = admin;
  }

  public boolean isAdmin(int userId) {
    return admin.getId() == userId;
  }

  public void addUser(UserInHousehold user) {
    userInHouseholdCollection.add(user);
  }

  public void deleteUser(UserInHousehold user) {
    userInHouseholdCollection.remove(user);
  }

  @XmlTransient
  public Collection<UserInHousehold> getUserInHouseholdCollection() {
    return userInHouseholdCollection.stream().collect(Collectors.toList());
  }

  public void setUserInHouseholdCollection(Collection<UserInHousehold> userInHouseholdCollection) {
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
    if (!(object instanceof Household)) {
      return false;
    }
    Household other = (Household) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.Household[ id=" + id + " ]";
  }

}
