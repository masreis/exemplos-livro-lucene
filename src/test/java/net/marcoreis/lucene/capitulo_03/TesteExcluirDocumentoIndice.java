package net.marcoreis.lucene.capitulo_03;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteExcluirDocumentoIndice {
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/indice";
	private static final Logger logger = Logger.getLogger(TesteExcluirDocumentoIndice.class);
	private Analyzer analyzer;
	private Directory diretorio;
	private IndexWriterConfig conf;
	private IndexWriter writer;

	@Before
	public void inicializarWriter() throws IOException {
		analyzer = new StandardAnalyzer();
		diretorio = FSDirectory.open(Paths.get((DIRETORIO_INDICE)));
		conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(diretorio, conf);
	}

	@Test
	public void teste01ExclusaoArquivo() throws IOException {
		// Termo que define o documento que será excluído
		Term termoParaExclusao = new Term("caminho",
				"/home/marco/Dropbox/mestrado/uso-de-mineracao-distribuicao-mpdft.pdf");
		verificarQuantidadeDocumentos(termoParaExclusao);
		// Verifica a quantidade de documentos antes da exclusão
		writer.deleteDocuments(termoParaExclusao);
		writer.commit();
		// Verifica a quantidade de documentos depois da exclusão
		verificarQuantidadeDocumentos(termoParaExclusao);
	}

	@After
	public void finalizar() throws IOException {
		writer.close();
		diretorio.close();
	}

	private void verificarQuantidadeDocumentos(Term termoParaExclusao) throws IOException {
		IndexReader reader = DirectoryReader.open(diretorio);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(new TermQuery(termoParaExclusao), 1);
		logger.info("Quantidade de documentos encontrados: " + docs.totalHits);
		// Verifica se a consulta retorna apenas um documento
		if (docs.totalHits > 1) {
			// Aconteceu algum problema
			logger.warn("Essa exclusão é potencialmente perigosa");
		}
		//
		logger.info("NumDocs: " + reader.numDocs());
		logger.info("MaxDoc: " + reader.maxDoc());
		logger.info("HasDeletions: " + reader.hasDeletions());
		reader.close();
	}

}
