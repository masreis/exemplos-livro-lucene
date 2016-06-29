package net.marcoreis.lucene.capitulo_03;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
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
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/master";

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
		consulta = "conteudo:(ciência informação)";
		// consulta = "conteudo:MunicipiosBeneficiados";
		// consulta = "conteudo:(didáticas)";
		// consulta = "extensao:doc";
		// consulta = "conteudo:(\"instituto quadrix\")";
		 consulta = "tamanho:[0 TO 400]";
		 LongPoint.newRangeQuery("tamanhoLong", 50000, 100000);
		buscador.buscar(consulta);
	}

	public void buscar(String consulta) {
		try {
			//
			// Abrir o índice e preparar o buscador
			Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher buscador = new IndexSearcher(reader);
			//
			// Criar e analisar a consulta
			QueryParser parser = new QueryParser("", new BrazilianAnalyzer());
			Query query = parser.parse(consulta);
			//
			// Processar o resultado
			TopDocs docs = buscador.search(query, QUANTIDADE_DE_ITENS_RETORNADOS);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
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
