package entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

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

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "shoppingList")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "ShoppingList.findAll", query = "SELECT s FROM ShoppingList s"),
  @NamedQuery(name = "ShoppingList.findById", query = "SELECT s FROM ShoppingList s WHERE s.id = :id"),
  @NamedQuery(name = "ShoppingList.findByName", query = "SELECT s FROM ShoppingList s WHERE s.name = :name"),
  @NamedQuery(name = "ShoppingList.findByStart", query = "SELECT s FROM ShoppingList s WHERE s.start = :start"),
  @NamedQuery(name = "ShoppingList.findByFinish", query = "SELECT s FROM ShoppingList s WHERE s.finish = :finish")})
public class ShoppingList implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 20) @Column(name = "name")
  private String name;
  @Column(name = "start") @Temporal(TemporalType.DATE)
  private Date start;
  @Column(name = "finish") @Temporal(TemporalType.DATE)
  private Date finish;
  @OneToMany(mappedBy = "shoppingList")
  private Collection<Item> itemCollection;
  @JoinColumn(name = "household", referencedColumnName = "id") @ManyToOne
  private Household household;
  @JoinColumn(name = "user", referencedColumnName = "id") @ManyToOne
  private User user;
  //

  public ShoppingList() {
  }

  public ShoppingList(Integer id) {
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

  @XmlTransient
  public Collection<Item> getItemCollection() {
    return itemCollection.stream().collect(Collectors.toList());
  }

  public void setItemCollection(Collection<Item> itemCollection) {
    this.itemCollection = itemCollection;
  }

  public Household getHousehold() {
    return household;
  }

  public void setHousehold(Household household) {
    this.household = household;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void removeItem(Item item) {
    itemCollection.remove(item);
  }

  public void addItem(Item item) {
    itemCollection.add(item);
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
    if (!(object instanceof ShoppingList)) {
      return false;
    }
    ShoppingList other = (ShoppingList) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.ShoppingList[ id=" + id + " ]";
  }

}
