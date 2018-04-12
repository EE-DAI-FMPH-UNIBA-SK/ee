package JSON;

import XML.HolidayXML;
import XML.NamesXML;
import com.entity.Calendar;
import com.entity.Event;
import com.entity.EventInCalendar;
import com.query.DataQuery;

import java.io.InputStream;
import java.text.SimpleDateFormat;
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
    Calendar calendar = DataQuery.getInstance().getCalendarById(calendarId);
    JSONArray events = new JSONArray();
    if (calendar != null) {
      for (EventInCalendar el : calendar.getEventincalendarCollection()) {
        Event e = el.getEvent();
        if (e != null) {
          JSONObject event = new JSONObject();
          event.put("title", e.getName());
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(e.getStartDate());
          cal.add(java.util.Calendar.MILLISECOND, ((int) e.getStart().getTime() + 3600000));
          event.put("start", sdf2.format(cal.getTime()));
          cal.add(java.util.Calendar.HOUR, e.getLength());
          cal.add(java.util.Calendar.MINUTE, -1);
          event.put("end", sdf2.format(cal.getTime()));
          JSONArray dow = new JSONArray();
          if (!e.getIter().equals("")) {
            for (String i : e.getIter().split(",")) {
              dow.add(Integer.valueOf(i));
            }
            event.put("dow", dow);
          }
          JSONArray ranges = new JSONArray();
          JSONObject range = new JSONObject();
          range.put("start", sdf1.format(e.getStartDate()));
          range.put("end", sdf1.format(e.getEndDate()));
          ranges.add(range);
          event.put("ranges", ranges);
          events.add(event);
        }
      }
    }
    events.addAll(getNames());

    events.addAll(getHolidays());

    obj.put("events", events);

    return obj.toJSONString();

  }

  private Collection<JSONObject> getNames() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();

      NamesXML names = new NamesXML();
      InputStream is = CallendarJSON.class.getResourceAsStream("/udalosti.xml");
      saxParser.parse(is, names);

      return names.getNames();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return Collections.EMPTY_SET;
  }

  private Collection<JSONObject> getHolidays() {
    HolidayXML holidays = new HolidayXML();
    return holidays.getHolidays();
  }

}
