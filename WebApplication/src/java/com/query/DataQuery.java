package com.query;

import com.entity.Calendar;
import com.entity.Event;
import com.entity.EventInCalendar;
import com.entity.User;

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
      User u = em.createNamedQuery("User.control", User.class).setParameter("name", name).setParameter("password", password).getSingleResult();
      if (u != null) {
        return u.getId();
      }
      return 0;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
    }
  }

  public List<Calendar> getCalendars() {
    try {
      TypedQuery<Calendar> q = em.createNamedQuery("Calendar.findAll", Calendar.class);
      q.setHint("javax.persistence.cache.storeMode", "REFRESH");
      return q.getResultList();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  public Calendar getCalendarById(int id) {
    try {
      TypedQuery<Calendar> q = em.createNamedQuery("Calendar.findById", Calendar.class);
      q.setHint("javax.persistence.cache.storeMode", "REFRESH");
      return q.setParameter("id", id).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Calendar addCalendar(Calendar calendar, boolean c) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.persist(calendar);
      et.commit();
      calendar.getUser().addCalendar(calendar);
      return calendar;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Calendar findCalendarsByName(String name) {
    try {
      if (name != null) {
        TypedQuery<Calendar> q = em.createNamedQuery("Calendar.findByName", Calendar.class);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return q.setParameter("name", name).getSingleResult();
      }
      return null;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public Event addEvent(Event event) {
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

  public EventInCalendar addCalendarEvent(EventInCalendar eventList) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.persist(eventList);
      et.commit();
      eventList.getCalendar().addEventInCalendar(eventList);
      return eventList;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  public Event findEventByName(String name) {
    try {
      if (name != null) {

        TypedQuery<Event> q = em.createNamedQuery("Event.findByName", Event.class);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        return q.setParameter("name", name).getSingleResult();

      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  public Event getEventById(int id) {
    try {
      return em.createNamedQuery("Event.findById", Event.class).setParameter("id", id).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public EventInCalendar getEventList(Calendar calendarId, Event eventId) {
    try {
      return em.createNamedQuery("Eventincalendar.findByEventCalendarId", EventInCalendar.class).setParameter("calendarId", calendarId).setParameter("eventId", eventId).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public User getUserById(int id) {
    try {
      return em.createNamedQuery("User.findById", User.class).setParameter("id", id).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void commit() {
    em.getTransaction().commit();
  }
}
