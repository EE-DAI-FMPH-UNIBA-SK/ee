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
@Table(name = "ingredients")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Ingredients.findAll", query = "SELECT i FROM Ingredients i"), @NamedQuery(name = "Ingredients.findById", query = "SELECT i FROM Ingredients i WHERE i.id = :id")})
public class Ingredients implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @JoinColumn(name = "recipe_id", referencedColumnName = "id") @ManyToOne
  private Recipes recipeId;
  @JoinColumn(name = "product_id", referencedColumnName = "id") @ManyToOne
  private Products productId;
  //

  public Ingredients() {
  }

  public Ingredients(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Recipes getRecipeId() {
    return recipeId;
  }

  public void setRecipeId(Recipes recipeId) {
    this.recipeId = recipeId;
  }

  public Products getProductId() {
    return productId;
  }

  public void setProductId(Products productId) {
    this.productId = productId;
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
    if (!(object instanceof Ingredients)) {
      return false;
    }
    Ingredients other = (Ingredients) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Ingredients[ id=" + id + " ]";
  }

}
