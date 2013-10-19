package net.marcoreis.lucene.capitulo_02;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorComAPIArquivosLocais {
    private static String diretorioIndice = System.getProperty("user.home")
	    + "/livro-lucene/indice-capitulo-02";
    private static final Logger logger = Logger
	    .getLogger(BuscadorArquivosLocais.class);

    public static void main(String[] args) {
	new BuscadorComAPIArquivosLocais().buscar();
    }

    public void buscar() {
	try {
	    Directory diretorio = FSDirectory.open(new File(diretorioIndice));
	    IndexReader reader = DirectoryReader.open(diretorio);
	    IndexSearcher buscador = new IndexSearcher(reader);
	    //
	    BooleanQuery query = new BooleanQuery();
	    query.add(new TermQuery(new Term("conteudo", "java")), Occur.MUST);
	    query.add(new TermQuery(new Term("dataAtualizacao", "2013")),
		    Occur.MUST);
	    TopDocs docs = buscador.search(query, 100);
	    //
	    for (ScoreDoc sd : docs.scoreDocs) {
		Document doc = buscador.doc(sd.doc);
		System.out.println("Arquivo: " + doc.get("dataAtualizacao")
			+ " - " + doc.get("caminho"));
	    }
	    //
	    reader.close();
	    diretorio.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
