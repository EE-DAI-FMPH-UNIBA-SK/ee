package entity;

import java.io.Serializable;
import java.util.List;

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

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "product")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p"),
  @NamedQuery(name = "Product.findById", query = "SELECT p FROM Product p WHERE p.id = :id"),
  @NamedQuery(name = "Product.findByName", query = "SELECT p FROM Product p WHERE p.name = :name"),
  @NamedQuery(name = "Product.findByValue", query = "SELECT p FROM Product p WHERE p.value = :value"),
  @NamedQuery(name = "Product.findByCount", query = "SELECT p FROM Product p WHERE p.count = :count")})
public class Product implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @Column(name = "value")
  private Integer value;
  @Column(name = "count")
  private Integer count;
  @OneToMany(mappedBy = "product")
  private List<Item> itemCollection;
  @OneToMany(mappedBy = "product")
  private List<Ingredient> ingredientCollection;
  //

  public Product() {
  }

  public Product(Integer id) {
    this.id = id;
  }

  public Product(String name, int objem, int count) {
    this.name = name;
    this.value = objem;
    this.count = count;
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

  @XmlTransient
  public List<Item> getItemCollection() {
    return itemCollection;
  }

  public void setItemCollection(List<Item> itemCollection) {
    this.itemCollection = itemCollection;
  }

  @XmlTransient
  public List<Ingredient> getIngredientCollection() {
    return ingredientCollection;
  }

  public void setIngredientCollection(List<Ingredient> ingredientCollection) {
    this.ingredientCollection = ingredientCollection;
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
    if (!(object instanceof Product)) {
      return false;
    }
    Product other = (Product) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.Product[ id=" + id + " ]";
  }

}
