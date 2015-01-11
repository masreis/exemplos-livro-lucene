package net.marcoreis.lucene.capitulo_01;

import java.io.File;

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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorNomes {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-01";
    private static final Logger logger = Logger.getLogger(BuscadorNomes.class);

    public static void main(String[] args) {
        BuscadorNomes buscador = new BuscadorNomes();
        String consulta = "nome:(marco)";
        buscador.buscar(consulta);
    }

    public void buscar(String consulta) {
        try {
            Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
            //
            QueryParser parser = new QueryParser("", new StandardAnalyzer());
            Query query = parser.parse(consulta);
            //
            TopDocs docs = buscador.search(query, 100);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                Explanation explicacao = buscador.explain(query, sd.doc);
                logger.info(doc.get("nome"));
                // logger.info(explicacao.toString());
            }
            //
            logger.info("Quantidade de itens: " + docs.totalHits);
            diretorio.close();
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
