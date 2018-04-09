package com.controllers;

import com.query.DataQuery;

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
  @ManagedProperty(value = "#{applicationManager}")
  ApplicationManager manager;
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
      return "calendars";
    }
    setMessage("Nesprávne meno alebo heslo. Skúste znova.");
    return "users";
  }

  public void checkLoggin() {
    if (userId != 0) {
      manager.setCalendar(userId, null);
      try {
        String uri = "calendars.xhtml";
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

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

}
