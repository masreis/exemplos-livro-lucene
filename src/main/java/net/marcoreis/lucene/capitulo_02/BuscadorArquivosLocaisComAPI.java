package net.marcoreis.lucene.capitulo_02;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
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
        buscador.buscarFuzzyQuery();
    }

    private void buscarRegexQuery() {
        try {
            String regex = ".*ler\\scódigo.*";
            regex = ".*http[s].*";
            regex = ".*\\d{5},.\\d{4}.*";
            regex = "\\d{4}.\\d{2}.\\d{2}";
            regex = ".*\\d{4}.\\d{2}.\\d{2}.*";
            regex = ".*[0-9]{4}.*";
            Term termo = new Term("conteudo", regex);
            // RegexQuery rq = new RegexQuery(termo);
            RegexpQuery rq = new RegexpQuery(termo);
            // rq.setRegexImplementation(new JakartaRegexpCapabilities());
            TopDocs docs = buscador.search(rq, 100);
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

    private void buscarMatchAllDocs() {
        try {
            MatchAllDocsQuery all = new MatchAllDocsQuery();
            TopDocs docs = buscador.search(all, 100);
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
            Term termoAplicacao = new Term("conteudoComVetores", "aplicação");
            Term termoExemplo = new Term("conteudo", "exemplo");
            PhraseQuery pq = new PhraseQuery();
            pq.add(termoAplicacao);
            pq.add(termoExemplo);
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
            Term[] termosAplicacaoExemplo = new Term[] {
                    new Term("conteudo", "aplicação"),
                    new Term("conteudo", "exemplo") };
            Term[] termosBancoDados = new Term[] {
                    new Term("conteudo", "banco"),
                    new Term("conteudo", "dados") };
            //
            MultiPhraseQuery mpq = new MultiPhraseQuery();
            mpq.add(termosAplicacaoExemplo);
            mpq.add(termosBancoDados);
            mpq.setSlop(2);
            //
            TopDocs docs = buscador.search(mpq, 100);
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
            SpanQuery[] clausulas = null;
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
            TermQuery tq = new TermQuery(new Term("conteudo", "calor"));
            TermQuery tq1 = new TermQuery(new Term("conteudo", "java"));
            PhraseQuery pq = new PhraseQuery();
            pq.add(new Term("conteudo", "rede"));
            pq.add(new Term("conteudo", "social"));
            //
            //
            bq.add(tq, Occur.MUST);
            bq.add(tq1, Occur.MUST);
            // bq.add(new TermQuery(new Term("conteudo", "drools")),
            // Occur.MUST);
            // bq.add(pq, Occur.MUST_NOT);
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

    public void buscarWildcardQuery() {
        try {
            Term termo = new Term("conteudo", "calor");
            WildcardQuery wq = new WildcardQuery(termo);
            //
            TopDocs docs = buscador.search(wq, 100);
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

    public void buscarFuzzyQuery() {
        try {
            Term termo = new Term("conteudo", "seção");
            FuzzyQuery fq = new FuzzyQuery(termo, 2);
            logger.info("Query: " + fq);
            //
            TopDocs docs = buscador.search(fq, 100);
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
