package net.marcoreis.lucene.fragmentos;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class FastHighlighterTeste {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-01";
    private static final Logger logger = Logger
            .getLogger(FastHighlighterTeste.class);
    private static final String CAMPO_COM_VETORES = "conteudoComVetores";

    public static void main(String[] args) {
        FastHighlighterTeste buscador = new FastHighlighterTeste();
        buscador.highlightFuzzyQuery();
    }

    public void highlight(Query query) {
        try {
            Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
            //
            TopDocs docs = buscador.search(query, 100);
            logger.info("Query: " + query);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            FastVectorHighlighter fhl = new FastVectorHighlighter();
            FieldQuery fq = fhl.getFieldQuery(query);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                String fragmentos = fhl.getBestFragment(fq, reader, sd.doc,
                        CAMPO_COM_VETORES, 30);
                logger.info(fragmentos);
                // Explanation explicacao = buscador.explain(query, sd.doc);
                // logger.info(explicacao.toString());
                logger.info(doc.get("nome"));
                logger.info(doc.get("dataAtualizacao"));
                // String[] comissoes = doc.getValues("comissao");
                // for (String comissao : comissoes) {
                // logger.info(comissao);
                // }
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void highlightQueryParser() {
        try {
            String consulta = "";
            consulta = "nome:josé";
            consulta = "comissao:(comissão AND lei)";
            consulta = "comissao:\"reforma proposta\"~10";
            consulta = "conteudo:\"(aplicação exemplo) OR (banco dados)\"~1";
            consulta = "conteudo:idioma AND conteudo:java ";
            consulta = "conteudo:(+java +cdi) AND dataAtualizacao:[2013-01-01 TO 2014-12-31]";
            consulta = "conteudo:/[vc]alor/";
            consulta = "conteudoComVetores:manuel~1";
            consulta = "conteudoComVetores:aplicação";
            QueryParser qp = new QueryParser(Version.LUCENE_48, "",
                    new StandardAnalyzer(Version.LUCENE_48));
            Query query = qp.parse(consulta);
            highlight(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void highlightMultiPhraseQuery() {
        try {
            //
            Term[] termosAplicacaoExemplo = new Term[] {
                    new Term("conteudoComVetores", "aplicação"),
                    new Term("conteudoComVetores", "exemplo") };
            Term[] termosBancoDados = new Term[] {
                    new Term("conteudoComVetores", "banco"),
                    new Term("conteudoComVetores", "dados") };
            //
            MultiPhraseQuery mpq = new MultiPhraseQuery();
            mpq.add(termosAplicacaoExemplo);
            mpq.add(termosBancoDados);
            mpq.setSlop(2);
            //
            highlight(mpq);
            //
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void highlightRegexQuery() {
        try {
            //
            String regex = ".*ler\\scódigo.*";
            regex = ".*http[s].*";
            regex = ".*[0-9]{4}[^0-9][0-9]{4}.*";
            regex = ".*[0-9]{2}:[0-9]{2},847.*";
            regex = "bug(\\s){0,1}[0-9]{4}";
            regex = "[a-z]{3}\\|[0-9]{4}";
            // Term termo = new Term("conteudo", regex);
            String sQuery = "conteudo:/[0-9]{4}/";
            sQuery = "conteudo:/8.../";
            Query rq = null;
            rq = new QueryParser(Version.LUCENE_48, "", new StandardAnalyzer(
                    Version.LUCENE_48)).parse(sQuery);
            // rq = new RegexpQuery(termo);
            // RegexQuery rq = new RegexQuery(termo);
            highlight(rq);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void highlightFuzzyQuery() {
        try {
            //
            Term termo = new Term("conteudoComVetores", "seção");
            FuzzyQuery fq = new FuzzyQuery(termo, 1, 1);
            //
            highlight(fq);
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
