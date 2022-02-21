package net.marcoreis.lucene.fragmentos;

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
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.SortField.Type;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorArquivosLocaisOrdenado {
	private static String DIRETORIO_INDICE = System
			.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-01";
	private static final Logger logger = LogManager
			.getLogger(BuscadorArquivosLocaisOrdenado.class);

	public static void main(String[] args) {
		BuscadorArquivosLocaisOrdenado buscador =
				new BuscadorArquivosLocaisOrdenado();
		String consulta = "";
		consulta = "dataAtualizacao:{2014-05-01 TO 2014-05-08}";
		buscador.buscar(consulta);
	}

	public void buscar(String consulta) {
		try {
			Directory diretorio = FSDirectory
					.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader =
					DirectoryReader.open(diretorio);
			IndexSearcher buscador = new IndexSearcher(reader);
			//
			// Query
			QueryParser parser =
					new QueryParser("", new StandardAnalyzer());
			Query query = parser.parse(consulta);
			//
			Sort ordenacao = new Sort();
			SortField campoOrdenado = new SortField(
					"dataAtualizacao", Type.STRING);
			ordenacao.setSort(campoOrdenado);
			TopDocs docs =
					buscador.search(query, 100, ordenacao);
			logger.info("Quantidade de itens encontrados: "
					+ docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
				logger.info("Arquivo: " + doc.get("nome"));
				logger.info("Tamanho: " + doc.get("tamanho"));
				logger.info("Atualização: "
						+ doc.get("dataAtualizacao"));
			}
			//
			diretorio.close();
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
