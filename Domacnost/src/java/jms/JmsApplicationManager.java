package jms;

import websocket.FindWebsocket;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.Stateless;

/**
 *
 * @author Livia
 */
@Stateless
public class JmsApplicationManager {
  //

  private Map<Integer, FindWebsocket> userToWs;

  public JmsApplicationManager() {
    userToWs = new HashMap<>();
  }

  public void addUserIdtoWs(int userId, FindWebsocket wsRef) {
    userToWs.put(userId, wsRef);
  }

  public void removeWsRef(FindWebsocket wsRef) {
    for (Map.Entry<Integer, FindWebsocket> e : userToWs.entrySet()) {
      if (e.getValue() == wsRef) {
        userToWs.remove(e.getKey());
        break;
      }
    }
  }

  public void reply(int userId, String result) {
    FindWebsocket ws = userToWs.get(userId);
    if (ws != null) {
      ws.reply(result);
    }
  }
}
