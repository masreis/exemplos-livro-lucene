package net.marcoreis.lucene.capitulo_wikipedia;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikipediaSAXParserToJDBC extends DefaultHandler {
    private static String nomeArquivo = System.getProperty("user.home")
	    + "/dados/ptwiki-20130817-pages-articles-multistream.xml";
    private static Logger logger = Logger
	    .getLogger(WikipediaSAXParserToJDBC.class);
    private PaginaWikipedia pagina;
    private StringBuilder content = new StringBuilder();
    private SimpleDateFormat sdf = new SimpleDateFormat(
	    "yyyy-MM-dd'T'HH:mm:ss'Z'");
    private int paginasIndexadas;
    private DAOPaginaWikipedia dao = new DAOPaginaWikipedia();

    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {
	if (qName.equals("page")) {
	    pagina = new PaginaWikipedia();
	    content.setLength(0);
	} else if (qName.equals("title")) {
	    content.setLength(0);
	} else if (qName.equals("timestamp")) {
	    content.setLength(0);
	} else if (qName.equals("username")) {
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
	content.append(String.copyValueOf(ch, start, length).trim());
    }

    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if (qName.equals("page")) {
	    dao.inserir(pagina);
	    paginasIndexadas++;
	    // pagina = null;
	    logAndamento();
	} else if (qName.equals("title")) {
	    pagina.setTitle(content.toString());
	} else if (qName.equals("timestamp")) {
	    try {
		Date timestamp = sdf.parse(content.toString());
		pagina.setTimeStamp(timestamp);
	    } catch (ParseException e) {
		logger.error(e);
	    }
	} else if (qName.equals("username")) {
	    pagina.setUserName(content.toString());
	} else if (qName.equals("text")) {
	    pagina.setText(content.toString());
	} else if (qName.equals("model")) {
	    pagina.setModel(content.toString());
	} else if (qName.equals("format")) {
	    pagina.setFormat(content.toString());
	} else if (qName.equals("comment")) {
	    pagina.setComment(content.toString());
	} else if (qName.equals("id")) {
	    if (pagina.getId() == null)
		pagina.setId(new Long(content.toString()));
	}
    }

    private void logAndamento() {
	if (paginasIndexadas % 10000 == 0) {
	    logger.info("Parcial: " + paginasIndexadas);
	}
    }

    public static void main(String[] args) {
	new WikipediaSAXParserToJDBC().parse();
    }

    public void parse() {
	try {
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setNamespaceAware(true);
	    SAXParser parser = factory.newSAXParser();
	    parser.parse(new File(nomeArquivo), this);
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
