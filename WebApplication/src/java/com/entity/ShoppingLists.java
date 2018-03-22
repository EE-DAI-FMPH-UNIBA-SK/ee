package com.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "shopping_lists")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "ShoppingLists.findAll", query = "SELECT s FROM ShoppingLists s"), @NamedQuery(name = "ShoppingLists.findById", query = "SELECT s FROM ShoppingLists s WHERE s.id = :id"), @NamedQuery(name = "ShoppingLists.findByName", query = "SELECT s FROM ShoppingLists s WHERE s.name = :name"), @NamedQuery(name = "ShoppingLists.findByStart", query = "SELECT s FROM ShoppingLists s WHERE s.start = :start"), @NamedQuery(name = "ShoppingLists.findByFinish", query = "SELECT s FROM ShoppingLists s WHERE s.finish = :finish")})
public class ShoppingLists implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @Column(name = "start") @Temporal(TemporalType.DATE)
  private Date start;
  @Column(name = "finish") @Temporal(TemporalType.DATE)
  private Date finish;
  @JoinColumn(name = "household_id", referencedColumnName = "id") @ManyToOne
  private Households householdId;
  @JoinColumn(name = "user_id", referencedColumnName = "id") @ManyToOne
  private Users userId;
  @OneToMany(mappedBy = "shoppingListId")
  private Collection<Items> itemsCollection;
  //

  public ShoppingLists() {
  }

  public ShoppingLists(Integer id) {
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

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getFinish() {
    return finish;
  }

  public void setFinish(Date finish) {
    this.finish = finish;
  }

  public Households getHouseholdId() {
    return householdId;
  }

  public void setHouseholdId(Households householdId) {
    this.householdId = householdId;
  }

  public Users getUserId() {
    return userId;
  }

  public void setUserId(Users userId) {
    this.userId = userId;
  }

  @XmlTransient @JsonIgnore
  public Collection<Items> getItemsCollection() {
    return itemsCollection;
  }

  public void setItemsCollection(Collection<Items> itemsCollection) {
    this.itemsCollection = itemsCollection;
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
    if (!(object instanceof ShoppingLists)) {
      return false;
    }
    ShoppingLists other = (ShoppingLists) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.ShoppingLists[ id=" + id + " ]";
  }

}
