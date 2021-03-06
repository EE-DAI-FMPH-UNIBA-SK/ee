package jms;

import jsf.ApplicationManager;

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
  @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "Direction = 'ToClient'")
})
public class ResultReader implements MessageListener {
  //

  @Inject
  ApplicationManager appBean;

  @Override
  public void onMessage(Message message) {
    try {
      String[] result = message.getBody(String.class).split("#");
      appBean.reply(Integer.valueOf(result[0]), result[1]);
    } catch (JMSException ex) {
      System.out.println(ex.getMessage());
      ex.printStackTrace();
    }
  }
}
