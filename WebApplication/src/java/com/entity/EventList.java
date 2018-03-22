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
@Table(name = "event_list")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "EventList.findAll", query = "SELECT e FROM EventList e"),
  @NamedQuery(name = "EventList.findById", query = "SELECT e FROM EventList e WHERE e.id = :id"),
  @NamedQuery(name = "EventList.findByCalendarId", query = "SELECT e FROM EventList e WHERE e.calendarId = :calendarId"),
  @NamedQuery(name = "EventList.findByEventId", query = "SELECT e FROM EventList e WHERE e.eventId = :eventId"),
  @NamedQuery(name = "EventList.findByEventCalendarId", query = "SELECT e FROM EventList e WHERE e.eventId = :eventId and e.calendarId = :calendarId")
})
public class EventList implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @JoinColumn(name = "event_id", referencedColumnName = "id") @ManyToOne
  private Events eventId;
  @JoinColumn(name = "calendar_id", referencedColumnName = "id") @ManyToOne
  private Calendars calendarId;
  //

  public EventList() {
  }

  public EventList(Integer id) {
    this.id = id;
  }

  public EventList(Calendars calendarId, Events eventId) {
    this.eventId = eventId;
    this.calendarId = calendarId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Events getEventId() {
    return eventId;
  }

  public void setEventId(Events eventId) {
    this.eventId = eventId;
  }

  public Calendars getCalendarId() {
    return calendarId;
  }

  public void setCalendarId(Calendars calendarId) {
    this.calendarId = calendarId;
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
    if (!(object instanceof EventList)) {
      return false;
    }
    EventList other = (EventList) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "com.entity.EventList[ id=" + id + " ]";
  }

}
