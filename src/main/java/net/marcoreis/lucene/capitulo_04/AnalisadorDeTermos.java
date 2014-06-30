package net.marcoreis.lucene.capitulo_04;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.Version;
import org.junit.Test;

public class AnalisadorDeTermos {
    private static final Logger logger = Logger
            .getLogger(AnalisadorDeTermos.class);

    public static void analisarFrase(Analyzer analyzer, String text) {
        try {
            TokenStream stream = analyzer.tokenStream("contents",
                    new StringReader(text));
            stream.reset();
            StringBuilder termos = new StringBuilder();
            termos.append(analyzer.getClass().getSimpleName());
            termos.append(" => ");
            while (stream.incrementToken()) {
                Iterator<AttributeImpl> ite = stream
                        .getAttributeImplsIterator();
                AttributeImpl impl = ite.next();
                termos.append(impl);
                termos.append(" ");
            }
            logger.info(termos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void analisar() {
        String frase = "De origem humilde até a riqueza: veja 11 bilionários que eram pobres na infância";
        Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_48);
        analisarFrase(standardAnalyzer, frase);
        Analyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_48);
        analisarFrase(simpleAnalyzer, frase);
        Analyzer brazilianAnalyzer = new BrazilianAnalyzer(Version.LUCENE_48);
        analisarFrase(brazilianAnalyzer, frase);
    }
}