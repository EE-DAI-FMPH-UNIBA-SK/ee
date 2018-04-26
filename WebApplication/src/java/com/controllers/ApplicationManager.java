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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
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

import static java.util.stream.Collectors.toList;

/**
 *
 * @author Livia
 */
@ManagedBean
@ApplicationScoped
public class ApplicationManager implements Serializable {
  private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
  private static final SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
  private static final SimpleDateFormat finderSdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
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
  @Resource(lookup = "jms/topicfactory")
  private TopicConnectionFactory connectionFactory;

  @Resource(lookup = "jms/topic")
  private Topic topic;
  JMSContext context;

  private Map<Integer, User> users;

  public ApplicationManager() {
    users = new HashMap<>();
  }

  @PostConstruct
  private void init() {
    context = connectionFactory.createContext();
  }

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

  //jms
  private void sendResponse(String response, String originalMsgId) throws JMSException {
    Message msg = context.createTextMessage(originalMsgId + "#" + response);
    msg.setStringProperty("Direction", "ToClient");
    context.createProducer().send(topic, msg);
    System.out.println("com.controllers.ApplicationManager.sendResponse()");
  }

  public void findFreeTime(String msg, String msgId) throws JMSException {
    try {
      String result = "freeTime;";
      Set<Event> events = new HashSet<>();
      String[] msgValue = msg.split(";");
      String[] usersId = msgValue[0].substring(6).split(",");
      Date start = finderSdf.parse(msgValue[1]);
      Date end = finderSdf.parse(msgValue[2]);
      int length = Integer.valueOf(msgValue[3]);
      for (int i = 0; i < usersId.length; i++) {
        User u = DataQuery.getInstance().getUserById(Integer.valueOf(usersId[i]));
        events.addAll(u.getCalendarCollection().stream()
            .map(c -> c.getEventincalendarCollection().stream().collect(toList()))
            .flatMap(List::stream)
            .map(eic -> eic.getEvent())
            .collect(Collectors.toSet()));
      }
      Set<List<Date>> intervals = new HashSet<>();
      for (Event e : events) {
        if ((e.getStart().after(start) && e.getStart().before(end)) || (e.getEndDate().after(start) && e.getEndDate().before(end))) {
          java.util.Calendar c = java.util.Calendar.getInstance();
          c.setTime(e.getStartDate());
          c.add(java.util.Calendar.MILLISECOND, (int) e.getStart().getTime() + 3600000);
          java.util.Calendar c1 = java.util.Calendar.getInstance();
          c1.setTime(e.getEndDate());
          c.add(java.util.Calendar.MILLISECOND, (int) e.getStart().getTime() + 3600000);
          c.add(java.util.Calendar.HOUR, e.getLength());
          if (e.getIter() != null && !e.getIter().equals("")) {
            String[] iter = e.getIter().split(",");

            for (int i = 0; i < iter.length; i++) {
              while (c.before(c1) && c.before(end) && c.after(start)) {
                int day = c.get(java.util.Calendar.DAY_OF_WEEK);
                if (day == 1) {
                  day = 6;
                } else {
                  day -= 2;
                }
                if (Integer.valueOf(iter[i]) == day) {
                  java.util.Calendar c2 = java.util.Calendar.getInstance();
                  c2.setTime(c.getTime());
                  c2.add(java.util.Calendar.HOUR, e.getLength());
                  List<Date> interval = new ArrayList<>();
                  interval.add(c.getTime());
                  interval.add(c2.getTime());
                  intervals.add(interval);
                } else {
                  c.add(java.util.Calendar.DAY_OF_YEAR, 1);
                }
              }
            }

          } else {
            java.util.Calendar c2 = java.util.Calendar.getInstance();
            c2.setTime(c.getTime());
            c2.add(java.util.Calendar.HOUR, e.getLength());
            List<Date> interval = new ArrayList<>();
            interval.add(c.getTime());
            interval.add(c2.getTime());
            intervals.add(interval);
          }
        }
      }
      List<List<Date>> sortedIntervals = intervals.stream().sorted((l1, l2) -> l1.get(1).compareTo(l2.get(1))).collect(Collectors.toList());
      List<List<Date>> resultIntervals = new ArrayList<>();
      for (int i = 0; i < sortedIntervals.size() - 1; i++) {
        if (length <= (int) (sortedIntervals.get(i + 1).get(0).getTime() - sortedIntervals.get(i).get(1).getTime()) / 3600000) {
          List<Date> r = new ArrayList<>();
          r.add(sortedIntervals.get(i).get(1));
          r.add(sortedIntervals.get(i + 1).get(0));
        }
      }
      for (int i = 0; i < resultIntervals.size(); i++) {
        result += finderSdf.format(resultIntervals.get(i).get(0)) + "-";
        result += finderSdf.format(resultIntervals.get(i).get(1)) + ";";
      }
      if (resultIntervals.isEmpty()) {
        result += msgValue[1] + "-" + msgValue[2];
      }
      sendResponse(result, msgId);
    } catch (ParseException ex) {
      Logger.getLogger(ApplicationManager.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void addJointEvent(String msg, String msgId) throws JMSException {
    String result = "event;";
    try {
      String[] msgValue = msg.split(";");
      String[] usersId = msgValue[0].split(",");
      String name = msgValue[1];
      Date startDate = finderSdf.parse(msgValue[2]);
      java.util.Calendar endDate = java.util.Calendar.getInstance();
      endDate.setTime(startDate);
      endDate.add(java.util.Calendar.DAY_OF_YEAR, 1);
      Date start = finderSdf.parse(msgValue[3]);
      int length = Integer.valueOf(msgValue[4]);
      Event e = new Event();
      e.setName(name);
      e.setStartDate(startDate);
      e.setStart(start);
      e.setLength(length);
      e.setIter("");
      e.setState(0);
      e.setEndDate(endDate.getTime());
      e = DataQuery.getInstance().addEvent(e);
      for (int i = 0; i < usersId.length; i++) {
        User u = DataQuery.getInstance().getUserById(Integer.valueOf(usersId[i]));
        Collection<Calendar> calendars = u.getCalendarCollection();
        for (Calendar c : calendars) {
          EventInCalendar eic = new EventInCalendar(c, e);
          DataQuery.getInstance().addCalendarEvent(eic);
        }
      }
      result += "Event was created.";
    } catch (ParseException ex) {
      result += "Error. Event was not created.";
    }
    sendResponse(result, msgId);
  }
}
