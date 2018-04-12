package jsf;

import entity.Household;
import entity.User;
import entity.UserInHousehold;
import session.HouseholdFacade;
import session.SessionUtils;
import session.UserFacade;
import session.UserInHouseholdFacade;
import websocket.FindWebsocket;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Livia
 */
@ManagedBean(name = "Household")
@SessionScoped
public class HouseholdController implements Serializable {
  //
  @PersistenceContext(unitName = "DomacnostPU")
  private EntityManager em;

  @Inject
  ApplicationManagers manager;
  private User user;
  private User newUser;
  private List<Household> householders;
  private int userId;
  private String name = "";
  private Household selectedHousehold;
  private String message;

  private Date startDate;
  private Date endDate;
  private int length;
  private Date startDateEvent;
  private Date eventTime;
  private int lengthEvent;
  private String eventName;
  private boolean waitEvent = false;

  @Resource(lookup = "jms/topicfactory")
  private TopicConnectionFactory connectionFactory;

  @Resource(lookup = "jms/topic")
  private Topic topic;
  JMSContext context;
  FindWebsocket wsRef;

  @EJB
  private HouseholdFacade hf;
  @EJB
  private UserFacade uf;
  @EJB
  private UserInHouseholdFacade uhf;
  //
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");

  public HouseholdController() {
  }

  @PostConstruct
  private void init() {
    java.util.Calendar c = java.util.Calendar.getInstance();
    userId = SessionUtils.getUserId();
    user = uf.getUserById(userId);
    selectedHousehold = null;
    householders = user.getHouseholdCollection();
    householders.addAll(user.getUserInHouseholdCollection().stream().map(uih -> uih.getHousehold()).collect(Collectors.toList()));
    context = connectionFactory.createContext();
  }

  public List<Household> getHouseholders() {
    return householders;
  }

  public void setHouseholders(List<Household> householders) {
    this.householders = householders;
  }

  public ApplicationManagers getManager() {
    return manager;
  }

  public void setManager(ApplicationManagers manager) {
    this.manager = manager;
  }

  public HouseholdFacade getHf() {
    return hf;
  }

  public UserFacade getUf() {
    return uf;
  }

  public UserInHouseholdFacade getUhf() {
    return uhf;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public User getNewUser() {
    return newUser;
  }

  public void setNewUser(User newUser) {
    this.newUser = newUser;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Household getSelectedHousehold() {
    selectedHousehold = manager.getSelectHousehold(userId);
    return selectedHousehold;
  }

  public void setSelectedHousehold(Household selectedHousehold) {
    this.selectedHousehold = selectedHousehold;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public Date getStartDateEvent() {
    return startDateEvent;
  }

  public void setStartDateEvent(Date startDateEvent) {
    this.startDateEvent = startDateEvent;
  }

  public Date getEventTime() {
    return eventTime;
  }

  public void setEventTime(Date eventTime) {
    this.eventTime = eventTime;
  }

  public int getLengthEvent() {
    return lengthEvent;
  }

  public void setLengthEvent(int lengthEvent) {
    this.lengthEvent = lengthEvent;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  public boolean isWaitEvent() {
    return waitEvent;
  }

  public void setWaitEvent(boolean waitEvent) {
    this.waitEvent = waitEvent;
  }

  public List<User> getAllUsers() {
    List<User> result = uf.getAllUsers();
    result.removeAll(selectedHousehold.getUserInHouseholdCollection().stream().map(uih -> uih.getUser()).collect(Collectors.toList()));
    result.remove(selectedHousehold.getAdmin());
    return result;
  }

  public void addHousehold() {
    setMessage("");
    User user = uf.getUserById(userId);
    Household newHousehold = new Household(name, user);
    newHousehold = hf.addHousehold(newHousehold);
    setName("");
    householders.add(newHousehold);
    setHouseholders(householders);
  }

  public void deleteHousehold(Household household) {
    try {
      hf.deleteHousehold(household);
      householders.remove(household);
    } catch (Exception e) {
      setMessage("Unable to delete the selected household");
    }
  }

  public void selectHousehold(Household h) {
    setMessage("");
    if (manager.setHousehold(userId, h)) {
      selectedHousehold = h;
    }
  }

  public void addUserInHousehold() {
    if (newUser != null) {
      UserInHousehold uih = new UserInHousehold(newUser, selectedHousehold);
      uhf.addUserInHousehold(uih);
      selectedHousehold.addUser(uih);
      setMessage("");
    } else {
      setMessage("User does not exist.");
    }
  }

  public void deleteUserFromHousehold(UserInHousehold householdUser) {
    uhf.deleteUserFromHousehold(householdUser);
    selectedHousehold.deleteUser(householdUser);
  }

//userId##freeTime#users:#id users in householder;startDate;endDate;length
  public void findFreeTime() {
    if (!waitEvent && selectedHousehold != null && startDate != null && endDate != null) {
      try {
        String msg = user.getId() + "#freeTime#users:";
        for (UserInHousehold uih : selectedHousehold.getUserInHouseholdCollection()) {
          msg += uih.getUser().getId();
          msg += ",";
        }
        msg += selectedHousehold.getAdmin().getId() + ";";
        startDate.setTime(startDate.getTime() - 3600000);
        endDate.setTime(endDate.getTime() - 3600000);
        msg += sdf.format(startDate) + ";";
        msg += sdf.format(endDate) + ";";
        msg += length;
        sendTask(msg);
      } catch (JMSException ex) {
        message = ex.getLocalizedMessage();
        return;
      }
      message = "Task submitted. Waiting for evaluation.";
      waitEvent = true;
      startDateEvent = null;
      eventTime = null;
      eventName = "";
      lengthEvent = 0;
    } else {
      //message = "Select householder";
    }
  }

//userId#event#id users in householder;name;startDate;start;length
  public void createJointEvent() {
    if (waitEvent && selectedHousehold != null && startDateEvent != null && eventTime != null) {
      try {
        String msg = userId + "#event#";
        for (UserInHousehold uih : selectedHousehold.getUserInHouseholdCollection()) {
          msg += uih.getUser().getId();
          msg += ",";
        }
        eventTime.setTime(eventTime.getTime() - 3600000);
        msg += selectedHousehold.getAdmin().getId() + ";";
        msg += eventName + ";";
        msg += sdf.format(startDateEvent) + ";";
        msg += sdf.format(eventTime) + ";";
        msg += lengthEvent + ";";
        sendTask(msg);
      } catch (JMSException ex) {
        message = ex.getLocalizedMessage();
        return;
      }
      startDate = null;
      endDate = null;
      length = 0;
      message = "Task submitted. Waiting for evaluation.";
      waitEvent = false;
    } else {
      message = "";
    }
  }

  private void sendTask(String task) throws JMSException {
    Message msg = context.createTextMessage(task);
    msg.setStringProperty("Direction", "ToServer");
    context.createProducer().send(topic, msg);
  }
}
