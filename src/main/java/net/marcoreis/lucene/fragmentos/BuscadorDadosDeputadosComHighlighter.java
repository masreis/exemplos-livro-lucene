package net.marcoreis.lucene.fragmentos;

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
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorDadosDeputadosComHighlighter {
	private static String DIRETORIO_INDICE = System.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-02";
	private static final Logger logger = Logger.getLogger(BuscadorDadosDeputadosComHighlighter.class);

	public static void main(String[] args) {
		BuscadorDadosDeputadosComHighlighter buscador = new BuscadorDadosDeputadosComHighlighter();
		String consulta = "";
		consulta = "nome:josé";
		consulta = "comissao:(comissão AND lei)";
		consulta = "comissao:\"reforma proposta\"~10";
		buscador.buscar(consulta);
	}

	public void buscar(String consulta) {
		try {
			Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher buscador = new IndexSearcher(reader);
			logger.info("Total de deputados indexados: " + reader.maxDoc());
			//
			QueryParser parser = new QueryParser("", new StandardAnalyzer());
			Query query = parser.parse(consulta);
			//
			TopDocs docs = buscador.search(query, 100);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			FastVectorHighlighter fhl = new FastVectorHighlighter();
			FieldQuery fq = fhl.getFieldQuery(query);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
				String fragmentos = fhl.getBestFragment(fq, reader, sd.doc, "comissao", 100000);
				System.out.println(fragmentos);
				Explanation explicacao = buscador.explain(query, sd.doc);
				// logger.info(explicacao.toString());
				logger.info(doc.get("nome"));
				String[] comissoes = doc.getValues("comissao");
				for (String comissao : comissoes) {
					// logger.info(comissao);
				}
			}
			//
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
