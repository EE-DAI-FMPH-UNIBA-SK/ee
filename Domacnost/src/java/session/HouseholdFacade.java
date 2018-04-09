package session;

import entity.Household;
import entity.User;
import entity.UserInHousehold;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@Stateless
public class HouseholdFacade extends AbstractFacade<Household> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public HouseholdFacade() {
    super(Household.class);
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

    em.persist(household);
    return household;
  }

  public void deleteHousehold(Household household) {
    em.remove(em.merge(household));
  }

}
