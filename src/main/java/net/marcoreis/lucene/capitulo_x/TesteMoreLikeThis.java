package net.marcoreis.lucene.capitulo_x;

import java.io.File;
import java.io.Reader;
import java.io.StringReader;

import org.apache.log4j.Logger;
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

public class TesteMoreLikeThis {

    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-01";
    private static final Logger logger = Logger
            .getLogger(TesteMoreLikeThis.class);

    public static void main(String[] args) {
        try {
            Directory directory = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader ir = DirectoryReader.open(directory);
            IndexSearcher is = new IndexSearcher(ir);
            Analyzer analyzer = new StandardAnalyzer();
            //
            MoreLikeThis mlt = new MoreLikeThis(ir);
            mlt.setMinDocFreq(0);
            mlt.setMinTermFreq(0);
            mlt.setBoost(true);
            mlt.setAnalyzer(analyzer);
            //
            mlt.setFieldNames(new String[] { "conteudo" });
            String textoBase = "java ee 6 tutorial";
            //
            Reader reader = new StringReader(textoBase);
            Query query = mlt.like(reader, "conteudo");
            TopDocs topDocs = is.search(query, 10);
            logger.info("Texto base: " + textoBase);
            logger.info("Documentos similares (" + topDocs.totalHits + "):");
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = is.doc(sd.doc);
                logger.info(doc.get("caminho"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
