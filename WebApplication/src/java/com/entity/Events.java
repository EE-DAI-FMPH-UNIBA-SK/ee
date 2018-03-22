package com.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "events")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Events.findAll", query = "SELECT e FROM Events e"),
  @NamedQuery(name = "Events.findById", query = "SELECT e FROM Events e WHERE e.id = :id"),
  @NamedQuery(name = "Events.findByName", query = "SELECT e FROM Events e WHERE e.name = :name"),
  @NamedQuery(name = "Events.findByStart", query = "SELECT e FROM Events e WHERE e.start = :start"),
  @NamedQuery(name = "Events.findByLength", query = "SELECT e FROM Events e WHERE e.length = :length"),
  @NamedQuery(name = "Events.findByType", query = "SELECT e FROM Events e WHERE e.type = :type"),
  @NamedQuery(name = "Events.findByState", query = "SELECT e FROM Events e WHERE e.state = :state"),
  @NamedQuery(name = "Events.findByStartDate", query = "SELECT e FROM Events e WHERE e.startDate = :startDate"),
  @NamedQuery(name = "Events.findByIter", query = "SELECT e FROM Events e WHERE e.iter = :iter"),
  @NamedQuery(name = "Events.findByEndDate", query = "SELECT e FROM Events e WHERE e.endDate = :endDate")})
public class Events implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 100) @Column(name = "name")
  private String name;
  @Column(name = "start") @Temporal(TemporalType.TIME)
  private Date start;
  @Column(name = "length")
  private Integer length;
  @Column(name = "type")
  private Integer type;
  @Column(name = "state")
  private Integer state;
  @Column(name = "start_date") @Temporal(TemporalType.DATE)
  private Date startDate;
  @Size(max = 20) @Column(name = "iter")
  private String iter;
  @Column(name = "end_date") @Temporal(TemporalType.DATE)
  private Date endDate;
  @OneToMany(mappedBy = "eventId")
  private Collection<EventList> eventListCollection;
  //

  public Events() {
  }

  public Events(Integer id) {
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

  public Integer getLength() {
    return length;
  }

  public void setLength(Integer length) {
    this.length = length;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public String getIter() {
    return iter;
  }

  public void setIter(String iter) {
    this.iter = iter;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
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
    if (!(object instanceof Events)) {
      return false;
    }
    Events other = (Events) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Events[ id=" + id + " ]";
  }

}
