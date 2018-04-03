package com.dataquery;

import com.entity.Household;
import com.entity.User;
import com.entity.Userinhousehold;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    emf = Persistence.createEntityManagerFactory("DomacnostPU");
    em = emf.createEntityManager();
    em.getTransaction().begin();
  }

  public int loginControl(String name, String password) {
    try {
      User u = em.createNamedQuery("User.control", User.class).setParameter("name", name).setParameter("password", password).getSingleResult();
      System.out.println(u);
      if (u != null) {
        return u.getId();
      }
      return 0;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return 0;
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

  public List<Household> getHousholdersByUser(int userId) {
    try {
      Set<Household> result = new HashSet<>();
      User u = em.createNamedQuery("User.findById", User.class).setParameter("id", userId).getSingleResult();
      TypedQuery<Household> q = em.createNamedQuery("Household.findByAdmin", Household.class).setParameter("admin", u);
      q.setHint("javax.persistence.cache.storeMode", "REFRESH");
      result.addAll(q.getResultList());
      TypedQuery<Userinhousehold> q1 = em.createNamedQuery("Userinhousehold.findByUserId", Userinhousehold.class).setParameter("user", u);
      q1.setHint("javax.persistence.cache.storeMode", "REFRESH");
      List<Userinhousehold> usersInHouseholder = q1.getResultList();
      if (!usersInHouseholder.isEmpty()) {
        result.addAll(usersInHouseholder.stream().map(Userinhousehold::getHousehold).collect(Collectors.toSet()));
      }
      return result.stream().collect(Collectors.toList());
    } catch (Exception e) {
      System.out.println("---------------------");
      System.out.println(e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  public Household addHousehold(Household household) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.persist(household);
      et.commit();
      return household;
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public boolean deleteHousehold(Household household) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.remove(household);
      et.commit();
      return true;
    } catch (Exception e) {
      System.err.println("*************");
      System.out.println(e.getMessage());
      return false;
    }
  }

  public boolean deleteUserFromHousehold(Userinhousehold householdUser) {
    try {
      EntityTransaction et = em.getTransaction();
      if (!et.isActive()) {
        et.begin();
      }
      em.remove(householdUser);
      et.commit();
      return true;
    } catch (Exception e) {
      System.err.println("*************");
      System.out.println(e.getMessage());
      return false;
    }
  }

  public List<Userinhousehold> getUserInHouseholder(int householdId) {
    try {
      TypedQuery<Userinhousehold> q = em.createNamedQuery("Userinhousehold.findByHouseholdId", Userinhousehold.class).setParameter("household", householdId);
      q.setHint("javax.persistence.cache.storeMode", "REFRESH");
      return q.getResultList();
    } catch (Exception e) {
      System.err.println("*************");
      System.out.println(e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  public void commit() {
    em.getTransaction().commit();
  }
}
