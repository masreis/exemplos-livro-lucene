package net.marcoreis.lucene.capitulo_02;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermFilter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorArquivosLocaisComFiltro {
	private static String DIRETORIO_INDICE = System.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-01";
	private static final Logger logger = Logger.getLogger(BuscadorArquivosLocaisComFiltro.class);

	//
	private Directory diretorio;
	private IndexReader reader;
	private IndexSearcher buscador;

	//
	public BuscadorArquivosLocaisComFiltro() throws IOException {
		diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		reader = DirectoryReader.open(diretorio);
		buscador = new IndexSearcher(reader);
	}

	public static void main(String[] args) throws IOException {
		BuscadorArquivosLocaisComFiltro buscador = new BuscadorArquivosLocaisComFiltro();
		buscador.buscarTermQuery();
	}

	public void buscarTermQuery() {
		try {
			logger.info("Filtro");
			QueryParser parser = new QueryParser("", new StandardAnalyzer());
			String consulta = "conteudo:(rede social)";
			Query query = parser.parse(consulta);
			Filter filtro = new TermFilter(new Term("conteudo", "java"));
			TopDocs docs = buscador.search(query, filtro, 100);
			logger.info(query);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
				logger.info("Arquivo: " + doc.get("nome"));
			}
			//
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
