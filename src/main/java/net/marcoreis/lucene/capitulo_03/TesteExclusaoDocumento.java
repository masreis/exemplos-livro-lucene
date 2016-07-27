package net.marcoreis.lucene.capitulo_03;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteExclusaoDocumento {
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/somente-dropbox";
	private static final Logger logger = Logger.getLogger(TesteExclusaoDocumento.class);
	private String nomeArquivo = "/home/marco/Dropbox/pendencias.md";
	private Analyzer analyzer;
	private Directory diretorio;
	private IndexWriterConfig conf;
	private IndexWriter writer;
	private IndexReader reader;
	private IndexSearcher searcher;

	@Before
	public void inicializar() throws IOException {
		analyzer = new BrazilianAnalyzer();
		diretorio = FSDirectory.open(Paths.get((DIRETORIO_INDICE)));
		conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(diretorio, conf);
		//
	}

	@Test
	public void testeExclusao() throws IOException {
		Query queryParaExclusao = new PhraseQuery("caminho", nomeArquivo);
		//
		verificarQuantidadeDocumentos(queryParaExclusao);
		writer.deleteDocuments(queryParaExclusao);
		// writer.commit();
		verificarQuantidadeDocumentos(queryParaExclusao);
		//
		writer.close();
	}

	private void verificarQuantidadeDocumentos(Query queryParaExclusao) throws IOException {
		reader = DirectoryReader.open(writer);
		searcher = new IndexSearcher(reader);

		TopDocs docs = searcher.search(queryParaExclusao, 1);
		logger.info("Quantidade de documentos encontrados: " + docs.totalHits);
		// Verifica se a consulta retorna apenas um documento
		// assertTrue(docs.totalHits < 2);
		//
		logger.info("MaxDoc: " + reader.maxDoc());
		logger.info("NumDocs: " + reader.numDocs());
		logger.info("HasDeletions: " + reader.hasDeletions());
		reader.close();
	}

	@Test
	public void testeDepoisDaExclusao() throws IOException {
		Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		IndexReader reader = DirectoryReader.open(diretorio);
		reader.hasDeletions();
	}
}
