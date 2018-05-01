package JSON;

import XML.HolidayXML;
import XML.NamesXML;
import com.entity.Calendar;
import com.entity.Event;
import com.entity.EventInCalendar;
import com.query.DataQuery;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author Livia
 */
public class CallendarJSON {

  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
  static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");

  public String getEvents(int calendarId) {
    JsonObjectBuilder obj = Json.createObjectBuilder();
    Calendar calendar = DataQuery.getInstance().getCalendarById(calendarId);
    JsonArrayBuilder events = Json.createArrayBuilder();
    if (calendar != null) {
      for (EventInCalendar el : calendar.getEventincalendarCollection()) {
        Event e = el.getEvent();
        if (e != null) {
          JsonObjectBuilder event = Json.createObjectBuilder();
          event.add("id", e.getId());
          event.add("title", e.getName());
          java.util.Calendar cal = java.util.Calendar.getInstance();
          cal.setTime(e.getStartDate());
          cal.add(java.util.Calendar.MILLISECOND, ((int) e.getStart().getTime() + 3600000));
          event.add("start", sdf2.format(cal.getTime()));
          cal.add(java.util.Calendar.HOUR, e.getLength());
          cal.add(java.util.Calendar.MINUTE, -1);
          event.add("end", sdf2.format(cal.getTime()));
          JsonArrayBuilder dow = Json.createArrayBuilder();
          if (!e.getIter().equals("")) {
            for (String i : e.getIter().split(",")) {
              dow.add(Integer.valueOf(i));
            }
            event.add("dow", dow);
          }
          JsonArrayBuilder ranges = Json.createArrayBuilder();
          JsonObjectBuilder range = Json.createObjectBuilder();
          range.add("start", sdf1.format(e.getStartDate()));
          range.add("end", sdf1.format(e.getEndDate()));
          ranges.add(range);
          event.add("ranges", ranges);
          events.add(event);
        }
      }
    }
    Set<JsonObjectBuilder> jobList = getNames();
    jobList.addAll(getHolidays());
    for (JsonObjectBuilder job : jobList) {
      events.add(job);
    }
    obj.add("events", events);

    return obj.build().toString();

  }

  private Set<JsonObjectBuilder> getNames() {
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

  private Set<JsonObjectBuilder> getHolidays() {
    HolidayXML holidays = new HolidayXML();
    return holidays.getHolidays();
  }

}
