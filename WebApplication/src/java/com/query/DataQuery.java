package com.query;

import com.entity.Calendars;
import com.entity.EventList;
import com.entity.Events;
import com.entity.Users;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author Livia
 */
public class DataQuery {
  //
  EntityManagerFactory emf;
  EntityManager em;
  private static DataQuery instance = null;

  public static DataQuery getInstance() {
    if (instance == null) {
      instance = new DataQuery();
    }
    return instance;
  }

  protected DataQuery() {
    emf = Persistence.createEntityManagerFactory("WebApplicationPU");
    em = emf.createEntityManager();
    em.getTransaction().begin();
  }

  public int loginControl(String name, String password) {
    try {
      Users u = em.createNamedQuery("Users.control", Users.class).setParameter("name", name).setParameter("password", password).getSingleResult();
      if (u != null) {
        return u.getId();
      }
      return 0;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }

  public List<Calendars> getCalendars() {
    try {
      TypedQuery<Calendars> q = em.createNamedQuery("Calendars.findAll", Calendars.class);
      q.setHint("javax.persistence.cache.storeMode", "REFRESH");
      return q.getResultList();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  public Calendars getCalendarById(int id) {
    try {
      TypedQuery<Calendars> q = em.createNamedQuery("Calendars.findById", Calendars.class);
      q.setHint("javax.persistence.cache.storeMode", "REFRESH");
      return q.setParameter("id", id).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Calendars addCalendar(Calendars calendar, boolean c) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.persist(calendar);
      et.commit();
      return calendar;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Calendars findCalendarsByName(String name) {
    try {
      if (name != null) {
        TypedQuery<Calendars> q = em.createNamedQuery("Calendars.findByName", Calendars.class);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return q.setParameter("name", name).getSingleResult();
      }
      return null;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Events addEvent(Events event) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.persist(event);
      et.commit();
      return event;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public EventList addEventList(EventList eventList) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.persist(eventList);
      et.commit();
      return eventList;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public Events findEventByName(String name) {
    try {
      if (name != null) {

        TypedQuery<Events> q = em.createNamedQuery("Events.findByName", Events.class);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return q.setParameter("name", name).getSingleResult();

      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public EventList getEventList(Calendars calendarId, Events eventId) {
    try {
      return em.createNamedQuery("EventList.findByEventCalendarId", EventList.class).setParameter("calendarId", calendarId).setParameter("eventId", eventId).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Users getUserById(int id) {
    try {
      return em.createNamedQuery("Users.findById", Users.class).setParameter("id", id).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void commit() {
    em.getTransaction().commit();
  }
}
