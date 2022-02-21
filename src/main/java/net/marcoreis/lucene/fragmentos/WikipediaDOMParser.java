package net.marcoreis.lucene.fragmentos;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

@Deprecated
/**
 * 
 * @author marco
 *
 */
public class WikipediaDOMParser {
    private static String nomeArquivo = System.getProperty("user.home")
            + "/dados/ptwiki-20130417-stub-articles.xml";
    private static final String PAGE = "page";
    private static final String TITLE = "title";
    private static int paginas = 0;
    private static Logger logger = LogManager.getLogger(WikipediaDOMParser.class);

    public void parse() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(nomeArquivo));
            doc.getDocumentElement().normalize();
            //
            NodeList nodes = doc.getElementsByTagName("page");
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public static void main(String[] args) {
        new WikipediaDOMParser().parse();
    }
}
