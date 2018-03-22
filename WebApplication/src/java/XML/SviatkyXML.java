package XML;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
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

import org.json.simple.JSONObject;

/**
 *
 * @author Livia
 */
public class SviatkyXML {
  //

  private static String SVIATOK = "sviatok";
  static final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdfIn = new SimpleDateFormat("dd.MM.");
  private Set<JSONObject> meninyList = new HashSet<>();
  private boolean citamUdalost;
  private boolean spracuvamMeno;
  private boolean spracuvamDatum;
  private boolean spracuvamDlzku;

  //parsovanie XML pomocou StAX metody
  public Collection<JSONObject> getSviatky() {

    try {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      InputStream is = SviatkyXML.class.getResourceAsStream("/udalosti.xml");
      XMLEventReader eventReader = factory.createXMLEventReader(is);
      JSONObject udalost = new JSONObject();
      Date datum = new Date();

      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();

        switch (event.getEventType()) {

          case XMLStreamConstants.START_ELEMENT:
            StartElement startElement = event.asStartElement();
            String name = startElement.getName().getLocalPart();

            if (name.equalsIgnoreCase("udalost")) {
              Iterator<Attribute> attributes = startElement.getAttributes();
              String type = attributes.next().getValue();
              if (type.equals(SVIATOK)) {
                citamUdalost = true;
                udalost = new JSONObject();
              }
            } else if (citamUdalost && name.equalsIgnoreCase("nazov")) {
              spracuvamMeno = true;
            } else if (citamUdalost && name.equalsIgnoreCase("datum")) {
              spracuvamDatum = true;
            } else if (citamUdalost && name.equalsIgnoreCase("dlzka")) {
              spracuvamDlzku = true;
            }
            break;

          case XMLStreamConstants.CHARACTERS:
            Characters characters = event.asCharacters();
            if (citamUdalost && spracuvamMeno) {
              udalost.put("title", characters.getData());
              spracuvamMeno = false;
            }
            if (citamUdalost && spracuvamDatum) {
              try {
                String pom = characters.getData();
                Calendar cal = Calendar.getInstance();
                int rok = cal.get(Calendar.YEAR);
                cal.setTime(sdfIn.parse(pom));
                cal.set(Calendar.YEAR, rok);
                datum = cal.getTime();
//                udalost.put("start", sdfOut.format(datum));
              } catch (Exception e) {
              }
              spracuvamDatum = false;
            }
            if (citamUdalost && spracuvamDlzku) {
              Calendar cal = Calendar.getInstance();
              cal.setTime(datum);
              int length = Integer.valueOf(characters.getData());
              if (length != 24) {
                cal.add(Calendar.HOUR, Integer.valueOf(length));
                udalost.put("end", sdfOut.format(cal.getTime()));
              } else {
                udalost.put("allDay", true);
              }
              spracuvamDlzku = false;
            }
            break;

          case XMLStreamConstants.END_ELEMENT:

            if (citamUdalost && event.asEndElement().getName().getLocalPart().equalsIgnoreCase("udalost")) {
              meninyList.add(udalost);
              citamUdalost = false;
            }
            break;
        }
      }
    } catch (XMLStreamException ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace();
    }

    return meninyList;
  }

}
