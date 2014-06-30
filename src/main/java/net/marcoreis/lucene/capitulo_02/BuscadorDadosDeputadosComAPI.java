package net.marcoreis.lucene.capitulo_02;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class BuscadorDadosDeputadosComAPI {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-02";
    private static final Logger logger = Logger
            .getLogger(BuscadorDadosDeputadosComAPI.class);

    //
    private Directory diretorio;
    private IndexReader reader;
    private IndexSearcher buscador;

    //
    public static void main(String[] args) throws IOException {
        BuscadorDadosDeputadosComAPI buscador = new BuscadorDadosDeputadosComAPI();
        buscador.buscarTermQueryUF();
    }

    public BuscadorDadosDeputadosComAPI() throws IOException {
        diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
        reader = DirectoryReader.open(diretorio);
        buscador = new IndexSearcher(reader);
    }

    public void buscarTermQueryUF() {
        try {
            Term termo = new Term("uf", "DF");
            TermQuery query = new TermQuery(termo);
            TopDocs docs = buscador.search(query, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info(doc.get("nome"));
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void buscarPhraseQuery() {
        try {
            Term termo = new Term("comissao", "saúde");
            
            PhraseQuery query = new PhraseQuery();
            query.add(termo);
            TopDocs docs = buscador.search(query, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info(doc.get("nome"));
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
