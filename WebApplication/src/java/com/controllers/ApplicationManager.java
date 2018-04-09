package com.controllers;

import com.entity.Calendar;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;

/**
 *
 * @author Livia
 */
@ManagedBean
@ApplicationScoped
public class ApplicationManager implements Serializable {
  //
  String version = "1.0";
  @Resource(lookup = "jms/topicfactory")
  private TopicConnectionFactory connectionFactory;

  @Resource(lookup = "jms/topic")
  private Topic topic;
  JMSContext context;

  private Map<Integer, Calendar> usersCalendar;

  public ApplicationManager() {
    usersCalendar = new HashMap<>();
  }

  @PostConstruct
  private void init() {
    context = connectionFactory.createContext();
  }

  public void newUser(int id) {
    usersCalendar.put(id, null);
  }

  public void setCalendar(int userId, Calendar cal) {
    usersCalendar.put(userId, cal);
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  private void sendResponse(String response, String originalMsgId) throws JMSException {
    Message msg = context.createTextMessage(response);
    msg.setJMSCorrelationID(originalMsgId);
    msg.setStringProperty("Direction", "ToClient");
    context.createProducer().send(topic, msg);
  }

  public void findFreeTime(String comp, String msgId) throws JMSException {
    double result = 0.0;
    String args[] = comp.split(";");
    //vyhladavanie
    sendResponse(Double.toString(result), msgId);

  }
}
