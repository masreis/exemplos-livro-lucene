package net.marcoreis.lucene.capitulo_03.teste;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.IndexadorArquivosLocais;

public class TesteIndexarDocumentoIndice {
	private static final Logger logger = Logger.getLogger(TesteIndexadorArquivosLocais.class);
	private static String CAMINHO_ARQUIVO = "/home/marco/Dropbox/material-de-estudo/mestrado/thesis.pdf";
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/indice";
	private Directory diretorio;

	@Test
	public void testeIndexacao() {
		try {
			String nomeArquivo = "/home/marco/Dropbox/material-de-estudo/mestrado/thesis.pdf";
			Term t = new Term("caminho", nomeArquivo);
			Query queryParaExclusao = new TermQuery(t);
			verificarQuantidadeDocumentos(queryParaExclusao);
			//
			IndexadorArquivosLocais indexador = new IndexadorArquivosLocais();
			indexador.setDiretorioIndice(DIRETORIO_INDICE);
			indexador.setRecursivo(true);
			indexador.inicializar();
			indexador.indexarArquivo(new File(CAMINHO_ARQUIVO));
			indexador.finalizar();
			//
			verificarQuantidadeDocumentos(queryParaExclusao);
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void verificarQuantidadeDocumentos(Query queryParaExclusao) throws IOException {
		IndexReader reader = DirectoryReader.open(diretorio);
		IndexSearcher searcher = new IndexSearcher(reader);
		TopDocs docs = searcher.search(queryParaExclusao, 1);
		logger.info("Quantidade de documentos encontrados: " + docs.totalHits);
		// Verifica se a consulta retorna apenas um documento
		if (docs.totalHits != 1) {
			// Aconteceu algum problema
		}
		//
		logger.info("MaxDoc: " + reader.maxDoc());
		logger.info("NumDocs: " + reader.numDocs());
		logger.info("HasDeletions: " + reader.hasDeletions());
		reader.close();
	}

	@Before
	public void inicializar() throws IOException {
		diretorio = FSDirectory.open(Paths.get((DIRETORIO_INDICE)));
	}
}
