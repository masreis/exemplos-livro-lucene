package net.marcoreis.lucene.capitulo_03;

import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorArquivosLocais {
	// private static String DIRETORIO_INDICE = System.getProperty("user.home")
	// + "/livro-lucene/aulas-para-concursos";
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/dropbox-epam";
	private static final Logger logger =
			LogManager.getLogger(BuscadorArquivosLocais.class);
	private static final int QUANTIDADE_DE_ITENS_RETORNADOS =
			100;

	public void buscar(String consulta) {
		try {
			//
			// Abrir o índice e preparar o buscador
			Directory diretorio = FSDirectory
					.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher searcher = new IndexSearcher(reader);
			//
			// Criar e analisar a consulta
			QueryParser parser =
					new QueryParser("background check", new StandardAnalyzer());
			// parser.setAllowLeadingWildcard(true);
			// parser.getEnablePositionIncrements();
			Query query = parser.parse(consulta);
			logger.info("Consulta analisada-> " + query);
			//
			// Processar o resultado
			TopDocs docs = searcher.search(query,
					QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: "
					+ docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Caminho: " + doc.get("caminho"));
				logger.info("Data: " + doc.get("data"));
				logger.info("Extensão: " + doc.get("extensao"));
			}
			//
			// Liberar os recursos
			reader.close();
			diretorio.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void buscar(Query query) {
		try {
			//
			logger.info("Consulta analisada-> " + query);
			// Abrir o índice e preparar o buscador
			Directory diretorio = FSDirectory
					.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher searcher = new IndexSearcher(reader);
			//
			// Processar o resultado
			TopDocs docs = searcher.search(query,
					QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: "
					+ docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Caminho: " + doc.get("caminho"));
				logger.info("Data: " + doc.get("data"));
				logger.info("Extensão: " + doc.get("extensao"));
			}
			//
			// Liberar os recursos
			reader.close();
			diretorio.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
