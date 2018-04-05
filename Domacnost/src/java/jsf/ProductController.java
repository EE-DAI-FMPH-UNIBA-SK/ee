package jsf;

import dataQuery.DataQuery;
import entity.Product;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Product")
@SessionScoped
public class ProductController {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers manager;
  String name;
  int objem;
  int count;

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getObjem() {
    return objem;
  }

  public void setObjem(int objem) {
    this.objem = objem;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public Product createProduct() {
    Product p = new Product(name, objem, count);
    return DataQuery.getInstance().createProduct(p);
  }

}
