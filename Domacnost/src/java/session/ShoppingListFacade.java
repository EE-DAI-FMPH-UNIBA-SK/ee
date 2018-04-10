package session;

import entity.ShoppingList;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

  public void deleteShoppingList(ShoppingList shoppingList) {
    em.remove(em.merge(shoppingList));
  }
}
