package jsf;

import session.SessionUtils;
import entity.Product;
import session.ProductFacade;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Product")
@SessionScoped
public class ProductController implements Serializable {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers manager;
  private String name;
  private int value;
  private int count;
  private int userId;

  @EJB
  private ProductFacade pf;

  public ProductController() {
    userId = SessionUtils.getUserId();
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    System.out.println("jsf.ProductController.setManager()");
    this.manager = manager;
  }

  public String getName() {
    System.out.println("jsf.ProductController.getName()");
    return name;
  }

  public void setName(String name) {
    System.out.println("jsf.ProductController.setName()");
    this.name = name;
  }

  public int getValue() {
    return value;
  }

  public void setValue(int value) {
    this.value = value;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Product createProduct() {
    Product p = new Product(name, value, count);
    setName("");
    setCount(0);
    setValue(0);
    return pf.createProduct(p);
  }

  public List<Product> getAllProducts() {
    List<Product> result = pf.getAllProduct();
    result.removeAll(manager.getSelectedShoppingList(userId).getItemCollection().stream().map(i -> i.getProduct()).collect(Collectors.toList()));
    return result;
  }

}
