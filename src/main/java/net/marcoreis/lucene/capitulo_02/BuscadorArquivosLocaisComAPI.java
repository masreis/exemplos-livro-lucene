package net.marcoreis.lucene.capitulo_02;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
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
        buscador.buscarPhraseQuery();
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

    public void buscarPhraseQuery() {
        try {
            Term termo1 = new Term("conteudo", "aplicação");
            Term termo2 = new Term("conteudo", "exemplo");
            PhraseQuery pq = new PhraseQuery();
            pq.add(termo1);
            pq.add(termo2);
            pq.setSlop(1);
            TopDocs docs = buscador.search(pq, 100);
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

    public void buscarMultiPhraseQuery() {
        try {
            Term termo1 = new Term("conteudo", "aplicação");
            Term termo2 = new Term("conteudo", "exemplo");
            MultiPhraseQuery mpq = new MultiPhraseQuery();
            
            pq.add(termo2);
            pq.setSlop(1);
            TopDocs docs = buscador.search(pq, 100);
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

    public void buscarPrefixQuery() {
        try {
            Term termo = new Term("conteudo", "consider");
            PrefixQuery pq = new PrefixQuery(termo);
            TopDocs docs = buscador.search(pq, 100);
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

    public void buscarSpanQuery() {
        try {
            Term termo = new Term("conteudo", "consider");
            SpanQuery query1;
            SpanQuery[] clausulas;
            SpanNearQuery sq = new SpanNearQuery(clausulas, 2, true);
            TopDocs docs = buscador.search(sq, 100);
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

    public void buscarBooleanQuery() {
        try {
            BooleanQuery bq = new BooleanQuery();
            TermQuery tq = new TermQuery(new Term("conteudo", "java"));
            PhraseQuery pq = new PhraseQuery();
            pq.add(new Term("conteudo", "rede"));
            pq.add(new Term("conteudo", "social"));
            //
            bq.add(tq, Occur.MUST);
            bq.add(pq, Occur.MUST_NOT);
            //
            TopDocs docs = buscador.search(bq, 100);
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
