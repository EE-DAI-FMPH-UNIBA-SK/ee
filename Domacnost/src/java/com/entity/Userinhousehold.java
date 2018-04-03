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
@Table(name = "userInHousehold")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Userinhousehold.findAll", query = "SELECT u FROM Userinhousehold u"),
  @NamedQuery(name = "Userinhousehold.findById", query = "SELECT u FROM Userinhousehold u WHERE u.id = :id"),
  @NamedQuery(name = "Userinhousehold.findByUserId", query = "SELECT u FROM Userinhousehold u WHERE u.user = :user"),
  @NamedQuery(name = "Userinhousehold.findByHouseholdId", query = "SELECT u FROM Userinhousehold u WHERE u.household = :household")
})
public class Userinhousehold implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @JoinColumn(name = "user", referencedColumnName = "id") @ManyToOne
  private User user;
  @JoinColumn(name = "household", referencedColumnName = "id") @ManyToOne
  private Household household;
  //

  public Userinhousehold() {
  }

  public Userinhousehold(Integer id) {
    this.id = id;
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
    if (!(object instanceof Userinhousehold)) {
      return false;
    }
    Userinhousehold other = (Userinhousehold) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Userinhousehold[ id=" + id + " ]";
  }

}
