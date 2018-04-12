package websocket;

import jsf.ApplicationManagers;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

/**
 *
 * @author Livia
 */
@javax.websocket.server.ServerEndpoint("/compute")
public class FindWebsocket {
  private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
  //
  @Inject
  ApplicationManagers manager;

  Session mySession;

  @OnOpen
  public void open(Session session, EndpointConfig conf) {
    mySession = session;
  }

  @OnClose
  public void close(Session session, CloseReason reason) {
//    manager.removeWsRef(this);
  }

  @OnMessage
  public void onMessage(String text, Session session) {
    String[] t = text.split(";");
    if (t[0].equals("userId")) {
      manager.addUserIdtoWs(Integer.valueOf(t[1]), this);
    }
  }

  public void reply(String text) {
    try {
      mySession.getBasicRemote().sendText(text);
    } catch (IOException ex) {
      Logger.getLogger(FindWebsocket.class.getName()).log(Level.SEVERE, null, ex);
    }

  }

}
