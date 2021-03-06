package com.controllers;

import com.entity.Calendar;
import com.entity.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author Livia
 */
@Named("Calendar")
@SessionScoped
public class CalendarController implements Serializable {
  //
  private List<Calendar> calendars;
  private Calendar showCalendar;
  @Inject
  ApplicationManager manager;
  private int userId;
  private User user;
  private Part file;

  public CalendarController() {
  }

  @PostConstruct
  private void init() {
    int id = SessionUtils.getUserId();
    if (id != 0) {
      userId = id;
      user = manager.getUser(userId);
      calendars = user.getCalendarCollection().stream().collect(Collectors.toList());
    } else {
      try {
        String uri = "users.xhtml";
        FacesContext.getCurrentInstance().getExternalContext().dispatch(uri);
      } catch (IOException ex) {
        System.out.println(ex.getMessage());
        ex.printStackTrace();
      }
    }
  }

  public List<Calendar> getCalendars() {
    return calendars;
  }

  public void setCalendars(List<Calendar> calendars) {
    this.calendars = calendars;
  }

  public Calendar getShowCalendar() {
    return showCalendar;
  }

  public void setShowCalendar(Calendar showCalendar) {
    this.showCalendar = showCalendar;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Part getFile() {
    return file;
  }

  public void setFile(Part file) {
    this.file = file;
  }

  public void saveFile() {
    try (InputStream input = file.getInputStream()) {
      Files.copy(input, new File(System.getProperty("java.io.tmpdir"), file.getSubmittedFileName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
