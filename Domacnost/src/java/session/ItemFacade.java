package session;

import entity.Item;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@Stateless
public class ItemFacade extends AbstractFacade<Item> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ItemFacade() {
    super(Item.class);
  }

  public void deleteItem(Item item) {
    em.remove(em.merge(item));
  }

  public Item createItem(Item item) {
    em.persist(item);
    return item;
  }

  public void update(Item item) {
//    em.refresh(item);
    em.merge(item);
  }
}
