package websocket;

import jsf.HouseholdController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
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
  HouseholdController manager;

  Session mySession;

  @OnOpen
  public void open(Session session, EndpointConfig conf) {
    manager.setWebsocketReference(this);
    mySession = session;
  }

  @OnClose
  public void close(Session session, CloseReason reason) {
    manager.removeWebsocketReference(this);
  }

  public void reply(String text) {
    String[] t = text.split(";");
    if (t[0].equals("freeTime")) {
      List<List<Date>> result = new ArrayList<>();
      for (int i = 1; i < t.length; i++) {
        try {
          String[] dates = t[i].split("-");
          Date start = sdf.parse(dates[0]);
          Date end = sdf.parse(dates[1]);
          List<Date> datesList = new ArrayList<>();
          datesList.add(start);
          datesList.add(end);
          result.add(datesList);
        } catch (ParseException ex) {
          Logger.getLogger(FindWebsocket.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      manager.setFreeTimes(result);
    }
  }

}
