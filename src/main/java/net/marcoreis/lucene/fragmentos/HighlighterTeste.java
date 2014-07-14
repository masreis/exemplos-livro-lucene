package net.marcoreis.lucene.fragmentos;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
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

public class HighlighterTeste {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-01";
    private static final Logger logger = Logger
            .getLogger(HighlighterTeste.class);

    public static void main(String[] args) {
        HighlighterTeste buscador = new HighlighterTeste();
        String consulta = "";
        consulta = "nome:josé";
        consulta = "comissao:(comissão AND lei)";
        consulta = "comissao:\"reforma proposta\"~10";
        consulta = "conteudo:\"(aplicação exemplo) OR (banco dados)\"~1";
        consulta = "conteudoComVetores:aplicação";
        consulta = "conteudo:idioma AND conteudo:java ";
        consulta = "conteudo:(+java +cdi) AND dataAtualizacao:[2013-01-01 TO 2014-12-31]";
        consulta = "conteudo:/[vc]alor/";
        buscador.buscar(consulta);
        // buscador.buscarMultiPhraseQuery();
        // buscador.buscarRegexQuery();
    }

    public void buscar(String consulta) {
        try {
            Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
            QueryParser parser = new QueryParser(Version.LUCENE_48, "",
                    new StandardAnalyzer(Version.LUCENE_48));
            Query query = parser.parse(consulta);
            //
            TopDocs docs = buscador.search(query, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            logger.info(query);
            FastVectorHighlighter fhl = new FastVectorHighlighter();
            FieldQuery fq = fhl.getFieldQuery(query);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                String fragmentos = fhl.getBestFragment(fq, reader, sd.doc,
                        "conteudoComVetores", 100);
                // System.out.println(fragmentos);
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

    public void buscarMultiPhraseQuery() {
        try {
            Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
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
            TopDocs docs = buscador.search(mpq, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            FastVectorHighlighter fhl = new FastVectorHighlighter();
            FieldQuery fq = fhl.getFieldQuery(mpq);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                String fragmentos = fhl.getBestFragment(fq, reader, sd.doc,
                        "conteudoComVetores", 100);
                logger.info(fragmentos);
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }

    private void buscarRegexQuery() {
        try {
            Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
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
            logger.info(rq);
            FastVectorHighlighter fhl = new FastVectorHighlighter();
            FieldQuery fq = fhl.getFieldQuery(rq);
            TopDocs docs = buscador.search(rq, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info("Arquivo: " + doc.get("nome"));
                String fragmentos = fhl.getBestFragment(fq, reader, sd.doc,
                        "conteudoComVetores", 100);
                logger.info(fragmentos);
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }

    }

}
