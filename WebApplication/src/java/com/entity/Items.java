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
@Table(name = "items")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Items.findAll", query = "SELECT i FROM Items i"), @NamedQuery(name = "Items.findById", query = "SELECT i FROM Items i WHERE i.id = :id"), @NamedQuery(name = "Items.findByDone", query = "SELECT i FROM Items i WHERE i.done = :done")})
public class Items implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Column(name = "done")
  private Boolean done;
  @JoinColumn(name = "product_id", referencedColumnName = "id") @ManyToOne
  private Products productId;
  @JoinColumn(name = "shopping_list_id", referencedColumnName = "id") @ManyToOne
  private ShoppingLists shoppingListId;
  //

  public Items() {
  }

  public Items(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Boolean getDone() {
    return done;
  }

  public void setDone(Boolean done) {
    this.done = done;
  }

  public Products getProductId() {
    return productId;
  }

  public void setProductId(Products productId) {
    this.productId = productId;
  }

  public ShoppingLists getShoppingListId() {
    return shoppingListId;
  }

  public void setShoppingListId(ShoppingLists shoppingListId) {
    this.shoppingListId = shoppingListId;
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
    if (!(object instanceof Items)) {
      return false;
    }
    Items other = (Items) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Items[ id=" + id + " ]";
  }

}
