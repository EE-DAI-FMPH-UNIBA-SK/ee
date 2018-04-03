package com.jsf;

import com.dataquery.DataQuery;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "User")
@SessionScoped
public class UserController implements Serializable {
  //
  @ManagedProperty(value = "#{applicationManagers}")
  ApplicationManagers householders;
  private String email;
  private String password;

  private int userId;

  public String loginControl() {
    FacesContext context = FacesContext.getCurrentInstance();
    userId = DataQuery.getInstance().loginControl(email, password);
    if (userId != 0) {
      householders.newUser(userId);
      HttpSession session = SessionUtils.getSession();
      session.setAttribute("userid", userId);
//      System.err.println("nastavenev session");
//      System.err.println(SessionUtils.getUserId());
      return "householders";
    }
    RequestContext.getCurrentInstance().update("growl");
    context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Email or password invalid!" + email + password));
    return "";
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

  public ApplicationManagers getHouseholders() {
    return householders;
  }

  public void setHouseholders(ApplicationManagers householders) {
    this.householders = householders;
  }

}
