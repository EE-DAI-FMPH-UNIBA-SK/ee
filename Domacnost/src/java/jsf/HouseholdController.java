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
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
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

  @ManagedProperty(value = "#{applicationManagers}")
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
  private List<List<Date>> freeTimes;

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

  public List<List<Date>> getFreeTimes() {
    return freeTimes;
  }

  public void setFreeTimes(List<List<Date>> freeTimes) {
    this.freeTimes = freeTimes;
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

  public void setWebsocketReference(FindWebsocket ws) {
    wsRef = ws;
    if (userId != 0) {
      manager.addUserIdtoWs(userId, ws);
    }
  }

  public void removeWebsocketReference(FindWebsocket aThis) {
    manager.removeWsRef(wsRef);
  }

  public void getFindTime() {
    try {
      String msg = user + "#users:";
      for (UserInHousehold uih : selectedHousehold.getUserInHouseholdCollection()) {
        msg += uih.getUser().getId();
        msg += ",";
      }
      msg += selectedHousehold.getAdmin().getId() + ";";
      msg += sdf.format(startDate) + ";";
      msg += sdf.format(endDate) + ";";
      msg += length;
      sendTask(msg);
    } catch (JMSException ex) {
      message = ex.getLocalizedMessage();
      return;
    }
    message = "Task submitted. Waiting for evaluation.";
  }

  private void sendTask(String task) throws JMSException {
    Message msg = context.createTextMessage(task);
    msg.setStringProperty("Direction", "ToServer");
    context.createProducer().send(topic, msg);
  }
}
