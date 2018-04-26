package entity;

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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "item")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Item.findAll", query = "SELECT i FROM Item i"),
  @NamedQuery(name = "Item.findById", query = "SELECT i FROM Item i WHERE i.id = :id"),
  @NamedQuery(name = "Item.findByDone", query = "SELECT i FROM Item i WHERE i.done = :done")})
public class Item implements Serializable {
  @Basic(optional = false) @NotNull @Column(name = "count")
  private double count;
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Column(name = "done")
  private Boolean done;
  @JoinColumn(name = "product", referencedColumnName = "id") @ManyToOne
  private Product product;
  @JoinColumn(name = "shoppingList", referencedColumnName = "id") @ManyToOne
  private ShoppingList shoppingList;
  //

  public Item() {
  }

  public Item(Integer id) {
    this.id = id;
  }

  public Item(Product product, ShoppingList shoppingList, double count) {
    this.product = product;
    this.shoppingList = shoppingList;
    this.count = count;
    this.done = false;
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

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public ShoppingList getShoppingList() {
    return shoppingList;
  }

  public void setShoppingList(ShoppingList shoppingList) {
    this.shoppingList = shoppingList;
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
    if (!(object instanceof Item)) {
      return false;
    }
    Item other = (Item) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.Item[ id=" + id + " ]";
  }

  public double getCount() {
    return count;
  }

  public void setCount(double count) {
    this.count = count;
  }

}
