package net.marcoreis.lucene.fragmentos;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class BuscadorArquivosLocaisComAPI {
	// private static String DIRETORIO_INDICE = System.getProperty("user.home")
	// + "/livro-lucene/indice-capitulo-02-exemplo-01";
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/indice";
	private static final Logger logger = Logger.getLogger(BuscadorArquivosLocaisComAPI.class);
	private Directory diretorio;
	private IndexReader reader;
	private IndexSearcher searcher;
	private int QUANTIDADE_DE_ITENS_RETORNADOS = 100;

	//

	private void fechar() {
		try {
			reader.close();
			diretorio.close();
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void abrirIndice() {
		try {
			diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			reader = DirectoryReader.open(diretorio);
			searcher = new IndexSearcher(reader);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void buscarTermQuery() {
		try {
			Term term = new Term("conteudo", "extrair");
			Query query = new TermQuery(term);
			TopDocs docs = searcher.search(query, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Caminho: " + doc.get("caminho"));
				logger.info("Data: " + doc.get("data"));
				logger.info("Extensão: " + doc.get("extensao"));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void buscarPhraseQuery() {
		try {
			PhraseQuery query = new PhraseQuery("conteudo", "ciência", "da", "informação");
			TopDocs docs = searcher.search(query, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Caminho: " + doc.get("caminho"));
				logger.info("Data: " + doc.get("data"));
				logger.info("Extensão: " + doc.get("extensao"));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void buscarMultiPhraseQuery() {
		try {
			Term[] termosAplicacaoExemplo = new Term[] { new Term("conteudo", "aplicação"),
					new Term("conteudo", "exemplo") };
			Term[] termosBancoDados = new Term[] { new Term("conteudo", "banco"), new Term("conteudo", "dados") };
			//
			// MultiPhraseQuery mpq = new MultiPhraseQuery();
			// mpq.add(termosAplicacaoExemplo);
			// mpq.add(termosBancoDados);
			// mpq.setSlop(2);
			//
			TopDocs docs = searcher.search(null, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Arquivo: " + doc.get("nome"));
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Atualização: " + doc.get("dataAtualizacao"));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void buscarBooleanQuery() {
		try {
			Query q1 = new TermQuery(new Term("conteudo", "ciência"));
			Query q2 = new TermQuery(new Term("conteudo", "da"));
			Query q3 = new TermQuery(new Term("conteudo", "informação"));
			BooleanQuery query = new BooleanQuery.Builder().add(q1, Occur.SHOULD).add(q2, Occur.SHOULD)
					.add(q3, Occur.SHOULD).build();
			logger.info("Consulta-> " + query);
			//
			TopDocs docs = searcher.search(query, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Caminho: " + doc.get("caminho"));
				logger.info("Data: " + doc.get("data"));
				logger.info("Extensão: " + doc.get("extensao"));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void buscarRangeQuery() {
		try {
			boolean incluirLimiteInferior = true;
			boolean incluirLimiteSuperior = true;
			BytesRef limiteInferior = new BytesRef("20160120");
			BytesRef limiteSuperior = new BytesRef("20161231");
			TermRangeQuery query = new TermRangeQuery("data", limiteInferior, limiteSuperior, incluirLimiteInferior,
					incluirLimiteSuperior);
			TopDocs docs = searcher.search(query, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Arquivo: " + doc.get("caminho"));
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Atualização: " + doc.get("data"));
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public long count(final Query qry) {
		long outCount = 0;
		// try {
		// _searchManager.waitForGeneration(_reopenToken); // wait untill the
		// // index is
		// // re-opened
		// IndexSearcher searcher = _searchManager.acquire();
		// try {
		// TopDocs docs = searcher.search(qry, 0);
		// if (docs != null)
		// outCount = docs.totalHits;
		// log.debug("count-search executed against lucene index returning {}",
		// outCount);
		// } finally {
		// _searchManager.release(searcher);
		// }
		// } catch (IOException ioEx) {
		// log.error("Error re-opening the index {}", ioEx.getMessage(), ioEx);
		// }
		return outCount;
	}
}
