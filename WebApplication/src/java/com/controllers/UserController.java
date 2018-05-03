package com.controllers;

import com.entity.User;
import com.query.DataQuery;

import java.io.IOException;
import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Livia
 */
@Named("User")
@SessionScoped
public class UserController implements Serializable {
  //
  private String name;
  private String password;
  private String message;
  private int userId;

  @Inject
  ApplicationManager manager;

  public UserController() {
    int id = SessionUtils.getUserId();
    if (id != 0) {
      userId = id;
    }
  }

  public String loginControl() {
    userId = DataQuery.getInstance().loginControl(name, password);
    if (userId != 0) {
      User user = DataQuery.getInstance().getUserById(userId);
      manager.newUser(userId, user);
      HttpSession session = SessionUtils.getSession();
      session.setAttribute("userid", userId);
      message = "";
      name = "";
      password = "";
      return "calendars";
    }
    setMessage("Nesprávne meno alebo heslo. Skúste znova.");
    return "users";
  }

  public void checkLoggin() {
    if (userId != 0) {
      try {
        User user = DataQuery.getInstance().getUserById(userId);
        String uri = "calendars.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().dispatch(uri);
      } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      }
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

}
