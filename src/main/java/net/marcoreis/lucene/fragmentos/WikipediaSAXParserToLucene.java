package net.marcoreis.lucene.fragmentos;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WikipediaSAXParserToLucene extends DefaultHandler {
	public static final String DIRETORIO_INDICE_WIKIPEDIA =
			System.getProperty("user.home")
					+ "/livro-lucene/indice-wikipedia";
	private static final int BUFFER_DOCUMENTOS = 100000;
	private static String NOME_ARQUIVO_DUMP_WIKIPEDIA =
			"/home/marco/Downloads/ptwiki-20160720-pages-articles-multistream.xml";
	private static Logger logger =
			LogManager.getLogger(WikipediaSAXParserToLucene.class);
	private Map<String, String> pagina;
	private StringBuilder content = new StringBuilder();
	private IndexadorWikipedia indexador = null;
	// new IndexadorWikipedia(DIRETORIO_INDICE_WIKIPEDIA);
	private int paginasIndexadas;

	public void startElement(String uri, String localName,
			String qName, Attributes attributes)
			throws SAXException {
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
			content.append(String.copyValueOf(ch, start, length)
					.trim());
		}
	}

	public void endElement(String uri, String localName,
			String qName) throws SAXException {
		if (qName.equals("page")) {
			// indexador.indexar(pagina);
			paginasIndexadas++;
			pagina = null;
			if (paginasIndexadas > 0 && paginasIndexadas
					% BUFFER_DOCUMENTOS == 0) {
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
		new WikipediaSAXParserToLucene().parse();
	}

	public void parse() {
		try {
			SAXParserFactory factory =
					SAXParserFactory.newInstance();
			factory.setNamespaceAware(true);
			SAXParser parser = factory.newSAXParser();
			parser.parse(new File(NOME_ARQUIVO_DUMP_WIKIPEDIA),
					this);
			logger.info("Total de paginas: " + paginasIndexadas);
			// indexador.fechar();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
