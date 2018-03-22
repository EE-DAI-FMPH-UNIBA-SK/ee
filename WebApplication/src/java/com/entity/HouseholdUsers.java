package com.entity;

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
@Table(name = "household_users")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "HouseholdUsers.findAll", query = "SELECT h FROM HouseholdUsers h"), @NamedQuery(name = "HouseholdUsers.findById", query = "SELECT h FROM HouseholdUsers h WHERE h.id = :id")})
public class HouseholdUsers implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @JoinColumn(name = "user_id", referencedColumnName = "id") @ManyToOne
  private Users userId;
  @JoinColumn(name = "household_id", referencedColumnName = "id") @ManyToOne
  private Households householdId;
  //

  public HouseholdUsers() {
  }

  public HouseholdUsers(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Users getUserId() {
    return userId;
  }

  public void setUserId(Users userId) {
    this.userId = userId;
  }

  public Households getHouseholdId() {
    return householdId;
  }

  public void setHouseholdId(Households householdId) {
    this.householdId = householdId;
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
    if (!(object instanceof HouseholdUsers)) {
      return false;
    }
    HouseholdUsers other = (HouseholdUsers) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.HouseholdUsers[ id=" + id + " ]";
  }

}
