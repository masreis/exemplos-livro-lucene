package net.marcoreis.lucene.capitulo_02;

import java.io.File;

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
import org.apache.lucene.util.Version;

public class BuscadorDadosDeputados {
    private static String diretorioIndice = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-03";
    private static final Logger logger = Logger
            .getLogger(BuscadorDadosDeputados.class);

    public static void main(String[] args) {
        BuscadorDadosDeputados buscador = new BuscadorDadosDeputados();
        String consulta = "";
        consulta = "comissao:(\"rio de janeiro\")";
        // consulta = "uf:rj";
        buscador.buscar(consulta);
    }

    public void buscar(String consulta) {
        try {
            Directory diretorio = FSDirectory.open(new File(diretorioIndice));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
            logger.info("Total de deputados indexados: " + reader.maxDoc());
            //
            QueryParser parser = new QueryParser(Version.LUCENE_48, "",
                    new StandardAnalyzer(Version.LUCENE_48));
            Query query = parser.parse(consulta);
            //
            TopDocs docs = buscador.search(query, 100);
            logger.info("Quantidade de itens: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info("==================");
                logger.info(doc.get("nome"));
                String[] comissoes = doc.getValues("comissao");
                for (String comissao : comissoes) {
                    // logger.info(comissao);
                }
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
