package jsf;

import entity.User;
import session.SessionUtils;
import session.UserFacade;

import java.io.IOException;
import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "User")
@SessionScoped
public class UserController implements Serializable {
  //
  @Inject
  ApplicationManager manager;
  private String name;
  private String email;
  private String password;
  private String message;

  @EJB
  private UserFacade uf;
  private int userId;

  public UserController() {
    java.util.Calendar c = java.util.Calendar.getInstance();
    int id = SessionUtils.getUserId();
    if (id != 0) {
      userId = id;
    }
  }

  public String loginControl() {
    userId = uf.loginControl(email, password);
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
    return "users";
  }

  public void checkLoggin() {
    if (userId != 0) {
      int id = SessionUtils.getUserId();
      if (id == 0) {
        return;
      }
      manager.setHousehold(userId, null);
      try {
        String uri = "householders.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().dispatch(uri);
      } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      }
    }
  }

  public UserFacade getUf() {
    return uf;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public ApplicationManager getManager() {
    return manager;
  }

  public void setManager(ApplicationManager manager) {
    this.manager = manager;
  }

  public void addNewUser() {
    User u = new User(name, email, password);
    uf.create(u);
    name = "";
    email = "";
    password = "";
  }
}
