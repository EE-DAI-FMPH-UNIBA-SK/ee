package com.entity;

import java.io.Serializable;
import java.util.Collection;

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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "households")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Households.findAll", query = "SELECT h FROM Households h"), @NamedQuery(name = "Households.findById", query = "SELECT h FROM Households h WHERE h.id = :id"), @NamedQuery(name = "Households.findByName", query = "SELECT h FROM Households h WHERE h.name = :name")})
public class Households implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @OneToMany(mappedBy = "householdId")
  private Collection<HouseholdUsers> householdUsersCollection;
  @OneToMany(mappedBy = "householdId")
  private Collection<ShoppingLists> shoppingListsCollection;
  @JoinColumn(name = "admin_id", referencedColumnName = "id") @ManyToOne
  private Users adminId;
  //

  public Households() {
  }

  public Households(Integer id) {
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

  @XmlTransient @JsonIgnore
  public Collection<HouseholdUsers> getHouseholdUsersCollection() {
    return householdUsersCollection;
  }

  public void setHouseholdUsersCollection(Collection<HouseholdUsers> householdUsersCollection) {
    this.householdUsersCollection = householdUsersCollection;
  }

  @XmlTransient @JsonIgnore
  public Collection<ShoppingLists> getShoppingListsCollection() {
    return shoppingListsCollection;
  }

  public void setShoppingListsCollection(Collection<ShoppingLists> shoppingListsCollection) {
    this.shoppingListsCollection = shoppingListsCollection;
  }

  public Users getAdminId() {
    return adminId;
  }

  public void setAdminId(Users adminId) {
    this.adminId = adminId;
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
    if (!(object instanceof Households)) {
      return false;
    }
    Households other = (Households) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Households[ id=" + id + " ]";
  }

}
