package dataQuery;

import entity.Household;
import entity.Item;
import entity.Product;
import entity.ShoppingList;
import entity.User;
import entity.UserInHousehold;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Livia
 */
public class DataQuery {
  //
  private EntityManager em;
  private static DataQuery instance = null;

  public static DataQuery getInstance() {
    if (instance == null) {
      instance = new DataQuery();
    }
    return instance;
  }

  public DataQuery() {
    em = Persistence.createEntityManagerFactory("DomacnostPU").createEntityManager();
  }

  public void edit(Object entity) {
    em.merge(entity);
  }

  public int loginControl(String name, String password) {
    try {
      em.createNamedQuery("User.findById", User.class).setParameter("id", 1);
      User u = em.createNamedQuery("User.control", User.class).setParameter("name", name).setParameter("password", password).getSingleResult();
      System.err.println(u);
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

  public User getUserByEmail(String email) {
    try {
      return em.createNamedQuery("User.findByEmail", User.class).setParameter("email", email).getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public List<Household> getHousholdersByUser(int userId) {
    try {
      Set<Household> result = new HashSet<>();
      User u = em.createNamedQuery("User.findById", User.class).setParameter("id", userId).getSingleResult();
      result.addAll(em.createNamedQuery("Household.findByAdmin", Household.class).setParameter("admin", u).getResultList());
      List<UserInHousehold> usersInHouseholder = em.createNamedQuery("UserInHousehold.findByUserId", UserInHousehold.class).setParameter("user", u).getResultList();
      if (!usersInHouseholder.isEmpty()) {
        result.addAll(usersInHouseholder.stream().map(UserInHousehold::getHousehold).collect(Collectors.toSet()));
      }
      return result.stream().collect(Collectors.toList());
    } catch (Exception e) {
      System.out.println("---------------------");
      System.out.println(e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  public Household addHousehold(Household household) {
    EntityTransaction et = em.getTransaction();
    if (!et.isActive()) {
      et.begin();
    }
    em.persist(household);
    et.commit();

    return household;
  }

  public UserInHousehold addUserInHousehold(UserInHousehold userInHousehold) {
    em.persist(userInHousehold);
    return userInHousehold;
  }

  public void deleteHousehold(Household household) {
    EntityTransaction et = em.getTransaction();
    if (!et.isActive()) {
      et.begin();
    }
    em.remove(household);
    et.commit();
  }

  public void deleteUserFromHousehold(UserInHousehold householdUser) {
    em.remove(householdUser);
  }

  public List<UserInHousehold> getUserInHouseholder(int householdId) {
    try {
      return em.createNamedQuery("UserInHousehold.findByHouseholdId", UserInHousehold.class).setParameter("household", householdId).getResultList();
    } catch (Exception e) {
      System.err.println("*************");
      System.out.println(e.getMessage());
      return Collections.EMPTY_LIST;
    }
  }

  public void deleteShoppingList(ShoppingList shoppingList) {
    em.remove(shoppingList);
  }

  public void deleteItem(Item item) {
    em.remove(item);
  }

  public Product createProduct(Product product) {
    em.persist(product);
    return product;
  }

  public Item createItem(Item item) {
    em.persist(item);
    return item;
  }
}
