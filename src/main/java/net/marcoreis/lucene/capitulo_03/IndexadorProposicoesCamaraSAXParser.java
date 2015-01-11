package net.marcoreis.lucene.capitulo_03;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class IndexadorProposicoesCamaraSAXParser extends DefaultHandler {
    public static final String DIRETORIO_INDICE = System
            .getProperty("user.home")
            + "/livro-lucene/indice-capitulo-03-exemplo-01";
    private static URL DIRETORIO_DADOS = IndexadorProposicoesCamaraSAXParser.class
            .getClassLoader().getResource("dados/proposicoes");
    private static Logger logger = Logger
            .getLogger(IndexadorProposicoesCamaraSAXParser.class);
    private StringBuilder content = new StringBuilder();
    private IndexWriter writer;
    private Document proposicao;

    @Before
    public void inicializar() throws IOException {
        FileUtils.deleteDirectory(new File(DIRETORIO_INDICE));
        Directory dir = FSDirectory.open(new File(DIRETORIO_INDICE));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3,
                analyzer);
        writer = new IndexWriter(dir, conf);
    }

    @After
    public void finalizar() throws IOException {
        writer.close();
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (qName.equals("deputado")) {
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (proposicao != null) {
            content.append(String.copyValueOf(ch, start, length).trim());
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // Adicionar o documento no índice quando encontrar a tag </deputado>
        if (qName.equals("deputado")) {
            try {
                writer.addDocument(proposicao);
            } catch (IOException e) {
                logger.error(e);
            }
        } else if (qName.equals("ideCadastro")) {
        }
    }

    @Test
    public void indexar() throws ParserConfigurationException, SAXException,
            IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        // parser.parse(DIRETORIO_DADOS, this);
        Assert.assertTrue(writer.numDocs() == 513);
    }

}
