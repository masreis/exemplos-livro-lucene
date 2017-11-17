package net.marcoreis.lucene.capitulo_07;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.Lookup;
import org.apache.lucene.search.suggest.analyzing.AnalyzingInfixSuggester;
import org.apache.lucene.search.suggest.analyzing.AnalyzingSuggester;
import org.apache.lucene.search.suggest.analyzing.FreeTextSuggester;
import org.apache.lucene.search.suggest.analyzing.FuzzySuggester;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnalisadorSugestaoTest {

	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/cv";
	private static String DIRETORIO_INFIX =
			System.getProperty("user.home")
					+ "/livro-lucene/cv-infix";
	private static Directory directory;
	private static IndexReader reader;
	private static Analyzer analyzer;
	private String texto = "proj";

	@BeforeClass
	public static void inicializar() throws IOException {
		directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		reader = DirectoryReader.open(directory);
		analyzer = new StandardAnalyzer();
	}

	@Test
	public void testSugestao() throws IOException {
		System.out.println("\nAnalyzingSuggester");
		Dictionary dictionary =
				new LuceneDictionary(reader, "conteudo");
		Analyzer a = new Analyzer() {
			@Override
			protected TokenStreamComponents createComponents(
					final String fieldName) {

				/*
				 * Tokenizer t = new WhitespaceTokenizer(Version.LUCENE_50,
				 * reader); TokenStream tf = t; //TokenStream tf = new
				 * LowerCaseFilter(Version.LUCENE_50, tf); return new
				 * TokenStreamComponents(t, tf);
				 */

				// StandardAnalyzer + ICUFoldingFilter:
				final int maxTokenLength = 255;

				final StandardTokenizer src =
						new StandardTokenizer();
				src.setMaxTokenLength(maxTokenLength);
				TokenStream tok = src;
				// TokenStream tok = new StandardFilter(matchVersion, tok);
				tok = new LowerCaseFilter(tok);
				// tok = new StopFilter(matchVersion, tok,
				// StopAnalyzer.ENGLISH_STOP_WORDS_SET);
				// tok = new ICUFoldingFilter(tok);
				// tok = new EnglishMinimalStemFilter(tok);
				return new TokenStreamComponents(src, tok) {
					@Override
					protected void setReader(
							final Reader reader) {
						src.setMaxTokenLength(maxTokenLength);
						super.setReader(reader);
					}
				};
			}
		};

		AnalyzingSuggester analisadorSugestao =
				new AnalyzingSuggester(directory,
						"temp_default", a);
		analisadorSugestao.build(dictionary);
		List<Lookup.LookupResult> lookupResultList =
				analisadorSugestao.lookup(texto, false, 5);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": "
					+ lookupResult.value);
		}
	}

	@Test
	public void testInfixSugestao() throws IOException {
		System.out.println("\nAnalyzingInfixSuggester");
		Dictionary dictionary =
				new LuceneDictionary(reader, "conteudo");
		FSDirectory directoryInfix =
				FSDirectory.open(Paths.get(DIRETORIO_INFIX));
		AnalyzingInfixSuggester analisadorSugestao =
				new AnalyzingInfixSuggester(directoryInfix,
						analyzer);
		analisadorSugestao.build(dictionary);
		List<Lookup.LookupResult> lookupResultList =
				analisadorSugestao.lookup(texto, false, 5);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": "
					+ lookupResult.value);
		}
		analisadorSugestao.close();
	}

	@Test
	public void testFreeTextSugestao() throws IOException {
		System.out.println("\nFreeTextSuggester");
		Dictionary dictionary =
				new LuceneDictionary(reader, "conteudo");
		FreeTextSuggester analisadorSugestao =
				new FreeTextSuggester(analyzer);
		analisadorSugestao.build(dictionary);
		List<Lookup.LookupResult> lookupResultList =
				analisadorSugestao.lookup(texto, false, 5);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": "
					+ lookupResult.value);
		}
	}

	@Test
	public void testFuzzySugestao() throws IOException {
		System.out.println("\nFuzzySuggester");
		Dictionary dictionary =
				new LuceneDictionary(reader, "conteudo");
		FuzzySuggester analisadorSugestao = new FuzzySuggester(
				directory, "temp_fuzzy", analyzer);
		analisadorSugestao.build(dictionary);
		List<Lookup.LookupResult> lookupResultList =
				analisadorSugestao.lookup(texto, false, 5);
		for (Lookup.LookupResult lookupResult : lookupResultList) {
			System.out.println(lookupResult.key + ": "
					+ lookupResult.value);
		}
	}

	public void testBlended() {
		// BlendedInfixSuggester
		// CompletionAnalyzer
		// NRTSuggester
		// NRTSuggesterBuilder
		// SuggestField
		// SuggestIndexSearcher
		// WordBreakSpellChecker
	}
}
