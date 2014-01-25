package net.marcoreis.lucene.capitulo_x;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class TesteMoreLikeThisPorID {
    public static void main(String[] args) {
	Directory directory;
	try {
	    // directory = FSDirectory.open(new
	    // File(Constantes.DIRETORIO_INDICE));
	    directory = FSDirectory.open(new File(
		    "/home/marco/livro-lucene/indice-wikipedia"));
	    IndexReader ir = DirectoryReader.open(directory);
	    IndexSearcher is = new IndexSearcher(ir);
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
	    String likeText = "programador";
	    String[] moreLikeFields = new String[] { "title", "text" };
	    String fieldName = "title";
	    MoreLikeThisQuery query = new MoreLikeThisQuery(likeText,
		    moreLikeFields, analyzer, fieldName);
	    TopDocs topdocs = is.search(query, 100);
	    for (ScoreDoc sd : topdocs.scoreDocs) {
		Document doc = is.doc(sd.doc);
		System.out.println(doc.get("title"));
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
