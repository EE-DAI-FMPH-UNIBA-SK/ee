package session;

import entity.Ingredient;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@Stateless
public class IngredientFacade extends AbstractFacade<Ingredient> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public IngredientFacade() {
    super(Ingredient.class);
  }

  public void deleteIngredient(Ingredient ingredient) {
    em.remove(em.merge(ingredient));
  }
}
