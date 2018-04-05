package jsf;

import dataQuery.DataQuery;
import entity.Household;
import entity.User;
import entity.UserInHousehold;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "User")
@SessionScoped
public class UserController implements Serializable {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers manager;
  private String email;
  private String password;
  private String message;

  private int userId;

  public UserController() {
    int id = SessionUtils.getUserId();
    if (id != 0) {
      userId = id;
    }
  }

  public String loginControl() {
    userId = DataQuery.getInstance().loginControl(email, password);
    if (userId != 0) {
      manager.newUser(userId);
      HttpSession session = SessionUtils.getSession();
      session.setAttribute("userid", userId);
      message = "";
      email = "";
      password = "";
      return "householders";
    }
    setMessage("Nesprávne meno alebo heslo. Skúste znova.");
    return "";
  }

  public void checkLoggin() {
    System.out.println(userId);
    if (userId != 0) {
      manager.setHousehold(userId, null);
      try {
//        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
//        ec.redirect(ec.getRequestContextPath() + "/householders.xhtml");
        String uri = "householders.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().dispatch(uri);
      } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      }
    }

  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public void addUserInHousehold() {
    User user = DataQuery.getInstance().getUserByEmail(email);
    if (user != null) {
      Household h = manager.getSelectHousehold(userId);
      UserInHousehold uih = new UserInHousehold(user, h);
      DataQuery.getInstance().addUserInHousehold(uih);
      h.addUser(uih);
      setMessage("");
    } else {
      setMessage("User does not exist.");
    }
  }

}
