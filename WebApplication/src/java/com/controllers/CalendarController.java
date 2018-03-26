package com.controllers;

import JSON.CallendarJSON;
import XML.CalendarsXML;
import com.entity.Calendar;
import com.entity.Event;
import com.entity.Eventincalendar;
import com.entity.User;
import com.query.DataQuery;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.servlet.http.Part;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;
import org.codehaus.jackson.map.JsonMappingException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Calendar")
@ApplicationScoped
public class CalendarController implements Serializable {
  private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
  private static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
  private static final Map<String, String> days;

  static {
    days = new HashMap<String, String>();
    days.put("Po", "0");
    days.put("Ut", "1");
    days.put("St", "2");
    days.put("Št", "3");
    days.put("Pi", "4");
  }
  //
  private List<Calendar> calendars;
  private Calendar showCalendar;

  private int userId = 1;
  private Part file;

  public List<Calendar> getCalendars() {
    calendars = DataQuery.getInstance().getCalendars();
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

  public void importXmlData(String fileName) {
    if (showCalendar != null) {
      try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream fIs = new FileInputStream(System.getProperty("java.io.tmpdir") + "/" + fileName);

        Document doc = dBuilder.parse(fIs);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("lesson");
        for (int temp = 0; temp < nList.getLength(); temp++) {

          Node nNode = nList.item(temp);

          if (nNode.getNodeType() == Node.ELEMENT_NODE) {

            Element eElement = (Element) nNode;
            Event e = new Event();
            e.setName(eElement.getElementsByTagName("subject").item(0).getTextContent());
            e.setType(eElement.getElementsByTagName("type").item(0).getTextContent());
            e.setIter(days.get(eElement.getElementsByTagName("day").item(0).getTextContent()));
            Date start = sdfTime.parse(eElement.getElementsByTagName("start").item(0).getTextContent());
            Date end = sdfTime.parse(eElement.getElementsByTagName("end").item(0).getTextContent());
            java.util.Calendar cal = java.util.Calendar.getInstance();
            e.setStartDate(cal.getTime());
            cal.add(java.util.Calendar.MONTH, 6);
            e.setEndDate(cal.getTime());
            e.setStartDate(new Date());
            e.setStart(start);
            e.setLength((int) ((end.getTime() - start.getTime()) * 0.000000277778));
            e.setState(0);
            Event e2 = DataQuery.getInstance().findEventByName(e.getName());
            if (e2 == null || e2.getType() != e.getType()) {
              e2 = DataQuery.getInstance().addEvent(e);
              Eventincalendar eL = new Eventincalendar(showCalendar, e2);
              DataQuery.getInstance().addCalendarEvent(eL);
            } else {
              Eventincalendar el = DataQuery.getInstance().getEventList(showCalendar, e2);
              if (el == null) {
                Eventincalendar eL = new Eventincalendar(showCalendar, e2);
                DataQuery.getInstance().addCalendarEvent(eL);
              }
            }
          }
        }
      } catch (ParserConfigurationException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      } catch (SAXException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      } catch (ParseException ex) {
        ex.printStackTrace();
        System.out.println(ex.getMessage());
      }
    }
  }

  public void importJsonData(String fileName) {
    try {

      JsonFactory jfactory = new JsonFactory();
      InputStream is = new FileInputStream(System.getProperty("java.io.tmpdir") + "/" + fileName);

      JsonParser jParser = jfactory.createJsonParser(is);

      while (jParser.nextToken() != JsonToken.END_OBJECT) {
        String fieldname = jParser.getCurrentName();
        if ("calendars".equals(fieldname)) {
          while (jParser.nextToken() != JsonToken.END_ARRAY) {
            Calendar c = new Calendar();
            while (jParser.nextToken() != JsonToken.END_OBJECT) {
              String fieldname2 = jParser.getCurrentName();
              if ("calendarName".equals(fieldname2)) {
                jParser.nextToken();
                c.setName(jParser.getText());
              }
              if ("visible".equals(fieldname2)) {
                jParser.nextToken();
                c.setVisible(jParser.getBooleanValue());
              }

              if ("events".equals(fieldname2)) {
                Calendar c2 = DataQuery.getInstance().findCalendarsByName(c.getName());
                if (c2 == null) {
                  c.setUser(DataQuery.getInstance().getUserById(userId));
                  c2 = DataQuery.getInstance().addCalendar(c, false);
                  calendars.add(c2);
                }
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                  Event e = new Event();
                  while (jParser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldname3 = jParser.getCurrentName();
                    if ("name".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setName(jParser.getText());
                    }
                    if ("start_date".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setStartDate(sdfDate.parse(jParser.getText()));
                    }
                    if ("end_date".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setEndDate(sdfDate.parse(jParser.getText()));
                    }
                    if ("start".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setStart(sdfTime.parse(jParser.getText()));
                    }
                    if ("length".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setLength(jParser.getIntValue());
                    }
                    if ("type".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setType(jParser.getText());
                    }
                    if ("state".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setState(jParser.getIntValue());
                    }
                    if ("iter".equals(fieldname3)) {
                      jParser.nextToken();
                      e.setIter(jParser.getText());
                    }
                  }
                  Event e2 = DataQuery.getInstance().findEventByName(e.getName());
                  if (e2 == null || e2.getType() != e.getType()) {
                    e2 = DataQuery.getInstance().addEvent(e);
                  }
                  Eventincalendar eL = new Eventincalendar(c2, e2);
                  DataQuery.getInstance().addCalendarEvent(eL);
                }
              }
            }
          }
        }
      }
      jParser.close();
    } catch (JsonGenerationException e) {

      e.printStackTrace();

    } catch (JsonMappingException e) {

      e.printStackTrace();

    } catch (IOException e) {

      e.printStackTrace();

    } catch (ParseException ex) {
      ex.printStackTrace();
    }
  }

  public String getShowCalendars() {
    if (calendars == null) {
      getCalendars();
    }
    CalendarsXML xml = new CalendarsXML(calendars);
    return xml.exportXMLData(1);
  }

  public String getEvents(int id) {
    showCalendar = DataQuery.getInstance().getCalendarById(id);
    CallendarJSON json = new CallendarJSON();
    return json.getEvents(id);
  }

  public int addCalendar(String name, boolean visible) {
    Calendar newCalendar = new Calendar(name, visible);
    User user = DataQuery.getInstance().getUserById(1);
    newCalendar.setUser(user);
    newCalendar = DataQuery.getInstance().addCalendar(newCalendar, true);
    calendars.add(newCalendar);
    return newCalendar.getId();
  }

}
