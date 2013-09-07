package net.marcoreis.lucene.capitulo_wikipedia;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikipediaSAXParser extends DefaultHandler {
  private static String nomeArquivo = System.getProperty("user.home")
      + "/dados/ptwiki-20130817-pages-articles-multistream.xml";
  private static Logger logger = Logger.getLogger(WikipediaSAXParser.class);
  //private PaginaWikipedia pagina;
  //  private Collection<PaginaWikipedia> paginas = new ArrayList<PaginaWikipedia>();
  private Map<String, String> pagina;
  private String content;
  private IndexadorWikipedia indexador = new IndexadorWikipedia(
      System.getProperty("user.home") + "/dados/indice-wikipedia");
  private int paginasIndexadas;

  public void startElement(String uri, String localName, String qName,
      Attributes attributes) throws SAXException {
    switch (qName) {
      case "page":
        //pagina = new PaginaWikipedia();
        pagina = new HashMap<String, String>();
        break;
    }
  }

  public void characters(char[] ch, int start, int length) throws SAXException {
    if (pagina != null) content = String.copyValueOf(ch, start, length).trim();
  }

  public void endElement(String uri, String localName, String qName)
      throws SAXException {
    switch (qName) {
      case "page":
        //paginas.add(pagina);
        indexador.indexar(pagina);
        paginasIndexadas++;
        pagina = null;
        break;
      case "title":
        pagina.put("title", content);
        break;
      case "timestamp":
        pagina.put("timestamp", content);
        break;
      case "username":
        pagina.put("username", content);
        break;
      case "minor":
        pagina.put("minor", content);
        break;
      case "text":
        pagina.put("text", content);
        break;
      case "model":
        pagina.put("model", content);
        break;
      case "format":
        pagina.put("format", content);
        break;
      case "comment":
        pagina.put("comment", content);
    }
    //
    if (paginasIndexadas % 10000 == 0) {
      logger.info("Parcial: " + paginasIndexadas);
    }
  }

  public static void main(String[] args) {
    new WikipediaSAXParser().parse();
  }

  public void parse() {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser parser = factory.newSAXParser();
      parser.parse(new File(nomeArquivo), this);
      indexador.commit();
    } catch (Exception e) {
      logger.error(e);
    }
  }
}
