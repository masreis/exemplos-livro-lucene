package net.marcoreis.lucene.capitulo_02;

import java.nio.file.Paths;

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

public class BuscadorArquivosLocais {
    // private static String DIRETORIO_INDICE = System.getProperty("user.home")
    // + "/livro-lucene/cursos";
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/treinamento/aulas-concursos";
    private static final Logger logger = Logger
            .getLogger(BuscadorArquivosLocais.class);

    public static void main(String[] args) {
        BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
        String consulta = "";
        consulta = "dataAtualizacao:[2012 TO 2013]";
        consulta = "conteudo:rafael~2";
        consulta = "dataAtualizacao:[2014-05-01 TO 2014-05-30]";
        consulta = "conteudo:(rede social)";
        consulta = "conteudo:a*";
        consulta = "conteudo:itext";
        consulta = "conteudoComVetores:u7ddle2941splce2rnrna";
        consulta = "conteudo:(ssh AND integrator)";
        consulta = "conteudo:(orientação a objetos)";
        consulta = "conteudo:\"soapui\"";
        // consulta = "conteudo:(\"instituto quadrix\")";
        // consulta = "conteudo:zope";
//        consulta = "conteudo:(+forms +apex)";
//        consulta = "nome:plano";
        buscador.buscar(consulta);
    }

    public void buscar(String consulta) {
        try {
            Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
            IndexReader reader = DirectoryReader.open(diretorio);
            IndexSearcher buscador = new IndexSearcher(reader);
            //
            // Query
            QueryParser parser = new QueryParser("", new StandardAnalyzer());
            Query query = parser.parse(consulta);
            //
            TopDocs docs = buscador.search(query, 100);
            logger.info("Quantidade de itens encontrados: " + docs.totalHits);
            for (ScoreDoc sd : docs.scoreDocs) {
                Document doc = buscador.doc(sd.doc);
                logger.info(doc.get("conteudoStored"));
                logger.info("Arquivo: " + doc.get("nome"));
                logger.info("Caminho: " + doc.get("caminho"));
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
