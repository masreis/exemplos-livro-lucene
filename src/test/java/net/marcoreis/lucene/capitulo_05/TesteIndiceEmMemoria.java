package net.marcoreis.lucene.capitulo_05;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class TesteIndiceEmMemoria {
	@Test
	public void testeEmMemoria()
			throws IOException, ParseException {
		Directory diretorio = new IndiceEmMemoria()
				.recuperarDiretorioEmMemoria();
		IndexReader reader = DirectoryReader.open(diretorio);
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser("",
				new StandardAnalyzer());
		String consulta = "conteudo:alsa";
		Query query = parser.parse(consulta);
		TopDocs docs = searcher.search(query, 1);
		//
		assertTrue(docs.totalHits > 0);
	}
}
