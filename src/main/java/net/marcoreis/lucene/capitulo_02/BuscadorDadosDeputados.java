package net.marcoreis.lucene.capitulo_02;

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

public class BuscadorDadosDeputados {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-02";
    private static final Logger logger = Logger
            .getLogger(BuscadorDadosDeputados.class);

    public static void main(String[] args) {
        BuscadorDadosDeputados buscador = new BuscadorDadosDeputados();
        String consulta = "";
        consulta = "comissao:(\"rio de janeiro\")";
        consulta = "nome:alves";
        consulta = "fone:(3215?5202)";
        consulta = "gabinete:543";
        consulta = "comissao:pais~1";
        consulta = "nome:lucio~1";
        consulta = "nome:wellington~";
        consulta = "nome:rafael~";
        consulta = "nome:artur~0";
        consulta = "nome:luis~1";
        consulta = "comissao:\"reforma proposta\"~10";
        buscador.buscar(consulta);
    }

    public void buscar(String consulta) {
        try {
            Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
            logger.info("Total de deputados indexados: " + reader.maxDoc());
            //
            QueryParser parser = new QueryParser("", new StandardAnalyzer());
            Query query = parser.parse(consulta);
            //
            TopDocs docs = buscador.search(query, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                Explanation explicacao = buscador.explain(query, sd.doc);
                // logger.info(explicacao.toString());
                logger.info(doc.get("nome"));
                String[] comissoes = doc.getValues("comissao");
                for (String comissao : comissoes) {
                    logger.info(comissao);
                }
            }
            //
            reader.close();
        } catch (Exception e) {
            logger.error(e);
        }
    }
}
