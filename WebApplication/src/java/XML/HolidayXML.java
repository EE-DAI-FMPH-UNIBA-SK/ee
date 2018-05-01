package XML;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Livia
 */
public class HolidayXML {
  //

  private static String HOLIDAY = "sviatok";
  static final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdfIn = new SimpleDateFormat("dd.MM.");
  private Set<JsonObjectBuilder> holidaysList = new HashSet<>();
  private boolean readEvent;
  private boolean readName;
  private boolean readDate;
  private boolean readLength;

  //parsovanie XML pomocou StAX metody
  public Set<JsonObjectBuilder> getHolidays() {

    try {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      InputStream is = HolidayXML.class.getResourceAsStream("/udalosti.xml");
      XMLEventReader eventReader = factory.createXMLEventReader(is);

      JsonObjectBuilder jsonObject = Json.createObjectBuilder();
      Date date = new Date();

      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();

        switch (event.getEventType()) {

          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            String name = startElement.getName().getLocalPart();

            if (name.equalsIgnoreCase("udalost")) {
              Iterator<Attribute> attributes = startElement.getAttributes();
              String type = attributes.next().getValue();
              if (type.equals(HOLIDAY)) {
                readEvent = true;
                jsonObject = Json.createObjectBuilder();
              }
            } else if (readEvent && name.equalsIgnoreCase("nazov")) {
              readName = true;
            } else if (readEvent && name.equalsIgnoreCase("datum")) {
              readDate = true;
            } else if (readEvent && name.equalsIgnoreCase("dlzka")) {
              readLength = true;
            }
            break;

          case XMLStreamConstants.CHARACTERS:
            Characters characters = event.asCharacters();
            if (readEvent && readName) {
              jsonObject.add("title", characters.getData());
              readName = false;
            }
            if (readEvent && readDate) {
              try {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                cal.setTime(sdfIn.parse(characters.getData()));
                cal.set(Calendar.YEAR, year);
                date = cal.getTime();
                jsonObject.add("start", sdfOut.format(date));
              } catch (Exception e) {
              }
              readDate = false;
            }
            if (readEvent && readLength) {
              Calendar cal = Calendar.getInstance();
              cal.setTime(date);
              int length = Integer.valueOf(characters.getData());
              if (length != 24) {
                cal.add(Calendar.HOUR, Integer.valueOf(length));
                jsonObject.add("end", sdfOut.format(cal.getTime()));
              } else {
                jsonObject.add("allDay", true);
              }
              readLength = false;
            }
            break;

          case XMLStreamConstants.END_ELEMENT:

            if (readEvent && event.asEndElement().getName().getLocalPart().equalsIgnoreCase("udalost")) {
              holidaysList.add(jsonObject);
              readEvent = false;
            }
            break;
        }
      }
    } catch (XMLStreamException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }
    System.out.println(holidaysList.size());
    return holidaysList;
  }

}
