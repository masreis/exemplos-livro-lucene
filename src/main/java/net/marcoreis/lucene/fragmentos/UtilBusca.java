package net.marcoreis.lucene.fragmentos;

import java.io.IOException;
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

public class UtilBusca {
	private static Logger logger = Logger.getLogger(UtilBusca.class);
	private Directory diretorio;
	private IndexSearcher buscador;
	private IndexReader reader;
	private long duracaoBusca;
	private Integer quantidadeLimiteRegistros = 5000;
	private String diretorioIndice;

	// private SearcherManager sm;

	public void reopen() {
		try {
			diretorio = FSDirectory.open(Paths.get(diretorioIndice));
			// diretorio = new RAMDirectory(FSDirectory.open(new
			// File(diretorioIndice)), IOContext.DEFAULT);
			// sm = new SearcherManager(diretorio, null);
			// reader=sm.acquire();
			// buscador = sm.acquire();
			reader = DirectoryReader.open(diretorio);
			buscador = new IndexSearcher(reader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public UtilBusca(String diretorioIndice) throws IOException {
		this.diretorioIndice = diretorioIndice;
		reopen();
	}

	public void fecha() {
		try {
			diretorio.close();
		} catch (Exception e) {
			logger.error(e);
		}
		try {
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private IndexSearcher getBuscador() throws IOException {
		return buscador;
	}

	public Document doc(int docID) throws IOException {
		Document doc = getBuscador().doc(docID);
		return doc;
	}

	public long getDuracaoBusca() {
		return duracaoBusca;
	}

	public TopDocs busca(String consulta) {
		TopDocs hits = null;
		try {
			long time = System.currentTimeMillis();
			QueryParser parser = new QueryParser("", new StandardAnalyzer());
			Query query = parser.parse(consulta);
			hits = getBuscador().search(query, quantidadeLimiteRegistros);
			duracaoBusca = System.currentTimeMillis() - time;
		} catch (Exception e) {
			logger.error(e);
		}
		return hits;
	}

	public static void main(String[] args) {
		try {
			UtilBusca buscador = new UtilBusca(System.getProperty("user.home") + "/livro-lucene/indice-wikipedia");
			TopDocs topDocs = buscador.busca("text:a*");
			for (ScoreDoc sd : topDocs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
				System.out.println(doc.get("id"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
