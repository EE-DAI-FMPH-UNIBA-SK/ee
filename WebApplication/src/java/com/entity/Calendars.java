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
@Table(name = "calendars")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Calendars.findAll", query = "SELECT c FROM Calendars c"),
  @NamedQuery(name = "Calendars.findById", query = "SELECT c FROM Calendars c WHERE c.id = :id"),
  @NamedQuery(name = "Calendars.findByName", query = "SELECT c FROM Calendars c WHERE c.name = :name"),
  @NamedQuery(name = "Calendars.findByVisible", query = "SELECT c FROM Calendars c WHERE c.visible = :visible")
})
public class Calendars implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Column(name = "visible")
  private Boolean visible;
  @Size(max = 100) @Column(name = "name")
  private String name;
  @JoinColumn(name = "user_id", referencedColumnName = "id") @ManyToOne
  private Users userId;
  @OneToMany(mappedBy = "calendarId")
  private Collection<EventList> eventListCollection;
  //

  public Calendars() {
  }

  public Calendars(Integer id) {
    this.id = id;
  }

  public Calendars(String name, boolean visible) {
    this.name = name;
    this.visible = visible;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Boolean getVisible() {
    return visible;
  }

  public void setVisible(Boolean visible) {
    this.visible = visible;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Users getUserId() {
    return userId;
  }

  public void setUserId(Users userId) {
    this.userId = userId;
  }

  @XmlTransient @JsonIgnore
  public Collection<EventList> getEventListCollection() {
    return eventListCollection;
  }

  public void setEventListCollection(Collection<EventList> eventListCollection) {
    this.eventListCollection = eventListCollection;
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
    if (!(object instanceof Calendars)) {
      return false;
    }
    Calendars other = (Calendars) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Calendars[ id=" + id + " ]";
  }

}
