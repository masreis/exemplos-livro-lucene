package net.marcoreis.lucene.capitulo_02;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class DadosLegislativoSAXParserToLucene extends DefaultHandler {
    public static final String DIRETORIO_INDICE_LEGISLATIVO = System
            .getProperty("user.home") + "/livro-lucene/indice-capitulo-02-02";
    private static String nomeArquivo = System.getProperty("user.home")
            + "/Dropbox/Autoria/book-lucene/ObterDeputados.xml";
    private static Logger logger = Logger
            .getLogger(DadosLegislativoSAXParserToLucene.class);
    private StringBuilder content = new StringBuilder();
    private IndexWriter writer;
    private Document parlamentar;

    public DadosLegislativoSAXParserToLucene() {
        try {
            Directory dir = FSDirectory.open(new File(
                    DIRETORIO_INDICE_LEGISLATIVO));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_48,
                    analyzer);
            writer = new IndexWriter(dir, conf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (qName.equals("deputado")) {
            parlamentar = new Document();
            content.setLength(0);
        } else if (qName.equals("ideCadastro")) {
            content.setLength(0);
        } else if (qName.equals("condicao")) {
            content.setLength(0);
        } else if (qName.equals("matricula")) {
            content.setLength(0);
        } else if (qName.equals("idParlamentar")) {
            content.setLength(0);
        } else if (qName.equals("nome")) {
            content.setLength(0);
        } else if (qName.equals("nomeParlamentar")) {
            content.setLength(0);
        } else if (qName.equals("urlFoto")) {
            content.setLength(0);
        } else if (qName.equals("sexo")) {
            content.setLength(0);
        } else if (qName.equals("uf")) {
            content.setLength(0);
        } else if (qName.equals("comissao")) {
            content.setLength(0);
            content.append(attributes.getValue("nome"));
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (parlamentar != null) {
            content.append(String.copyValueOf(ch, start, length).trim());
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        if (qName.equals("deputado")) {
            try {
                writer.addDocument(parlamentar);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            parlamentar = null;
        } else if (qName.equals("ideCadastro")) {
            parlamentar.add(new StringField("ideCadastro", content.toString(),
                    Store.YES));
        } else if (qName.equals("condicao")) {
            parlamentar.add(new StringField("condicao", content.toString(),
                    Store.YES));
        } else if (qName.equals("matricula")) {
            parlamentar.add(new StringField("matricula", content.toString(),
                    Store.YES));
        } else if (qName.equals("idParlamentar")) {
            parlamentar.add(new StringField("idParlamentar",
                    content.toString(), Store.YES));
        } else if (qName.equals("nome")) {
            parlamentar
                    .add(new TextField("nome", content.toString(), Store.YES));
        } else if (qName.equals("nomeParlamentar")) {
            parlamentar.add(new TextField("nomeParlamentar",
                    content.toString(), Store.YES));
        } else if (qName.equals("urlFoto")) {
            parlamentar.add(new StringField("urlFoto", content.toString(),
                    Store.YES));
        } else if (qName.equals("sexo")) {
            parlamentar.add(new StringField("sexo", content.toString(),
                    Store.YES));
        } else if (qName.equals("uf")) {
            parlamentar.add(new TextField("uf", content.toString(), Store.YES));
        } else if (qName.equals("comissao")) {
            parlamentar.add(new TextField("comissao", content.toString(),
                    Store.YES));
        }
    }

    public static void main(String[] args) {
        new DadosLegislativoSAXParserToLucene().parse();
    }

    public void parse() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(true);
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(nomeArquivo), this);
            writer.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
