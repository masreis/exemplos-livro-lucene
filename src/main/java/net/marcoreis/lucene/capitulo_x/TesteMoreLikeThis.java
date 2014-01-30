package net.marcoreis.lucene.capitulo_x;

import java.io.File;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TesteMoreLikeThis {

    public static void main(String[] args) {
	try {
	    String diretorioIndice = "/home/marco/livro-lucene/indice-wikipedia";
	    diretorioIndice = "/home/marco/livro-lucene/indice-capitulo-02";
	    Directory directory = FSDirectory.open(new File(diretorioIndice));
	    IndexReader ir = DirectoryReader.open(directory);
	    IndexSearcher is = new IndexSearcher(ir);
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
	    MoreLikeThis mlt = new MoreLikeThis(ir);
	    mlt.setMinDocFreq(0);
	    mlt.setMinTermFreq(0);
	    mlt.setBoost(true);
	    mlt.setAnalyzer(analyzer);
	    mlt.setFieldNames(new String[] { "conteudo" });
	    String textoBase = "educação";
	    StringReader sr = new StringReader(textoBase);
	    Query query = mlt.like(sr, null);
	    TopDocs topdocs = is.search(query, 10);
	    System.out.println("Texto base: " + textoBase);
	    System.out.println("Artigos similares:");
	    for (ScoreDoc sd : topdocs.scoreDocs) {
		Document doc = is.doc(sd.doc);
		System.out.println(doc.get("caminho"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
