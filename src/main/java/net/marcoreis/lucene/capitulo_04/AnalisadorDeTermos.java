package net.marcoreis.lucene.capitulo_04;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.Version;

public class AnalisadorDeTermos {
    private static final Logger logger = Logger
            .getLogger(AnalisadorDeTermos.class);

    public static void analisarFrase(Analyzer analyzer, String texto) {
        try {
            TokenStream stream = analyzer.tokenStream(null, new StringReader(
                    texto));
            stream.reset();
            StringBuilder termos = new StringBuilder();
            termos.append(analyzer.getClass().getSimpleName());
            termos.append(" => ");
            while (stream.incrementToken()) {
                Iterator<AttributeImpl> ite = stream
                        .getAttributeImplsIterator();
                AttributeImpl impl = ite.next();
                termos.append(impl);
                termos.append("|");
            }
            logger.info(termos);
        } catch (IOException e) {
            logger.error(e);
        }
    }

    public void analisarComStopWords() throws IOException {
        String frase = "De origem humilde até a riqueza: veja 11 bilionários que eram pobres na infância.\n"
                + "Trabalho duro e resiliência é a característica comum a todos.";
        //
        Collection<String> listaDeStopWords = new ArrayList<String>();
        listaDeStopWords.add("de");
        listaDeStopWords.add("até");
        listaDeStopWords.add("que");
        listaDeStopWords.add("e");
        listaDeStopWords.add("a");
        CharArraySet stopWords = new CharArraySet(Version.LUCENE_48,
                listaDeStopWords, true);
        // Analisandor padrão
        Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_48);
        analisarFrase(standardAnalyzer, frase);
        // Analisador padrão com as stop words indicadas
        Analyzer standardAnalyzerComStopWords = new StandardAnalyzer(
                Version.LUCENE_48, stopWords);
        analisarFrase(standardAnalyzerComStopWords, frase);
    }

    public void analisar() throws IOException {
        String frase = "De origem humilde até a riqueza: veja 11 bilionários que eram pobres na infância.\n"
                + "Trabalho duro e resiliência é a característica comum a todos.";
        Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_48);
        analisarFrase(standardAnalyzer, frase);
        Analyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_48);
        analisarFrase(simpleAnalyzer, frase);
        Analyzer brazilianAnalyzer = new BrazilianAnalyzer(Version.LUCENE_48);
        analisarFrase(brazilianAnalyzer, frase);
        Analyzer whiteSpaceAnalyzer = new WhitespaceAnalyzer(Version.LUCENE_48);
        analisarFrase(whiteSpaceAnalyzer, frase);
        Analyzer keyWordAnalyzer = new KeywordAnalyzer();
        analisarFrase(keyWordAnalyzer, frase);
    }

    public static void main(String[] args) {
        try {
            logger.info("Analisadores");
            new AnalisadorDeTermos().analisar();
            logger.info("Analisadores com stop words");
            new AnalisadorDeTermos().analisarComStopWords();
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
