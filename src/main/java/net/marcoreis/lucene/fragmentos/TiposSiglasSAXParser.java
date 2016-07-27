package net.marcoreis.lucene.fragmentos;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class TiposSiglasSAXParser extends DefaultHandler {
    private static InputStream ARQUIVO_DADOS = IndexadorDadosDeputadosSAXParser.class
            .getClassLoader().getResourceAsStream(
                    "dados/ListarSiglasTipoProposicao.xml");

    private Collection<String> siglas;
    private StringBuilder sigla = new StringBuilder();

    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (qName.equals("sigla")) {
            sigla.setLength(0);
            sigla.append(attributes.getValue("tipoSigla").trim());
        }
    }

    public void characters(char[] ch, int start, int length)
            throws SAXException {
        if (siglas != null) {
            sigla.append(String.copyValueOf(ch, start, length).trim());
        }
    }

    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        // Adicionar o documento no índice quando encontrar a tag </deputado>
        if (qName.equals("sigla")) {
            siglas.add(sigla.toString());
        }
    }

    public Collection<String> recuperarSiglas()
            throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setNamespaceAware(true);
        SAXParser parser = factory.newSAXParser();
        siglas = new ArrayList<>();
        parser.parse(ARQUIVO_DADOS, this);
        return siglas;
    }

    public static void main(String[] args) throws ParserConfigurationException,
            SAXException, IOException {
        TiposSiglasSAXParser parser = new TiposSiglasSAXParser();
        Collection<String> lista = parser.recuperarSiglas();
        System.out.println(lista);
    }
}
