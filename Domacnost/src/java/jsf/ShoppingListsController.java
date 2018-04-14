package jsf;

import entity.Household;
import entity.Item;
import entity.Product;
import entity.ShoppingList;
import session.ItemFacade;
import session.SessionUtils;
import session.ShoppingListFacade;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "ShoppingList")
@SessionScoped
public class ShoppingListsController implements Serializable {
  //
  @Inject
  ApplicationManagers manager;
  private Household selectedHousehold;
  private int userId;
  private ShoppingList selectedShoppingList;
  private String message;
  private Product newProduct;
  private double count;
  private String name;
  private Date startDate = null;
  private Date endDate = null;

  @EJB
  private ShoppingListFacade slf;
  @EJB
  private ItemFacade ifac;

  @PostConstruct
  private void init() {
    userId = SessionUtils.getUserId();
    selectedHousehold = null;
    manager.setSelectedShoppingList(userId, selectedShoppingList);
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public Household getSelectedHousehold() {
    selectedHousehold = manager.getSelectHousehold(userId);
    return selectedHousehold;
  }

  public void setSelectedHousehold(Household selectedHousehold) {
    this.selectedHousehold = selectedHousehold;
    manager.setHousehold(userId, selectedHousehold);
  }

  public ShoppingList getSelectedShoppingList() {
    return selectedShoppingList;
  }

  public void setSelectedShoppingList(ShoppingList selectedShoppingList) {
    setMessage("");
    this.selectedShoppingList = selectedShoppingList;
    manager.setSelectedShoppingList(userId, selectedShoppingList);
  }

  public void addShoppingList() {
    setMessage("");
    if (name != "" && startDate != null && endDate != null) {
      ShoppingList newShoppingList = new ShoppingList(name, startDate, endDate, selectedHousehold);
      slf.create(newShoppingList);
      selectedHousehold.addShoppingList(newShoppingList);
      selectedShoppingList = null;
      setName("");
      setStartDate(null);
      setEndDate(null);
    } else {
      setMessage("You can not create a household. Check the correctness of the data");
    }
  }

  public void deleteShoppingList(ShoppingList shoppingList) {
    setMessage("");
    try {
      slf.deleteShoppingList(shoppingList);
      selectedHousehold.deleteShoppingList(shoppingList);
    } catch (Exception e) {
      setMessage("Unable to delete the selected shopping list");
    }
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Product getNewProduct() {
    return newProduct;
  }

  public void setNewProduct(Product newProduct) {
    this.newProduct = newProduct;
  }

  public double getCount() {
    return count;
  }

  public void setCount(double count) {
    this.count = count;
  }

  public String getName() {
    return name;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public void changeItemState(Item item) {
    ifac.update(item);
  }

  public void deleteItemFromShoppingList(Item item) {
    ifac.deleteItem(item);
    selectedShoppingList.removeItem(item);
  }

  public void addNewItem() {
    Item item = new Item(newProduct, selectedShoppingList, count);
    item = ifac.createItem(item);
    selectedShoppingList.addItem(item);
  }

}
