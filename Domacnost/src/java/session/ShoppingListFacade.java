package session;

import entity.ShoppingList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author Livia
 */
@Stateless
public class ShoppingListFacade extends AbstractFacade<ShoppingList> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ShoppingListFacade() {
    super(ShoppingList.class);
  }

  public ShoppingList findByName(String name) {
    try {
      TypedQuery<ShoppingList> q = em.createNamedQuery("ShoppingList.findByName", ShoppingList.class).setParameter("name", name);
      return q.getSingleResult();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public void deleteShoppingList(ShoppingList shoppingList) {
    em.remove(em.merge(shoppingList));
  }
}
