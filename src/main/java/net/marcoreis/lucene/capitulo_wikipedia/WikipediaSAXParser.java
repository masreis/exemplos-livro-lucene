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
    private Map<String, String> pagina;
    private StringBuilder content = new StringBuilder();
    private IndexadorWikipedia indexador = new IndexadorWikipedia(
	    System.getProperty("user.home") + "/dados/indice-wikipedia");
    private int paginasIndexadas;

    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if (qName.equals("page")) {
	    pagina = new HashMap<String, String>();
	    content.setLength(0);
	} else if (qName.equals("title")) {
	    content.setLength(0);
	} else if (qName.equals("timestamp")) {
	    content.setLength(0);
	} else if (qName.equals("username")) {
	    content.setLength(0);
	} else if (qName.equals("minor")) {
	    content.setLength(0);
	} else if (qName.equals("text")) {
	    content.setLength(0);
	} else if (qName.equals("model")) {
	    content.setLength(0);
	} else if (qName.equals("format")) {
	    content.setLength(0);
	} else if (qName.equals("comment")) {
	    content.setLength(0);
	} else if (qName.equals("id")) {
	    content.setLength(0);
	}
    }

    public void characters(char[] ch, int start, int length)
	    throws SAXException {
	if (pagina != null) {
	    content.append(String.copyValueOf(ch, start, length).trim());
	}
    }

    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if (qName.equals("page")) {
	    indexador.indexar(pagina);
	    paginasIndexadas++;
	    pagina = null;
	    if (paginasIndexadas > 0 && paginasIndexadas % 10000 == 0) {
		logger.info("Parcial: " + paginasIndexadas);
	    }
	} else if (qName.equals("title")) {
	    pagina.put("title", content.toString());
	} else if (qName.equals("timestamp")) {
	    pagina.put("timestamp", content.toString());
	} else if (qName.equals("username")) {
	    pagina.put("username", content.toString());
	} else if (qName.equals("minor")) {
	    pagina.put("minor", content.toString());
	} else if (qName.equals("text")) {
	    pagina.put("text", content.toString());
	} else if (qName.equals("model")) {
	    pagina.put("model", content.toString());
	} else if (qName.equals("format")) {
	    pagina.put("format", content.toString());
	} else if (qName.equals("comment")) {
	    pagina.put("comment", content.toString());
	} else if (qName.equals("id")) {
	    pagina.put("id", content.toString());
	}
    }

    public static void main(String[] args) {
	new WikipediaSAXParser().parse();
    }

    public void parse() {
	try {
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setNamespaceAware(true);
	    SAXParser parser = factory.newSAXParser();
	    parser.parse(new File(nomeArquivo), this);
	    indexador.commit();
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
