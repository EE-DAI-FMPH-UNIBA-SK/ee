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
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Livia
 */
@Named
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
      boolean readCalendars = false;
      boolean readEvents = false;
      List<Event> events = new ArrayList<>();
      Event e = new Event();
      Calendar c = new Calendar();
      InputStream is = new FileInputStream(System.getProperty("java.io.tmpdir") + "/" + fileName);

      JsonParser jParser = Json.createParser(is);
      String keyName = null;

      while (jParser.hasNext()) {
        javax.json.stream.JsonParser.Event event = jParser.next();
        switch (event) {
          case START_ARRAY:
            if (readCalendars) {
              readEvents = true;
            } else {
              readCalendars = true;
            }
            break;
          case START_OBJECT:
            if (readEvents) {
              e = new Event();
            } else {
              c = new Calendar();
            }
            break;
          case END_ARRAY:
            if (readEvents) {
              readEvents = false;
            } else {
              readCalendars = false;
            }
            break;
          case END_OBJECT:
            if (readEvents) {
              events.add(e);
            } else {
              Calendar c2 = DataQuery.getInstance().findCalendarsByName(c.getName());
              if (c2 == null) {
                c.setUser(DataQuery.getInstance().getUserById(userId));
                c2 = DataQuery.getInstance().addCalendar(c, false);
              }
              for (Event e1 : events) {
                Event e2 = DataQuery.getInstance().findEventByName(e1.getName());
                if (e2 == null || e2.getType() != e.getType()) {
                  e2 = DataQuery.getInstance().addEvent(e1);
                }
                EventInCalendar eL = new EventInCalendar(c2, e2);
                DataQuery.getInstance().addCalendarEvent(eL);
              }
              events = new ArrayList<>();
            }
            break;
          case KEY_NAME:
            keyName = jParser.getString();
            break;
          case VALUE_STRING:
            setStringValues(c, e, keyName, jParser.getString());
            break;
          case VALUE_NUMBER:
            setNumberValues(e, keyName, jParser.getInt());
            break;
          case VALUE_FALSE:
            c.setVisible(false);
            break;
          case VALUE_TRUE:
            c.setVisible(true);
            break;
          case VALUE_NULL:
            break;
          default:
        }
      }
      is.close();
      jParser.close();
    } catch (IOException ex) {
      System.out.println(ex.getMessage());
    }
  }

  private static void setStringValues(Calendar c, Event e, String key, String value) {
    switch (key) {
      case "calendarName":
        c.setName(value);
        break;
      case "name":
        e.setName(value);
        break;
      case "start_date":
        try {
          e.setStartDate(sdfDate.parse(value));
        } catch (ParseException ex) {
          System.out.println(ex.getMessage());
        }
        break;
      case "end_date":
        try {
          e.setEndDate(sdfDate.parse(value));
        } catch (ParseException ex) {
          System.out.println(ex.getMessage());
        }
        break;
      case "start":
        try {
          e.setStart(sdfTime.parse(value));
        } catch (ParseException ex) {
          System.out.println(ex.getMessage());
        }
        break;
      case "type":
        e.setType(value);
        break;
      case "iter":
        e.setIter(value);
        break;
      default:
        System.out.println("Unknown Key=" + key);
    }
  }

  private static void setNumberValues(Event e, String keyName, int value) {
    switch (keyName) {
      case "length":
        e.setLength(value);
        break;
      case "state":
        e.setState(value);
        break;
      default:
        System.out.println("Unknown element with key=" + keyName);
    }
  }

  //jms
  private void sendResponse(String response, String originalMsgId) throws JMSException {
    Message msg = context.createTextMessage(originalMsgId + "#" + response);
    msg.setStringProperty("Direction", "ToClient");
    context.createProducer().send(topic, msg);
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
            .map(c -> c.getEventincalendarCollection().stream().collect(Collectors.toList()))
            .flatMap(List::stream)
            .map(eic -> eic.getEvent())
            .collect(Collectors.toSet()));
      }
      Set<List<Date>> intervals = new HashSet<>();
      for (Event e : events) {
        if ((e.getStart().after(start) && e.getStart().before(end))
            || (e.getEndDate().after(start) && e.getEndDate().before(end))
            || (e.getEndDate().after(end) && e.getStartDate().before(start))) {
          java.util.Calendar c = java.util.Calendar.getInstance();
          c.setTime(e.getStartDate());
          c.add(java.util.Calendar.MILLISECOND, (int) e.getStart().getTime() + 3600000);
          java.util.Calendar c1 = java.util.Calendar.getInstance();
          c1.setTime(e.getEndDate());
          c1.add(java.util.Calendar.MILLISECOND, (int) e.getStart().getTime() + 3600000);
          c1.add(java.util.Calendar.HOUR, e.getLength());
          if (e.getIter() != null && !e.getIter().equals("")) {
            List<String> iter = Arrays.asList(e.getIter().split(","));

            while (c.before(c1) && c.getTime().before(end)) {
              if (c.getTime().after(start)) {
                int day = c.get(java.util.Calendar.DAY_OF_WEEK);
                day -= 1;
                if (iter.contains(String.valueOf(day))) {
                  java.util.Calendar c2 = java.util.Calendar.getInstance();
                  c2.setTime(c.getTime());
                  c2.add(java.util.Calendar.HOUR, e.getLength());
                  List<Date> interval = new ArrayList<>();
                  interval.add(c.getTime());
                  interval.add(c2.getTime());
                  intervals.add(interval);
                }
              }
              c.add(java.util.Calendar.DAY_OF_YEAR, 1);

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
      intervals.stream();
      List<List<Date>> sortedIntervals = intervals.stream()
          .sorted((l1, l2) -> l1.get(1)
          .compareTo(l2.get(1)))
          .collect(Collectors.toList());
      List<List<Date>> resultIntervals = new ArrayList<>();
      Date s = start;
      for (int i = 0; i < sortedIntervals.size() - 1; i++) {
        if (length <= (int) (sortedIntervals.get(i).get(0).getTime() - s.getTime()) / 3600000) {
          List<Date> r = new ArrayList<>();
          r.add(s);
          r.add(sortedIntervals.get(i).get(0));
          resultIntervals.add(r);
          s = sortedIntervals.get(i).get(1);
        }
      }
      if (length <= (int) (end.getTime() - sortedIntervals.get(sortedIntervals.size() - 1).get(1).getTime()) / 3600000) {
        List<Date> r = new ArrayList<>();
        r.add(sortedIntervals.get(sortedIntervals.size() - 1).get(1));
        r.add(end);
        resultIntervals.add(r);
      }
      for (int i = 0; i < resultIntervals.size(); i++) {
        result += finderSdf.format(resultIntervals.get(i).get(0)) + " - ";
        result += finderSdf.format(resultIntervals.get(i).get(1)) + ";";
      }
      if (resultIntervals.isEmpty()) {
        result += msgValue[1] + " - " + msgValue[2];
      }
      sendResponse(result, msgId);

    } catch (ParseException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public void addJoinEvent(String msg, String msgId) throws JMSException {
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

  public void showShoppingListDetail() {
  }
}
