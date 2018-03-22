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
@Table(name = "products")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Products.findAll", query = "SELECT p FROM Products p"), @NamedQuery(name = "Products.findById", query = "SELECT p FROM Products p WHERE p.id = :id"), @NamedQuery(name = "Products.findByName", query = "SELECT p FROM Products p WHERE p.name = :name"), @NamedQuery(name = "Products.findByValue", query = "SELECT p FROM Products p WHERE p.value = :value"), @NamedQuery(name = "Products.findByCount", query = "SELECT p FROM Products p WHERE p.count = :count")})
public class Products implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @Column(name = "value")
  private Integer value;
  @Column(name = "count")
  private Integer count;
  @OneToMany(mappedBy = "productId")
  private Collection<Ingredients> ingredientsCollection;
  @OneToMany(mappedBy = "productId")
  private Collection<Items> itemsCollection;
  //

  public Products() {
  }

  public Products(Integer id) {
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

  public Integer getValue() {
    return value;
  }

  public void setValue(Integer value) {
    this.value = value;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @XmlTransient @JsonIgnore
  public Collection<Ingredients> getIngredientsCollection() {
    return ingredientsCollection;
  }

  public void setIngredientsCollection(Collection<Ingredients> ingredientsCollection) {
    this.ingredientsCollection = ingredientsCollection;
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
    if (!(object instanceof Products)) {
      return false;
    }
    Products other = (Products) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Products[ id=" + id + " ]";
  }

}
