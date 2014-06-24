package net.marcoreis.lucene.capitulo_02;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorArquivosLocaisComAPI {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-01";
    private static final Logger logger = Logger
            .getLogger(BuscadorArquivosLocaisComAPI.class);
    private static final Long UM_MEGA = 1000 * 1000L;
    private static final Long DOIS_MEGAS = 1000 * 1000 * 2L;

    //
    private Directory diretorio;
    private IndexReader reader;
    private IndexSearcher buscador;

    //
    public static void main(String[] args) throws IOException {
        BuscadorArquivosLocaisComAPI buscador = new BuscadorArquivosLocaisComAPI();
        buscador.buscarTermQuery();
    }

    public BuscadorArquivosLocaisComAPI() throws IOException {
        diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
        reader = DirectoryReader.open(diretorio);
        buscador = new IndexSearcher(reader);
    }

    public void buscarCampoNumerico() {
        try {
            NumericRangeQuery<Long> nrq = NumericRangeQuery.newLongRange(
                    "tamanho", UM_MEGA, DOIS_MEGAS, true, true);
            TopDocs docs = buscador.search(nrq, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info("Arquivo: " + doc.get("nome"));
                logger.info("Tamanho: " + doc.get("tamanho"));
                logger.info("Atualização: " + doc.get("dataAtualizacao"));
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void buscarTermQuery() {
        try {
            Term termo = new Term("conteudo", "java");
            TermQuery tq = new TermQuery(termo);
            TopDocs docs = buscador.search(tq, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info("Arquivo: " + doc.get("nome"));
                logger.info("Tamanho: " + doc.get("tamanho"));
                logger.info("Atualização: " + doc.get("dataAtualizacao"));
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
