package jsf;

import entity.Product;
import session.ProductFacade;
import session.SessionUtils;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Product")
@SessionScoped
public class ProductController implements Serializable {
  //
  @Inject
  ApplicationManager manager;
  private String name;
  private double value;
  private int userId;

  @EJB
  private ProductFacade pf;

  public ProductController() {
    userId = SessionUtils.getUserId();
  }

  public ApplicationManager getManager() {
    return manager;
  }

  public void setManager(ApplicationManager manager) {
    this.manager = manager;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  public Product createProduct() {
    Product p = new Product(name, value);
    setName("");
    setValue(0);
    return pf.createProduct(p);
  }

  public List<Product> getAllProducts() {
    List<Product> result = pf.getAllProduct();
    result.removeAll(manager.getSelectedShoppingList(userId).getItemCollection().stream().map(i -> i.getProduct()).collect(Collectors.toList()));
    return result;
  }

}
