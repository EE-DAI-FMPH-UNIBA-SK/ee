package XML;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Livia
 */
public class MeninyXML extends DefaultHandler {
  //
  private Set<JSONObject> meninyList = new HashSet<>();
  private boolean citamUdalost;
  private boolean spracuvamMeno;
  private boolean spracuvamDatum;
  private boolean spracuvamDlzku;
  private StringBuffer meno = new StringBuffer(50);
  private StringBuffer datum = new StringBuffer(10);
  private StringBuffer dlzka = new StringBuffer(5);
  private static String MENINY = "meniny";
  static final SimpleDateFormat sdfOut = new SimpleDateFormat("yyyy-MM-dd");
  static final SimpleDateFormat sdfIn = new SimpleDateFormat("dd.MM.");
  private JSONObject udalost = new JSONObject();

  //parsovanie XML pomocou SAX metody
  public Collection<JSONObject> getMeniny() {
    return meninyList;
  }

  @Override
  public void startDocument() {
    meninyList = new HashSet<>();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts) {
    if (qName.equals("udalost")) {
      if (atts.getValue("type").equals(MENINY)) {
        citamUdalost = true;
        udalost = new JSONObject();
      }
    } else if (qName.equals("meno") == true && citamUdalost) {
      spracuvamMeno = true;
      meno.setLength(0);
    } else if (qName.equals("datum") == true && citamUdalost) {
      spracuvamDatum = true;
      datum.setLength(0);
    } else if (qName.equals("dlzka") == true && citamUdalost) {
      spracuvamDlzku = true;
      dlzka.setLength(0);
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    if (qName.equals("udalost") && citamUdalost) {
      citamUdalost = false;
      udalost.put("title", meno.toString());
      Calendar cal = Calendar.getInstance();
      int rok = cal.get(Calendar.YEAR);
      try {
        cal.setTime(sdfIn.parse(datum.toString()));
        cal.set(Calendar.YEAR, rok);
        udalost.put("start", sdfOut.format(cal.getTime()));
      } catch (ParseException ex) {
        Logger.getLogger(MeninyXML.class.getName()).log(Level.SEVERE, null, ex);
      }
      int length = Integer.valueOf(dlzka.toString());
      if (length != 24) {
        cal.add(Calendar.HOUR, length);
        udalost.put("end", sdfOut.format(cal.getTime()));
      } else {
        udalost.put("allDay", true);
      }
      meninyList.add(udalost);
    } else if (qName.equals("meno") == true && citamUdalost) {
      spracuvamMeno = false;
    } else if (qName.equals("datum") == true && citamUdalost) {
      spracuvamDatum = false;
    } else if (qName.equals("dlzka") == true && citamUdalost) {
      spracuvamDlzku = false;
    }
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    if (spracuvamMeno == true) {
      meno.append(ch, start, length);
    } else if (spracuvamDatum == true) {
      datum.append(ch, start, length);
    } else if (spracuvamDlzku == true) {
      dlzka.append(ch, start, length);
    }
  }
}
