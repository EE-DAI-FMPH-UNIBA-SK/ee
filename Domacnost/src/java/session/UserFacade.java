package session;

import entity.User;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@Stateless
public class UserFacade extends AbstractFacade<User> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public UserFacade() {
    super(User.class);
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

  public List<User> getAllUsers() {
    return em.createNamedQuery("User.findAll", User.class).getResultList();
  }

}
