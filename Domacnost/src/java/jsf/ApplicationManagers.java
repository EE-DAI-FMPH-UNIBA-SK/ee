package jsf;

import entity.Household;
import entity.ShoppingList;
import websocket.FindWebsocket;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author Livia
 */
@ManagedBean
@ApplicationScoped
public class ApplicationManagers implements Serializable {
  //
  String version = "1.0";

  private Map<Integer, Household> usersHousholder;
  private Map<Integer, FindWebsocket> userToWs;

  public ApplicationManagers() {
    usersHousholder = new HashMap<>();
    userToWs = new HashMap<>();
  }

  public void newUser(int id) {
    usersHousholder.put(id, null);
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public boolean setHousehold(int userId, Household h) {
    if (usersHousholder.containsKey(userId)) {
      usersHousholder.put(userId, h);
      return true;
    }
    return false;
  }

  public Household getSelectHousehold(int userId) {
    if (usersHousholder.containsKey(userId)) {
      return usersHousholder.get(userId);
    }
    return null;
  }

  public void setSelectedShoppingList(int userId, ShoppingList shoppingList) {
    if (usersHousholder.containsKey(userId)) {
      usersHousholder.get(userId).setSelectedShoppingList(shoppingList);
    }
  }

  public ShoppingList getSelectedShoppingList(int userId) {
    if (usersHousholder.containsKey(userId)) {
      return usersHousholder.get(userId).getSelectedShoppingList();
    }
    return null;
  }

  void addUserIdtoWs(int userId, FindWebsocket wsRef) {
    userToWs.put(userId, wsRef);
  }

  void removeWsRef(FindWebsocket wsRef) {
    for (Map.Entry<Integer, FindWebsocket> e : userToWs.entrySet()) {
      if (e.getValue() == wsRef) {
        userToWs.remove(e.getKey());
        break;
      }
    }
  }

  public void reply(int userId, String result) {
    FindWebsocket ws = userToWs.get(userId);
    if (ws != null) {
      ws.reply(result);
    }
  }
}
