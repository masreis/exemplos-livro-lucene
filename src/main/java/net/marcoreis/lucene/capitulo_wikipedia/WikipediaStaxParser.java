package net.marcoreis.lucene.capitulo_wikipedia;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

public class WikipediaStaxParser {
  private static String nomeArquivo = System.getProperty("user.home")
      + "/dados/ptwiki-20130417-stub-articles.xml";
  private static final String PAGE = "page";
  private static final String TITLE = "title";
  private static int paginas = 0;
  private static Logger logger = Logger.getLogger(WikipediaStaxParser.class);

  public List<PaginaWikipedia> readConfig(String configFile) {
    List<PaginaWikipedia> items = new ArrayList<PaginaWikipedia>();
    try {
      XMLInputFactory inputFactory = XMLInputFactory.newInstance();
      InputStream in = new FileInputStream(configFile);
      XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
      PaginaWikipedia item = null;
      while (eventReader.hasNext()) {
        XMLEvent event = eventReader.nextEvent();
        if (event.isStartElement()) {
          StartElement startElement = event.asStartElement();
          // If we have a item element we create a new item
          if (startElement.getName().getLocalPart() == (PAGE)) {
            paginas++;
            item = new PaginaWikipedia();
            Iterator<Attribute> attributes = startElement.getAttributes();
            while (attributes.hasNext()) {
              Attribute attribute = attributes.next();
              if (attribute.getName().toString().equals(TITLE)) {
                item.setTitle(attribute.getValue());
              }
            }
          }
        }
        if (event.isEndElement()) {
          EndElement endElement = event.asEndElement();
          if (endElement.getName().getLocalPart() == (PAGE)) {
            items.add(item);
          }
        }
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (XMLStreamException e) {
      e.printStackTrace();
    }
    logger.info("Paginas: " + paginas);
    return items;
  }

  public static void main(String[] args) {
    WikipediaStaxParser w = new WikipediaStaxParser();
    List<PaginaWikipedia> lista = w.readConfig(nomeArquivo);
    lista.size();
  }
}
