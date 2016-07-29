package net.marcoreis.lucene.capitulo_03;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
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
	// + "/livro-lucene/cursos";
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/somente-dropbox";

	private static final Logger logger = Logger.getLogger(BuscadorArquivosLocais.class);

	private static final int QUANTIDADE_DE_ITENS_RETORNADOS = 100;

	public static void main(String[] args) {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String consulta = "";
		consulta = "data:[2010 TO 201202]";
		// consulta = "conteudo:rafael~2";
		// consulta = "dataAtualizacao:[2014-05-01 TO 2014-05-30]";
		// consulta = "conteudo:(rede social)";
		// consulta = "conteudo:a*";
		// consulta = "conteudo:itext";
		// consulta = "conteudoComVetores:u7ddle2941splce2rnrna";
		// consulta = "conteudo:(ssh AND integrator)";
		consulta = "conteudo:jmeter";
		consulta = "conteudo:\"Application Lifecycle Management\" AND extensao:pdf";
		consulta = "conteudo:(recall AND precision)";
		// consulta = "conteudo:MunicipiosBeneficiados";
		// consulta = "conteudo:(didáticas)";
		// consulta = "extensao:doc";
		// consulta = "conteudo:(\"instituto quadrix\")";
		// consulta = "tamanhoLong:[0 TO 400]";
		consulta = "conteudo:(extrair parcela)";
		buscador.buscar(consulta);
	}

	public void buscar(String consulta) {
		try {
			//
			// Abrir o índice e preparar o buscador
			Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher searcher = new IndexSearcher(reader);
			//
			// Criar e analisar a consulta
			QueryParser parser = new QueryParser("", new StandardAnalyzer());
			Query query = parser.parse(consulta);
			//
			// Processar o resultado
			TopDocs docs = searcher.search(query, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
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
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
