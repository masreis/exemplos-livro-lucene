package net.marcoreis.lucene.fragmentos;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThis;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class TesteMoreLikeThisPorDocumentID {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-01";
    private static final Logger logger = Logger
            .getLogger(TesteMoreLikeThisPorDocumentID.class);

    public static void main(String[] args) {
        new TesteMoreLikeThisPorDocumentID().buscarDocumentosSimilares();
    }

    public void buscarDocumentosSimilares() {
        try {
            Directory directory = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
            IndexReader ir = DirectoryReader.open(directory);
            IndexSearcher is = new IndexSearcher(ir);
            Analyzer analyzer = new StandardAnalyzer();
            //
            MoreLikeThis mlt = new MoreLikeThis(ir);
            mlt.setAnalyzer(analyzer);
            mlt.setFieldNames(new String[] { "conteudo" });
            //
            int documentoID = 0;
            QueryParser parser = new QueryParser("", analyzer);
            String nomeArquivo = "conteudo:(java ee 6 tutorial)";
            Query queryOrigem = parser.parse(nomeArquivo);
            TopDocs topdocsOrigem = is.search(queryOrigem, 1);
            if (topdocsOrigem.totalHits == 0) {
                throw new RuntimeException("Não encontrou nenhum documento.");
            }
            for (ScoreDoc sd : topdocsOrigem.scoreDocs) {
                Document doc = is.doc(sd.doc);
                logger.info("Documento base: " + doc.get("caminho"));
                documentoID = sd.doc;
            }
            //
            logger.info("Parâmetros utilizados para gerar a query: "
                    + mlt.describeParams());
            logger.info("Termos interessantes: ");
            String[] termosInteressantes = mlt
                    .retrieveInterestingTerms(documentoID);
            for (String termo : termosInteressantes) {
                logger.info(termo);
            }
            //
            Query query = mlt.like(documentoID);
            TopDocs topDocs = is.search(query, 10);
            logger.info("Documentos similares (" + topDocs.totalHits + "):");
            for (ScoreDoc sd : topDocs.scoreDocs) {
                Document doc = is.doc(sd.doc);
                logger.info(doc.get("caminho"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
