/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.entity.Calendar;
import com.entity.Event;
import com.entity.EventInCalendar;
import com.entity.User;
import com.query.DataQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * REST Web Service
 *
 * @author MaX
 */
@Path("createEvent")
public class CreateEventResource {

  /** Creates a new instance of CreateEventResource */
  public CreateEventResource() {
  }

  static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

  @GET
  @Path("{name}/{date}/{time}/{length}/{userId}")
  public boolean create(@PathParam("name") String name,
      @PathParam("date") String date,
      @PathParam("time") String time,
      @PathParam("length") int length,
      @PathParam("userId") int userId) {
    try {
      Event entity = new Event(name, sdfDate.parse(date), sdfTime.parse(time), length);
      entity.setType("shoppingList");
      entity = DataQuery.getInstance().addEvent(entity);
      User user = DataQuery.getInstance().getUserById(userId);
      for (Calendar c : user.getCalendarCollection()) {
        EventInCalendar eic = new EventInCalendar(c, entity);
        DataQuery.getInstance().addCalendarEvent(eic);
      }
      return entity != null;
    } catch (ParseException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}
