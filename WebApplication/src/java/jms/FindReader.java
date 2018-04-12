package jms;

import com.controllers.ApplicationManager;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Livia
 */
@MessageDriven(activationConfig = {
  @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/topic"),
  @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/topic"),
  @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/topic"),
  @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
  @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "Direction = 'ToServer'")
})
public class FindReader implements MessageListener {
  @Inject
  ApplicationManager manager;

  @Override
  public void onMessage(Message message) {
    System.err.println("-------------------------------------------------");
    System.err.println(message);
    try {
      String[] comp = message.getBody(String.class).split("#");
      if (comp[1].equals("freeTime")) {
        System.err.println(comp[2]);
        manager.findFreeTime(comp[2], comp[0]);
      } else if (comp[1].equals("event")) {
        System.err.println(comp[2]);
        manager.addJointEvent(comp[2], comp[0]);
      }
    } catch (JMSException ex) {
      ex.printStackTrace();
      System.err.println(ex.getMessage());
    }
  }

}
