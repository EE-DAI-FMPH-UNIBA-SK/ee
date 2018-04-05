package jsf;

import dataQuery.DataQuery;
import entity.Household;
import entity.Item;
import entity.Product;
import entity.ShoppingList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "ShoppingList")
@SessionScoped
public class ShoppingListsController {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers manager;
  private Household selectedHousehold;
  private int userId;
  private ShoppingList selectedShoppingList;

  public ShoppingListsController() {
    userId = SessionUtils.getUserId();
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
    this.selectedShoppingList = selectedShoppingList;
  }

  public void deleteShoppingList(ShoppingList shoppingList) {
    DataQuery.getInstance().deleteShoppingList(shoppingList);
  }

  public String showItem(Item item) {
    return "";
  }

  public void deleteItemFromShoppingList(Item item) {
    DataQuery.getInstance().deleteItem(item);
    selectedShoppingList.removeItem(item);
  }

  public void addNewItem(Product p) {
    Item item = new Item(p, selectedShoppingList);
    item = DataQuery.getInstance().createItem(item);
    selectedShoppingList.addItem(item);
  }
}
