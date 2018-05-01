package XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Livia
 */
public class NamesXML extends DefaultHandler {
  //
  private Set<JsonObjectBuilder> namesList = new HashSet<>();
  private boolean readEvent;
  private boolean readName;
  private boolean readDate;
  private boolean readLength;
  private StringBuffer name = new StringBuffer(50);
  private StringBuffer date = new StringBuffer(10);
  private StringBuffer length = new StringBuffer(5);
  private static String NAME = "meniny";
  static final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdfIn = new SimpleDateFormat("dd.MM.");
  private JsonObjectBuilder jsonObject = Json.createObjectBuilder();

  //parsovanie XML pomocou SAX metody
  public Set<JsonObjectBuilder> getNames() {
    System.out.println(namesList.size());
    return namesList;
  }

  @Override
  public void startDocument() {
    namesList = new HashSet<>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) {
    if (qName.equals("udalost")) {
      if (atts.getValue("type").equals(NAME)) {
        readEvent = true;
        jsonObject = Json.createObjectBuilder();
      }
    } else if (qName.equals("meno") == true && readEvent) {
      readName = true;
      name.setLength(0);
    } else if (qName.equals("datum") == true && readEvent) {
      readDate = true;
      date.setLength(0);
    } else if (qName.equals("dlzka") == true && readEvent) {
      readLength = true;
      length.setLength(0);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("udalost") && readEvent) {
      readEvent = false;
      jsonObject.add("title", name.toString());
      Calendar cal = Calendar.getInstance();
      int rok = cal.get(Calendar.YEAR);
      try {
        cal.setTime(sdfIn.parse(date.toString()));
        cal.set(Calendar.YEAR, rok);
        jsonObject.add("start", sdfOut.format(cal.getTime()));
      } catch (ParseException ex) {
        Logger.getLogger(NamesXML.class.getName()).log(Level.SEVERE, null, ex);
      }
      jsonObject.add("allDay", true);
      namesList.add(jsonObject);
    } else if (qName.equals("meno") == true && readEvent) {
      readName = false;
    } else if (qName.equals("datum") == true && readEvent) {
      readDate = false;
    } else if (qName.equals("dlzka") == true && readEvent) {
      readLength = false;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (readName == true) {
      name.append(ch, start, length);
    } else if (readDate == true) {
      date.append(ch, start, length);
    } else if (readLength == true) {
      this.length.append(ch, start, length);
    }
  }
}
