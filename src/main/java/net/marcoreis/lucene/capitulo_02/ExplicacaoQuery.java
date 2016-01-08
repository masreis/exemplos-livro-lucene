package net.marcoreis.lucene.capitulo_02;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class ExplicacaoQuery {
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/indice-capitulo-02";
	private static final Logger logger = Logger.getLogger(ExplicacaoQuery.class);

	public static void main(String[] args) {
		ExplicacaoQuery explicacao = new ExplicacaoQuery();
		String consulta = "";
		consulta = "conteudo:(\"service\")";
		explicacao.buscar(consulta);
	}

	public void buscar(String consulta) {
		try {
			Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher buscador = new IndexSearcher(reader);
			System.out.println(reader.numDocs());
			//
			// Query
			QueryParser parser = new QueryParser("", new StandardAnalyzer());
			Query query = parser.parse(consulta);
			//
			TopDocs topDocs = buscador.search(query, 100);
			for (ScoreDoc sd : topDocs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
				Explanation explicacao = buscador.explain(query, sd.doc);
				// logger.info("Arquivo: " + doc.get("caminho"));
				logger.info(explicacao.toString());
			}
			//
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
