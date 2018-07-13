package net.marcoreis.lucene.capitulo_05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.junit.Test;

public class AnalisadorDeTermosTest {
	private String frase = "De origem humilde até a riqueza: "
			+ "veja 11 bilionários que eram pobres na infância.\n"
			+ "Trabalho duro e resiliência é a característica comum a todos. "
			+ "Contudo, eles representam apenas 1% da população.";

	@Test
	public void analisar() throws IOException {
		Analyzer standardAnalyzer = new StandardAnalyzer();
		AnalisadorDeTermos.analisarFrase(standardAnalyzer,
				frase);
		Analyzer simpleAnalyzer = new SimpleAnalyzer();
		AnalisadorDeTermos.analisarFrase(simpleAnalyzer, frase);
		Analyzer brazilianAnalyzer = new BrazilianAnalyzer();
		AnalisadorDeTermos.analisarFrase(brazilianAnalyzer,
				frase);
		Analyzer whiteSpaceAnalyzer = new WhitespaceAnalyzer();
		AnalisadorDeTermos.analisarFrase(whiteSpaceAnalyzer,
				frase);
		Analyzer keyWordAnalyzer = new KeywordAnalyzer();
		AnalisadorDeTermos.analisarFrase(keyWordAnalyzer, frase);
	}

	@Test
	public void analisarComStopWords() throws IOException {
		// Cria lista de stop words em português
		Collection<String> listaDeStopWords = new ArrayList<String>();
		listaDeStopWords.add("de");
		listaDeStopWords.add("até");
		listaDeStopWords.add("que");
		listaDeStopWords.add("e");
		listaDeStopWords.add("a");
		CharArraySet stopWords = new CharArraySet(
				listaDeStopWords, true);
		// Aplica a lista ao StandardAnalyzer
		Analyzer standardAnalyzer = new StandardAnalyzer(
				stopWords);
		AnalisadorDeTermos.analisarFrase(standardAnalyzer,
				frase);
	}
}
