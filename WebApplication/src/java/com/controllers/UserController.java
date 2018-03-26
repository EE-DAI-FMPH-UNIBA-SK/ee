package com.controllers;

import com.query.DataQuery;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "User")
@SessionScoped
public class UserController implements Serializable {
  //

  private String email;
  private String password;

  private int userId;

  public String loginControl() {
    FacesContext context = FacesContext.getCurrentInstance();
    userId = DataQuery.getInstance().loginControl(email, password);
    if (userId != 0) {
      return "calendars";
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

}
