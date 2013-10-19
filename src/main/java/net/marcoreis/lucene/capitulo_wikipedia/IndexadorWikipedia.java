package net.marcoreis.lucene.capitulo_wikipedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.Version;

public class IndexadorWikipedia {
  private static Logger logger = Logger.getLogger(IndexadorWikipedia.class);
  private IndexWriter writer;
  private String diretorioIndice;
  int quantidadeArquivosIndexados = 0;
  private UtilBusca buscador;

  public IndexadorWikipedia(String diretorioIndice) {
    try {
      this.diretorioIndice = diretorioIndice;
      File file = new File(diretorioIndice);
      if (!file.exists()) {
        file.mkdirs();
      }
      Directory d = new NIOFSDirectory(file);
      logger.info("Diretorio do indice: " + diretorioIndice);
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
      IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_44,
          analyzer);
      writer = new IndexWriter(d, config);
    } catch (Exception e) {
      logger.error(e);
    }
  }

  private Set<String> getStopWords() {
    try {
      URL resource = getClass().getClassLoader().getResource("stopwords.txt");
      BufferedReader br = new BufferedReader(new InputStreamReader(
          new FileInputStream(new File(resource.toURI()))));
      String linha;
      Set<String> stopWords = new HashSet<String>();
      while ((linha = br.readLine()) != null) {
        stopWords.add(linha);
      }
      return stopWords;
    } catch (Exception e) {
    }
    return null;
  }

  private boolean jahIndexado(String id) {
    try {
      TopDocs hits = getBuscador().busca("id:" + id);
      int qtd = hits.totalHits;
      return qtd > 0;
    } catch (Exception e) {
      return false;
    }
  }

  private UtilBusca getBuscador() throws IOException {
    if (buscador == null) {
      buscador = new UtilBusca(diretorioIndice);
    }
    return buscador;
  }

  public void fecha() throws CorruptIndexException, IOException {
    writer.close();
  }

  public boolean indexar(Map<String, String> valores) {
    Document documento = new Document();
    if (jahIndexado(valores.get("id"))) return false;
    try {
      for (String coluna : valores.keySet()) {
        String valor = valores.get(coluna);
        valor = Normalizer.normalize(valor, Normalizer.Form.NFD);
        FieldType tipo = new FieldType();
        tipo.setIndexed(true);
        tipo.setStored(true);
        tipo.setTokenized(true);
        documento.add(new Field(coluna, valor, tipo));
      }
      writer.addDocument(documento);
      return true;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void commit() throws IOException {
    writer.commit();
  }

  public static void main(String[] args) throws IOException {
    String arquivo = "/Users/marcoreis/Documents/teste.txt";
    FileReader fileReader = new FileReader(arquivo);
    BufferedReader bufferedReader = new BufferedReader(fileReader);
    String linha;
    while ((linha = bufferedReader.readLine()) != null) {
      System.out.println(Normalizer.normalize(linha, Normalizer.Form.NFD));
    }
    bufferedReader.close();
  }

  public int getQuantidadeArquivosIndexados() {
    return quantidadeArquivosIndexados;
  }
}
