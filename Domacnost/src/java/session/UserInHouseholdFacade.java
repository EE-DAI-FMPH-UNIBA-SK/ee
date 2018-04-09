package session;

import entity.UserInHousehold;

import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@Stateless
public class UserInHouseholdFacade extends AbstractFacade<UserInHousehold> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public UserInHouseholdFacade() {
    super(UserInHousehold.class);
  }

  public UserInHousehold addUserInHousehold(UserInHousehold userInHousehold) {
    em.persist(userInHousehold);
    return userInHousehold;
  }

  public void deleteUserFromHousehold(UserInHousehold householdUser) {
    em.remove(em.merge(householdUser));
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
}
