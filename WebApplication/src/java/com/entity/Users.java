package com.entity;

import java.io.Serializable;
import java.util.Collection;

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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "users")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Users.control", query = "SELECT u FROM Users u WHERE u.name = :name and u.password = :password"),
  @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
  @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
  @NamedQuery(name = "Users.findByName", query = "SELECT u FROM Users u WHERE u.name = :name"),
  @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email"),
  @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")
})
public class Users implements Serializable {
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
  @OneToMany(mappedBy = "userId")
  private Collection<HouseholdUsers> householdUsersCollection;
  @OneToMany(mappedBy = "userId")
  private Collection<Calendars> calendarsCollection;
  @OneToMany(mappedBy = "userId")
  private Collection<ShoppingLists> shoppingListsCollection;
  @OneToMany(mappedBy = "adminId")
  private Collection<Households> householdsCollection;
  //

  public Users() {
  }

  public Users(Integer id) {
    this.id = id;
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

  @XmlTransient @JsonIgnore
  public Collection<HouseholdUsers> getHouseholdUsersCollection() {
    return householdUsersCollection;
  }

  public void setHouseholdUsersCollection(Collection<HouseholdUsers> householdUsersCollection) {
    this.householdUsersCollection = householdUsersCollection;
  }

  @XmlTransient @JsonIgnore
  public Collection<Calendars> getCalendarsCollection() {
    return calendarsCollection;
  }

  public void setCalendarsCollection(Collection<Calendars> calendarsCollection) {
    this.calendarsCollection = calendarsCollection;
  }

  @XmlTransient @JsonIgnore
  public Collection<ShoppingLists> getShoppingListsCollection() {
    return shoppingListsCollection;
  }

  public void setShoppingListsCollection(Collection<ShoppingLists> shoppingListsCollection) {
    this.shoppingListsCollection = shoppingListsCollection;
  }

  @XmlTransient @JsonIgnore
  public Collection<Households> getHouseholdsCollection() {
    return householdsCollection;
  }

  public void setHouseholdsCollection(Collection<Households> householdsCollection) {
    this.householdsCollection = householdsCollection;
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
    if (!(object instanceof Users)) {
      return false;
    }
    Users other = (Users) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Users[ id=" + id + " ]";
  }

}
