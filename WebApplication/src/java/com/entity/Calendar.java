package com.entity;

import java.io.Serializable;
import java.util.Collection;

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

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "calendar")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c"),
  @NamedQuery(name = "Calendar.findById", query = "SELECT c FROM Calendar c WHERE c.id = :id"),
  @NamedQuery(name = "Calendar.findByName", query = "SELECT c FROM Calendar c WHERE c.name = :name"),
  @NamedQuery(name = "Calendar.findByVisible", query = "SELECT c FROM Calendar c WHERE c.visible = :visible")})
public class Calendar implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 100) @Column(name = "name")
  private String name;
  @Column(name = "visible")
  private Boolean visible;
  @JoinColumn(name = "user", referencedColumnName = "id") @ManyToOne
  private User user;
  @OneToMany(mappedBy = "calendar")
  private Collection<Eventincalendar> eventincalendarCollection;
  //

  public Calendar() {
  }

  public Calendar(Integer id) {
    this.id = id;
  }

  public Calendar(String name, boolean visible) {
    this.name = name;
    this.visible = visible;
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

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @XmlTransient @JsonIgnore
  public Collection<Eventincalendar> getEventincalendarCollection() {
    return eventincalendarCollection;
  }

  public void setEventincalendarCollection(Collection<Eventincalendar> eventincalendarCollection) {
    this.eventincalendarCollection = eventincalendarCollection;
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
    if (!(object instanceof Calendar)) {
      return false;
    }
    Calendar other = (Calendar) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Calendar[ id=" + id + " ]";
  }

}
