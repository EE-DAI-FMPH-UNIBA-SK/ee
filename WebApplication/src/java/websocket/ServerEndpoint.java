package websocket;

import com.controllers.ApplicationManager;
import com.entity.User;
import com.query.DataQuery;

import java.io.IOException;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;

/**
 *
 * @author Livia
 */
@javax.websocket.server.ServerEndpoint("/server")
public class ServerEndpoint {
  //
  Session mySession;
  @Inject
  ApplicationManager manager;

  //protokol komunikacie
  // client -> server
  //  "calendars;userId" -> daj mi kalendare pre pouzivatela s userId
  //  "events;calendarId;userId" -> daj mi eventy pre calendar s id
  //  "addCalendar;name:xx;visible:yy;userId" -> pridaj novy kalendar s menom xx a visible yy pre userId
  //  "importJson;fileName;userId" -> importni kalendar a jeho eventy zo suboru s nazvom fileName pre pouzivatela s id userId
  //  "importXML;fileName;userId" -> importni eventy zo suboru s nazvom fileName do vybraneho kalendara pouzivatela s id userId
  //
  // server -> client
  //  "calendars;xml" -> kalendare pouzivatela
  //  "calendar;id" -> pridany kalendar
  //  "calendarEvents;JSON" -> eventy pre calendar
  //  "importJson" -> importnute data z JSON
  //  "importXML" -> importnute data z XML
  @OnOpen
  public void open(Session session, EndpointConfig conf) {
    System.out.println("websocket.ServerEndpoint.open()");
    mySession = session;
    final RemoteEndpoint.Basic remote = session.getBasicRemote();
  }

  @OnMessage
  public void onMessage(String text, Session session) {
    System.err.println(text);
    final RemoteEndpoint.Basic remote = session.getBasicRemote();
    String[] t = text.split(";");
    if (t[0].equals("events")) {
      try {
        String pom = manager.getEvents(Integer.valueOf(t[1]));
        DataQuery.getInstance().getUserById(Integer.valueOf(t[2])).setSelectedCalendar(Integer.valueOf(t[1]));
        remote.sendText("calendarEvents;" + pom);
      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("calendars")) {
      try {
        remote.sendText("calendars;" + manager.getShowCalendars(Integer.valueOf(t[1])));
      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("addCalendar")) {
      try {
        String name = t[1].split(":")[1];
        boolean visible = Boolean.valueOf(t[2].split(":")[1]);
        remote.sendText("calendar;" + manager.addCalendar(name, visible, Integer.valueOf(t[3])));

      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("importXML")) {
      try {
        String name = t[1];
        manager.importXmlData(name, Integer.valueOf(t[2]));
        remote.sendText("importXML");

      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("importJson")) {
      try {
        String name = t[1];
        manager.importJsonData(name, Integer.valueOf(t[2]));
        remote.sendText("importJson");

      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("refresh")) {
      try {
        User u = DataQuery.getInstance().getUserById(Integer.valueOf(t[1]));
        if (u.getSelectedCalendar() != null) {
          remote.sendText("calendarEvents;" + manager.getEvents(u.getSelectedCalendar().getId()));
        }
      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    }
  }

  @OnClose
  public void onClose(Session session, CloseReason reason) throws IOException {
    //prepare the endpoint for closing.
  }

}
