package XML;

import com.entity.Calendar;

import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Livia
 */
public class CalendarsXML {
  //
  private List<Calendar> calendars;

  public CalendarsXML(List<Calendar> cal) {
    calendars = cal;
  }

  public String exportXMLData(int id) {
    try {
      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

      // root elements
      Document doc = docBuilder.newDocument();
      Element rootElement = doc.createElement("timetable");
      rootElement.setAttribute("version", "0.1");
      rootElement.setAttribute("name", "calendars");
      doc.appendChild(rootElement);
      Element calendarsElement = doc.createElement("calendars");

//      if (calendars == null || calendars.isEmpty()) {
//        calendars = DataQuery.getInstance().getCalendars();
//      }
//      calendars = calendars.stream().filter(cal -> cal.getUser().getId() == id).collect(Collectors.toList());
      for (Calendar c : calendars) {
        Element calendar = doc.createElement("calendar");

        //set id attribute
        calendar.setAttribute("id", c.getId().toString());

        //add child
        Element node = doc.createElement("name");
        node.appendChild(doc.createTextNode(c.getName()));
        calendar.appendChild(node);

        calendarsElement.appendChild(calendar);
      }
      rootElement.appendChild(calendarsElement);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      StringWriter writer = new StringWriter();
      transformer.transform(new DOMSource(doc), new StreamResult(writer));

      String result = writer.getBuffer().toString().replaceAll("\n|\r", "");
      System.out.println(result);
      return result;

    } catch (ParserConfigurationException pce) {
      pce.printStackTrace();
    } catch (TransformerException tfe) {
      tfe.printStackTrace();
    }
    return "";
  }
}
