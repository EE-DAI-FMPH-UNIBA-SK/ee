package session;

import entity.Product;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@Stateless
public class ProductFacade extends AbstractFacade<Product> {
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;
  //

  @Override
  protected EntityManager getEntityManager() {
    return em;
  }

  public ProductFacade() {
    super(Product.class);
  }

  public Product createProduct(Product product) {
    em.persist(product);
    return product;
  }

  public List<Product> getAllProduct() {
    return em.createNamedQuery("Product.findAll", Product.class).getResultList();
  }
}
