package entity;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "recipe")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Recipe.findAll", query = "SELECT r FROM Recipe r"),
  @NamedQuery(name = "Recipe.findById", query = "SELECT r FROM Recipe r WHERE r.id = :id"),
  @NamedQuery(name = "Recipe.findByName", query = "SELECT r FROM Recipe r WHERE r.name = :name"),
  @NamedQuery(name = "Recipe.findByCountPortions", query = "SELECT r FROM Recipe r WHERE r.countPortions = :countPortions"),
  @NamedQuery(name = "Recipe.findByDescription", query = "SELECT r FROM Recipe r WHERE r.description = :description")})
public class Recipe implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @Column(name = "count_portions")
  private Integer countPortions;
  @Size(max = 2000) @Column(name = "description")
  private String description;
  @JoinColumn(name = "household", referencedColumnName = "id") @ManyToOne
  private Household household;
  @OneToMany(mappedBy = "recipe")
  private List<Ingredient> ingredientList;
  //

  public Recipe() {
  }

  public Recipe(Integer id) {
    this.id = id;
  }

  public Recipe(String name, String description, int portion, Household household) {
    this.name = name;
    this.description = description;
    countPortions = portion;
    this.household = household;
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

  public Household getHousehold() {
    return household;
  }

  public void setHousehold(Household household) {
    this.household = household;
  }

  @XmlTransient
  public List<Ingredient> getIngredientList() {
    return ingredientList;
  }

  public void setIngredientList(List<Ingredient> ingredientList) {
    this.ingredientList = ingredientList;
  }

  public void addIngredient(Ingredient i) {
    ingredientList.add(i);
  }

  public void deleteIngredient(Ingredient i) {
    ingredientList.remove(i);
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
    if (!(object instanceof Recipe)) {
      return false;
    }
    Recipe other = (Recipe) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.Recipe[ id=" + id + " ]";
  }

}
