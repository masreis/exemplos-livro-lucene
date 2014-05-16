package net.marcoreis.lucene.capitulo_02;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class BuscadorArquivosLocais {
    private static String diretorioIndice = System.getProperty("user.home")
	    + "/livro-lucene/indice-capitulo-02";
    private static final Logger logger = Logger
	    .getLogger(BuscadorArquivosLocais.class);

    public static void main(String[] args) {
	BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
	String consulta = "conteudo:miotti";
	// String consulta = "dataAtualizacao:[20130531 TO 20130627]";
	consulta = "conteudo:miotti";
	buscador.buscar(consulta);
    }

    public void buscar(String consulta) {
	try {
	    Directory diretorio = FSDirectory.open(new File(diretorioIndice));
	    IndexReader reader = DirectoryReader.open(diretorio);
	    IndexSearcher buscador = new IndexSearcher(reader);
	    //
	    // Query
	    QueryParser parser = new QueryParser(Version.LUCENE_46, "",
		    new StandardAnalyzer(Version.LUCENE_46));
	    parser.setDefaultOperator(Operator.AND);
	    Query query = parser.parse(consulta);
	    //
	    TopDocs docs = buscador.search(query, 100);
	    for (ScoreDoc sd : docs.scoreDocs) {
		Document doc = buscador.doc(sd.doc);
		System.out.println("Arquivo: " + doc.get("dataAtualizacao")
			+ " - " + doc.get("caminho"));
	    }
	    //
	    diretorio.close();
	    reader.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
