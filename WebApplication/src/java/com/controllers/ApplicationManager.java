package com.controllers;

import JSON.CallendarJSON;
import XML.CalendarsXML;
import com.entity.Calendar;
import com.entity.Event;
import com.entity.EventInCalendar;
import com.entity.User;
import com.query.DataQuery;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@ManagedBean
@ApplicationScoped
public class ApplicationManager implements Serializable {
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
  String version = "1.0";
//  @Resource(lookup = "jms/topicfactory")
//  private TopicConnectionFactory connectionFactory;
//
//  @Resource(lookup = "jms/topic")
//  private Topic topic;
//  JMSContext context;

  private Map<Integer, User> users;

  public ApplicationManager() {
    users = new HashMap<>();
  }

//  @PostConstruct
//  private void init() {
//    context = connectionFactory.createContext();
//  }
  public void newUser(int id, User user) {
    users.put(id, user);
  }

  public User getUser(int userId) {
    return users.get(userId);
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  //websocket
  public String getShowCalendars(int userId) {
    List<Calendar> calendars = DataQuery.getInstance().getUserById(userId).getCalendarCollection().stream().collect(Collectors.toList());
    CalendarsXML xml = new CalendarsXML(calendars);
    return xml.exportXMLData(1);
  }

  public String getEvents(int id) {
    CallendarJSON json = new CallendarJSON();
    return json.getEvents(id);
  }

  public int addCalendar(String name, boolean visible, int userId) {
    Calendar newCalendar = new Calendar(name, visible);
    User user = DataQuery.getInstance().getUserById(userId);
    newCalendar.setUser(user);
    newCalendar = DataQuery.getInstance().addCalendar(newCalendar, true);
    user.addCalendar(newCalendar);
    return newCalendar.getId();
  }

  public void importXmlData(String fileName, int userId) {
    User u = DataQuery.getInstance().getUserById(userId);
    if (u.getSelectedCalendar() != null) {
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
              EventInCalendar eL = new EventInCalendar(u.getSelectedCalendar(), e2);
              DataQuery.getInstance().addCalendarEvent(eL);
            } else {
              EventInCalendar el = DataQuery.getInstance().getEventList(u.getSelectedCalendar(), e2);
              if (el == null) {
                EventInCalendar eL = new EventInCalendar(u.getSelectedCalendar(), e2);
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

  public void importJsonData(String fileName, int userId) {
    try {
      User u = DataQuery.getInstance().getUserById(userId);
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
              c.setUser(u);
              if ("events".equals(fieldname2)) {
                Calendar c2 = DataQuery.getInstance().findCalendarsByName(c.getName());
                if (c2 == null) {
                  c.setUser(DataQuery.getInstance().getUserById(userId));
                  c2 = DataQuery.getInstance().addCalendar(c, false);
                  u.addCalendar(c2);
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
                  EventInCalendar eL = new EventInCalendar(c2, e2);
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

//  private void sendResponse(String response, String originalMsgId) throws JMSException {
//    Message msg = context.createTextMessage(response);
//    msg.setJMSCorrelationID(originalMsgId);
//    msg.setStringProperty("Direction", "ToClient");
//    context.createProducer().send(topic, msg);
//  }
//
//  public void findFreeTime(String comp, String msgId) throws JMSException {
//    double result = 0.0;
//    String args[] = comp.split(";");
//    //vyhladavanie
//    sendResponse(Double.toString(result), msgId);
//  }
}
