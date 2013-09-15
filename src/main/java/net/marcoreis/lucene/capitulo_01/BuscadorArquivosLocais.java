package net.marcoreis.lucene.capitulo_01;

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

public class BuscadorArquivosLocais {
  private static String diretorioIndice = System.getProperty("user.home")
      + "/livro-lucene/indice-capitulo-01";
  private static final Logger logger = Logger
      .getLogger(BuscadorArquivosLocais.class);

  public static void main(String[] args) {
    new BuscadorArquivosLocais().buscar("java");
  }

  public void buscar(String consulta) {
    try {
      Directory diretorio = FSDirectory.open(new File(diretorioIndice));
      IndexReader reader = DirectoryReader.open(diretorio);
      IndexSearcher buscador = new IndexSearcher(reader);
      //
      QueryParser parser = new QueryParser(Version.LUCENE_44, "",
          new StandardAnalyzer(Version.LUCENE_44));
      Query query = parser.parse("conteudo:(" + consulta + ")");
      TopDocs docs = buscador.search(query, 100);
      //
      for (ScoreDoc sd : docs.scoreDocs) {
        Document doc = buscador.doc(sd.doc);
        logger.info("Caminho: " + doc.get("caminho"));
      }
      //
      reader.close();
      diretorio.close();
    } catch (Exception e) {
      logger.error(e);
    }
  }
}
