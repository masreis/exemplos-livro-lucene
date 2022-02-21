package net.marcoreis.lucene.capitulo_07;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class MoreLikeThisTest {

	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/wikipedia";
	private static final Logger logger =
			LogManager.getLogger(MoreLikeThisTest.class);
	private static FSDirectory directory;
	private static IndexReader reader;
	private static IndexSearcher searcher;
	private static StandardAnalyzer analyzer;

	@BeforeClass
	public static void inicializa() throws IOException {
		directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		reader = DirectoryReader.open(directory);
		searcher = new IndexSearcher(reader);
		analyzer = new StandardAnalyzer();
	}

	@AfterClass
	public static void finalizar() throws IOException {
		directory.close();
		reader.close();
		analyzer.close();
	}

	@Test
	public void encontraSimilares() throws IOException {
		MoreLikeThis mlt = new MoreLikeThis(reader);
		mlt.setMinDocFreq(1);
		mlt.setMinTermFreq(1);
		mlt.setBoost(true);
		mlt.setAnalyzer(analyzer);
		mlt.setFieldNames(new String[] { "conteudo" });
		String textoBase = "star wars";
		//
		Reader reader = new StringReader(textoBase);
		Query query = mlt.like("conteudo", reader);
		TopDocs topDocs = searcher.search(query, 900);
		logger.info("Texto base: " + textoBase);
		logger.info("Documentos similares (" + topDocs.totalHits
				+ "):");
		for (ScoreDoc sd : topDocs.scoreDocs) {
			Document doc = searcher.doc(sd.doc);
			String conteudo = doc.get("conteudo");
			if (!conteudo.toLowerCase().contains(textoBase)) {
				logger.info(doc.get("caminho"));
				if (conteudo.length() > 200) {
					conteudo = conteudo.substring(0, 200);
				}
				logger.info(conteudo);
				// logger.info(conteudo);
			}
		}
	}

	public void encontraSimilaresWrapper() {
		String textoBase = "";
		String campo = "";
		MoreLikeThisQuery mltq = new MoreLikeThisQuery(
				textoBase, new String[] { "conteudo" },
				analyzer, campo);

	}
}
