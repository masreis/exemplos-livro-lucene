package net.marcoreis.lucene.capitulo_07;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
<<<<<<< HEAD
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class TesteMoreLikeThisPorDocumentID {
	private static String DIRETORIO_INDICE = System
			.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-01";
	private static FSDirectory directory;
	private static DirectoryReader reader;
	private static IndexSearcher searcher;
	private static StandardAnalyzer analyzer;
	private static final Logger logger = Logger
			.getLogger(TesteMoreLikeThisPorDocumentID.class);

	@BeforeClass
	public static void inicializar() throws IOException {
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

	public void buscarDocumentosSimilares() {
		try {
			//
			MoreLikeThis mlt = new MoreLikeThis(reader);
=======
import org.junit.Test;

public class TesteMoreLikeThisPorDocumentID {
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/dropbox";
	private static final Logger logger = Logger
			.getLogger(TesteMoreLikeThisPorDocumentID.class);

	@Test
	public void buscarDocumentosSimilares() {
		try {
			Directory directory = FSDirectory
					.open(Paths.get(DIRETORIO_INDICE));
			IndexReader ir = DirectoryReader.open(directory);
			IndexSearcher is = new IndexSearcher(ir);
			Analyzer analyzer = new StandardAnalyzer();
			//
			MoreLikeThis mlt = new MoreLikeThis(ir);
>>>>>>> 72d874ccf40226eca0af3c3c5135059e58d18aa4
			mlt.setAnalyzer(analyzer);
			mlt.setFieldNames(new String[] { "conteudo" });
			//
			int documentoID = 0;
			QueryParser parser = new QueryParser("", analyzer);
<<<<<<< HEAD
			String nomeArquivo =
					"conteudo:(java ee 6 tutorial)";
			Query queryOrigem = parser.parse(nomeArquivo);
			TopDocs topdocsOrigem =
					searcher.search(queryOrigem, 1);
=======
			String nomeArquivo = "conteudo:(java ee 6 tutorial)";
			Query queryOrigem = parser.parse(nomeArquivo);
			TopDocs topdocsOrigem = is.search(queryOrigem, 1);
>>>>>>> 72d874ccf40226eca0af3c3c5135059e58d18aa4
			if (topdocsOrigem.totalHits == 0) {
				throw new RuntimeException(
						"Não encontrou nenhum documento.");
			}
			for (ScoreDoc sd : topdocsOrigem.scoreDocs) {
<<<<<<< HEAD
				Document doc = searcher.doc(sd.doc);
				logger.info("Documento base: "
						+ doc.get("caminho"));
=======
				Document doc = is.doc(sd.doc);
				logger.info(
						"Documento base: " + doc.get("caminho"));
>>>>>>> 72d874ccf40226eca0af3c3c5135059e58d18aa4
				documentoID = sd.doc;
			}
			//
			logger.info(
					"Parâmetros utilizados para gerar a query: "
							+ mlt.describeParams());
			logger.info("Termos interessantes: ");
			String[] termosInteressantes =
					mlt.retrieveInterestingTerms(documentoID);
			for (String termo : termosInteressantes) {
				logger.info(termo);
			}
			//
			Query query = mlt.like(documentoID);
<<<<<<< HEAD
			TopDocs topDocs = searcher.search(query, 10);
			logger.info("Documentos similares ("
					+ topDocs.totalHits + "):");
			for (ScoreDoc sd : topDocs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
=======
			TopDocs topDocs = is.search(query, 10);
			logger.info("Documentos similares ("
					+ topDocs.totalHits + "):");
			for (ScoreDoc sd : topDocs.scoreDocs) {
				Document doc = is.doc(sd.doc);
>>>>>>> 72d874ccf40226eca0af3c3c5135059e58d18aa4
				logger.info(doc.get("caminho"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
