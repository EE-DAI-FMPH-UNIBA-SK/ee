package websocket;

import com.controllers.CalendarController;

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
  CalendarController calendar;

  //protokol komunikacie
  // client -> server
  //  "calendars" -> daj mi kalendare pre pouzivatela
  //  "events" -> daj mi eventy pre show calendar
  //  "addCalendar;name:xx;visible:yy" -> pridaj novy kalendar s menom xx a visible yy pre
  //  "importJson;fileName" -> importni kalendar a jeho eventy zo suboru s nazvom fileName
  //  "importXML;fileName" -> importni eventy zo suboru s nazvom fileName
  //
  // server -> client
  //  "calendars;xml" -> kalendare pouzivatela
  //  "calendar;id" -> pridany kalendar
  //  "calendarEvents;JSON" -> eventy pre calendar
  //  "editEvent;" -> zmena eventu
  //  "importJson" -> importnute data z JSON
  //  "importXML" -> importnute data z XML
  @OnOpen
  public void open(Session session, EndpointConfig conf) {
    mySession = session;
    final RemoteEndpoint.Basic remote = session.getBasicRemote();
  }

  @OnMessage
  public void onMessage(String text, Session session) {
    final RemoteEndpoint.Basic remote = session.getBasicRemote();
    String[] t = text.split(";");
    if (t[0].equals("events")) {
      try {
        String pom = calendar.getEvents(Integer.valueOf(t[1]));
        remote.sendText("calendarEvents;" + pom);
      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("calendars")) {
      try {
        remote.sendText("calendars;" + calendar.getShowCalendars());
      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("addCalendar")) {
      try {
        String name = t[1].split(":")[1];
        boolean visible = Boolean.valueOf(t[2].split(":")[1]);
        remote.sendText("calendar;" + calendar.addCalendar(name, visible));

      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("importXML")) {
      try {
        String name = t[1];
        calendar.importXmlData(name);
        remote.sendText("importXML");

      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("importJson")) {
      try {
        String name = t[1];
        calendar.importJsonData(name);
        remote.sendText("importJson");

      } catch (IOException ioe) {
        System.err.println(ioe.getMessage());
      }
    } else if (t[0].equals("refresh")) {
      try {
        if (calendar.getShowCalendar() != null) {
          remote.sendText("calendarEvents;" + calendar.getEvents(calendar.getShowCalendar().getId()));
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
