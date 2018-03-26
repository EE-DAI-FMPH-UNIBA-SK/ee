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
@Table(name = "event")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e"),
  @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id"),
  @NamedQuery(name = "Event.findByName", query = "SELECT e FROM Event e WHERE e.name = :name"),
  @NamedQuery(name = "Event.findByStart", query = "SELECT e FROM Event e WHERE e.start = :start"),
  @NamedQuery(name = "Event.findByLength", query = "SELECT e FROM Event e WHERE e.length = :length"),
  @NamedQuery(name = "Event.findByType", query = "SELECT e FROM Event e WHERE e.type = :type"),
  @NamedQuery(name = "Event.findByState", query = "SELECT e FROM Event e WHERE e.state = :state"),
  @NamedQuery(name = "Event.findByStartDate", query = "SELECT e FROM Event e WHERE e.startDate = :startDate"),
  @NamedQuery(name = "Event.findByEndDate", query = "SELECT e FROM Event e WHERE e.endDate = :endDate"),
  @NamedQuery(name = "Event.findByIter", query = "SELECT e FROM Event e WHERE e.iter = :iter")})
public class Event implements Serializable {
  @Size(max = 20) @Column(name = "type")
  private String type;
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @Size(max = 100) @Column(name = "name")
  private String name;
  @Column(name = "start") @Temporal(TemporalType.TIME)
  private Date start;
  @Column(name = "length")
  private Integer length;
  @Column(name = "state")
  private Integer state;
  @Column(name = "start_date") @Temporal(TemporalType.DATE)
  private Date startDate;
  @Column(name = "end_date") @Temporal(TemporalType.DATE)
  private Date endDate;
  @Size(max = 20) @Column(name = "iter")
  private String iter;
  @OneToMany(mappedBy = "event")
  private Collection<Eventincalendar> eventincalendarCollection;
  //

  public Event() {
  }

  public Event(Integer id) {
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

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getIter() {
    return iter;
  }

  public void setIter(String iter) {
    this.iter = iter;
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
    if (!(object instanceof Event)) {
      return false;
    }
    Event other = (Event) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.Event[ id=" + id + " ]";
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
