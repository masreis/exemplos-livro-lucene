package net.marcoreis.lucene.capitulo_05;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.junit.Test;

public class TesteExplicacaoConsulta {
	private static final Logger logger = Logger
			.getLogger(AnalisadorDeTermos.class);

	@Test
	public void testeExplicacao()
			throws IOException, ParseException {
		Directory diretorio = new IndiceEmMemoria()
				.getRamDirectory();
		IndexReader reader = DirectoryReader.open(diretorio);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("",
				new StandardAnalyzer());
		String consulta = "conteudo:linux";
		Query query = parser.parse(consulta);
		TopDocs docs = searcher.search(query, 3);
		for (int i = 0; i < docs.scoreDocs.length; i++) {
			Explanation explain = searcher.explain(query,
					docs.scoreDocs[i].doc);
			logger.info(explain);
		}
		//
		diretorio.close();
		reader.close();
	}
}
