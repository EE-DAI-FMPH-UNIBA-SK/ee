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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Livia
 */
@Entity
@Table(name = "eventInCalendar")
@XmlRootElement
@NamedQueries({
  @NamedQuery(name = "EventInCalendar.findAll", query = "SELECT e FROM EventInCalendar e"),
  @NamedQuery(name = "EventInCalendar.findById", query = "SELECT e FROM EventInCalendar e WHERE e.id = :id")})
public class EventInCalendar implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Basic(optional = false) @Column(name = "id")
  private Integer id;
  @JoinColumn(name = "event", referencedColumnName = "id") @ManyToOne
  private Event event;
  @JoinColumn(name = "calendar", referencedColumnName = "id") @ManyToOne
  private Calendar calendar;
  //

  public EventInCalendar() {
  }

  public EventInCalendar(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Event getEvent() {
    return event;
  }

  public void setEvent(Event event) {
    this.event = event;
  }

  public Calendar getCalendar() {
    return calendar;
  }

  public void setCalendar(Calendar calendar) {
    this.calendar = calendar;
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
    if (!(object instanceof EventInCalendar)) {
      return false;
    }
    EventInCalendar other = (EventInCalendar) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "entity.EventInCalendar[ id=" + id + " ]";
  }

}
