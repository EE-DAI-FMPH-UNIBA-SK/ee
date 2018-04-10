package jms;

import jsf.ApplicationManagers;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author MaX
 */
@MessageDriven(activationConfig = {
  @ActivationConfigProperty(propertyName = "clientId", propertyValue = "jms/topic"),
  @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/topic"),
  @ActivationConfigProperty(propertyName = "subscriptionName", propertyValue = "jms/topic"),
  @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
  @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "Direction = 'ToClient'")
})
public class ResultReader implements MessageListener {
  //

  @Inject
  ApplicationManagers appBean;

  @Override
  public void onMessage(Message message) {
    try {
      String[] result = message.getBody(String.class).split("#");
      appBean.reply(Integer.valueOf(result[0]), result[1]);
    } catch (JMSException ex) {
      Logger.getLogger(ResultReader.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
