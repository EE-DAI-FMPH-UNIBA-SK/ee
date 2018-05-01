package jsf;

import entity.Household;
import entity.Recipe;
import entity.ShoppingList;
import session.SessionUtils;
import websocket.FindWebsocket;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Livia
 */
@ManagedBean
@ApplicationScoped
public class ApplicationManager implements Serializable {
  //
  String version = "1.0";

  private Map<Integer, Household> usersHousholder;
  private Map<Integer, FindWebsocket> userToWs;

  public ApplicationManager() {
    usersHousholder = new HashMap<>();
    userToWs = new HashMap<>();
  }

  public void newUser(int id) {
    usersHousholder.put(id, null);
  }

  public void logout(int userId) {
    HttpSession session = SessionUtils.getSession();
    session.setAttribute("userid", 0);
    usersHousholder.remove(userId);
  }

  public void checkLoggin(int userId) {
    if (!usersHousholder.containsKey(userId)) {
      try {
        String uri = "users.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
      } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      }
    }
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

  public void setSelectedRecipe(int userId, Recipe recipe) {
    if (usersHousholder.containsKey(userId)) {
      usersHousholder.get(userId).setSelectedRecipe(recipe);
    }
  }

  public Recipe getSelectedRecipe(int userId) {
    if (usersHousholder.containsKey(userId)) {
      return usersHousholder.get(userId).getSelectedRecipe();
    }
    return null;
  }

  public void addUserIdtoWs(int userId, FindWebsocket wsRef) {
    userToWs.put(userId, wsRef);
  }

  public void removeWsRef(FindWebsocket wsRef) {
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
