package net.marcoreis.lucene.capitulo_wikipedia;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
    private int paginasIndexadas;
    private SimpleDateFormat sdf = new SimpleDateFormat(
	    "yyyy-MM-dd'T'HH:mm:ss'Z'");
    private Connection conexao;
    private String pwd = "";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";
    private PreparedStatement pstmt;

    public WikipediaSAXParserToJDBC() {
	try {
	    Class.forName(driver);
	    conexao = DriverManager.getConnection(url, user, pwd);
	    String sql = "insert into PaginaWikipedia (id, title, timestamp, username, text, model, format, comment) values (?,?,?,?,?,?,?,?,?)";
	    pstmt = conexao.prepareStatement(sql);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

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
	if (pagina != null) {
	    content.append(String.copyValueOf(ch, start, length).trim());
	}
    }

    public void endElement(String uri, String localName, String qName)
	    throws SAXException {
	if (qName.equals("page")) {
	    inserirComJDBC(pagina);
	    paginasIndexadas++;
	    pagina = null;
	    if (paginasIndexadas > 0 && paginasIndexadas % 10000 == 0) {
		logger.info("Parcial: " + paginasIndexadas);
	    }
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

    private void inserirComJDBC(PaginaWikipedia p) {
	try {
	    pstmt.setLong(1, pagina.getId());
	    pstmt.setString(2, pagina.getTitle());
	    java.sql.Date data = new java.sql.Date(pagina.getTimeStamp()
		    .getTime());
	    pstmt.setTimestamp(3, new Timestamp(data.getTime()));
	    pstmt.setString(4, pagina.getUserName());
	    pstmt.setString(5, pagina.getText());
	    pstmt.setString(6, pagina.getModel());
	    pstmt.setString(7, pagina.getFormat());
	    pstmt.setString(8, pagina.getComment());
	    int qtd = pstmt.executeUpdate();
	    if (qtd != 1) {
		logger.error("Registro não incluido -> " + pagina.getId());
	    }
	} catch (Exception e) {
	    logger.error(e);
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
	    pstmt.close();
	    conexao.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
