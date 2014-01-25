package net.marcoreis.lucene.capitulo_04;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.util.AttributeImpl;
import org.apache.lucene.util.Version;

public class AnalyzerUtil {

    public static void analisarFrase(Analyzer analyzer, String text) {
	try {
	    TokenStream stream = analyzer.tokenStream("contents",
		    new StringReader(text));
	    stream.reset();
	    System.out.println("\n" + analyzer.getClass().getName());
	    while (stream.incrementToken()) {
		Iterator<AttributeImpl> ite = stream
			.getAttributeImplsIterator();
		AttributeImpl impl = ite.next();
		System.out.print(impl);
		System.out.print(" ");
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	String frase = "De origem humilde à riqueza: veja 11 bilionários que eram pobres na infância";
	Analyzer standardAnalyzer = new StandardAnalyzer(Version.LUCENE_46);
	analisarFrase(standardAnalyzer, frase);
	Analyzer brazilianAnalyzer = new BrazilianAnalyzer(Version.LUCENE_46);
	analisarFrase(brazilianAnalyzer, frase);
	Analyzer simpleAnalyzer = new SimpleAnalyzer(Version.LUCENE_46);
	analisarFrase(simpleAnalyzer, frase);
    }
}
