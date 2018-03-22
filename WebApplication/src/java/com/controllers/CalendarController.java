package com.controllers;

import JSON.CallendarJSON;
import XML.KalendareXML;
import com.entity.Calendars;
import com.entity.EventList;
import com.entity.Events;
import com.entity.Users;
import com.query.DataQuery;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
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
@ManagedBean(name = "Calendars")
@ApplicationScoped
public class CalendarController implements Serializable {
  static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
  static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
  //
  private List<Calendars> calendars;
  private Calendars showCalendar;

  private int userId = 1;

  public List<Calendars> getCalendars() {
    calendars = DataQuery.getInstance().getCalendars();
    return calendars;
  }

  public void setCalendars(List<Calendars> calendars) {
    this.calendars = calendars;
  }

  public Calendars getShowCalendar() {
    return showCalendar;
  }

  public void setShowCalendar(Calendars showCalendar) {
    this.showCalendar = showCalendar;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public void importXmlData(String fileName) {
    if (showCalendar != null) {
      try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        InputStream fIs = CalendarController.class.getResourceAsStream("/" + fileName);

        Document doc = dBuilder.parse(fIs);
        doc.getDocumentElement().normalize();

        NodeList nList = doc.getElementsByTagName("lesson");
        for (int temp = 0; temp < nList.getLength(); temp++) {

          Node nNode = nList.item(temp);

          if (nNode.getNodeType() == Node.ELEMENT_NODE) {

            Element eElement = (Element) nNode;
            Events e = new Events();
            e.setName(eElement.getElementsByTagName("subject").item(0).getTextContent());
            switch (eElement.getElementsByTagName("type").item(0).getTextContent()) {
              case "Prednáška":
                e.setType(6);
                break;
              case "Seminár":
                e.setType(8);
                break;
              case "Cvičenie":
                e.setType(7);
                break;
            }
            switch (eElement.getElementsByTagName("day").item(0).getTextContent()) {
              case "Po":
                e.setIter("0");
                break;
              case "Ut":
                e.setIter("1");
                break;
              case "St":
                e.setIter("2");
                break;
              case "Št":
                e.setIter("3");
                break;
              case "Pi":
                e.setIter("4");
                break;
            }
            Date start = sdfTime.parse(eElement.getElementsByTagName("start").item(0).getTextContent());
            Date end = sdfTime.parse(eElement.getElementsByTagName("end").item(0).getTextContent());
            Calendar cal = Calendar.getInstance();
            e.setStartDate(cal.getTime());
            cal.add(Calendar.MONTH, 6);
            e.setEndDate(cal.getTime());
            e.setStartDate(new Date());
            e.setStart(start);
            e.setLength((int) ((end.getTime() - start.getTime()) * 0.000000277778));
            e.setState(0);
            Events e2 = DataQuery.getInstance().findEventByName(e.getName());
            if (e2 == null || e2.getType() != e.getType()) {
              e2 = DataQuery.getInstance().addEvent(e);
              EventList eL = new EventList(showCalendar, e2);
              DataQuery.getInstance().addEventList(eL);
            } else {
              EventList el = DataQuery.getInstance().getEventList(showCalendar, e2);
              if (el == null) {
                EventList eL = new EventList(showCalendar, e2);
                DataQuery.getInstance().addEventList(eL);
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
      InputStream is = CalendarController.class.getResourceAsStream("/" + fileName);
      JsonParser jParser = jfactory.createJsonParser(is);

      while (jParser.nextToken() != JsonToken.END_OBJECT) {
        String fieldname = jParser.getCurrentName();
        if ("calendars".equals(fieldname)) {
          while (jParser.nextToken() != JsonToken.END_ARRAY) {
            Calendars c = new Calendars();
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
                Calendars c2 = DataQuery.getInstance().findCalendarsByName(c.getName());
                if (c2 == null) {
                  c.setUserId(DataQuery.getInstance().getUserById(userId));
                  c2 = DataQuery.getInstance().addCalendar(c, false);
                  calendars.add(c2);
                }
                while (jParser.nextToken() != JsonToken.END_ARRAY) {
                  Events e = new Events();
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
                      e.setType(jParser.getIntValue());
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
                  Events e2 = DataQuery.getInstance().findEventByName(e.getName());
                  if (e2 == null || e2.getType() != e.getType()) {
                    e2 = DataQuery.getInstance().addEvent(e);
                  }
                  EventList eL = new EventList(c2, e2);
                  DataQuery.getInstance().addEventList(eL);
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
    KalendareXML xml = new KalendareXML(calendars);
    return xml.exportXMLData(1);
  }

  public String getEvents(int id) {
    showCalendar = DataQuery.getInstance().getCalendarById(id);
    CallendarJSON json = new CallendarJSON();
    return json.getEvents(id);
  }

  public int addCalendar(String name, boolean visible) {
    Calendars newCalendar = new Calendars(name, visible);
    Users user = DataQuery.getInstance().getUserById(1);
    newCalendar.setUserId(user);
    newCalendar = DataQuery.getInstance().addCalendar(newCalendar, true);
    calendars.add(newCalendar);
    return newCalendar.getId();
  }
}
