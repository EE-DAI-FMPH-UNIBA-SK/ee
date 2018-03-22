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
@Table(name = "recipes")
@XmlRootElement
@NamedQueries({@NamedQuery(name = "Recipes.findAll", query = "SELECT r FROM Recipes r"), @NamedQuery(name = "Recipes.findById", query = "SELECT r FROM Recipes r WHERE r.id = :id"), @NamedQuery(name = "Recipes.findByName", query = "SELECT r FROM Recipes r WHERE r.name = :name"), @NamedQuery(name = "Recipes.findByCountPortions", query = "SELECT r FROM Recipes r WHERE r.countPortions = :countPortions"), @NamedQuery(name = "Recipes.findByDescription", query = "SELECT r FROM Recipes r WHERE r.description = :description")})
public class Recipes implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @Column(name = "count_portions")
  private Integer countPortions;
  @Size(max = 20) @Column(name = "description")
  private String description;
  @OneToMany(mappedBy = "recipeId")
  private Collection<Ingredients> ingredientsCollection;
  //

  public Recipes() {
  }

  public Recipes(Integer id) {
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

  public Integer getCountPortions() {
    return countPortions;
  }

  public void setCountPortions(Integer countPortions) {
    this.countPortions = countPortions;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @XmlTransient @JsonIgnore
  public Collection<Ingredients> getIngredientsCollection() {
    return ingredientsCollection;
  }

  public void setIngredientsCollection(Collection<Ingredients> ingredientsCollection) {
    this.ingredientsCollection = ingredientsCollection;
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
    if (!(object instanceof Recipes)) {
      return false;
    }
    Recipes other = (Recipes) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Recipes[ id=" + id + " ]";
  }

}
