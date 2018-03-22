package JSON;

import XML.MeninyXML;
import XML.SviatkyXML;
import com.entity.Calendars;
import com.entity.EventList;
import com.entity.Events;
import com.query.DataQuery;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Livia
 */
public class CallendarJSON {

  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
  static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

  public String getEvents(int calendarId) {
    JSONObject obj = new JSONObject();
    Calendars calendar = DataQuery.getInstance().getCalendarById(calendarId);
    JSONArray events = new JSONArray();
    if (calendar != null) {
      for (EventList el : calendar.getEventListCollection()) {
        Events e = el.getEventId();
        if (e != null) {
          JSONObject event = new JSONObject();
          event.put("title", e.getName());
          Calendar cal = Calendar.getInstance();
          cal.setTime(e.getStartDate());
          cal.add(Calendar.MILLISECOND, ((int) e.getStart().getTime() + 3600000));
          event.put("start", sdf2.format(cal.getTime()));
          cal.add(Calendar.HOUR, e.getLength());
          cal.add(Calendar.MINUTE, -1);
          event.put("end", sdf2.format(cal.getTime()));
          JSONArray dow = new JSONArray();
          for (String i : e.getIter().split(",")) {
            dow.add(Integer.valueOf(i));
          }
          event.put("dow", dow);
          if (dow != null) {
            JSONObject ranges = new JSONObject();
            ranges.put("start", sdf1.format(e.getStartDate()));
            ranges.put("end", sdf1.format(e.getEndDate()));
            event.put("ranges", ranges);
          }
          events.add(event);
        }
      }
    }
    events.addAll(getMeniny());

    events.addAll(getSviatky());

    obj.put("events", events);

    return obj.toJSONString();

  }

  private Collection<JSONObject> getMeniny() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();

      MeninyXML meniny = new MeninyXML();
      InputStream is = SviatkyXML.class.getResourceAsStream("/udalosti.xml");
      saxParser.parse(is, meniny);

      return meniny.getMeniny();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return Collections.EMPTY_SET;
  }

  private Collection<JSONObject> getSviatky() {
    SviatkyXML sviatky = new SviatkyXML();
    return sviatky.getSviatky();
  }

}
