package net.marcoreis.lucene.capitulo_01;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.tika.Tika;

public class IndexadorArquivosLocais {
  private static String diretorioDocumentosLocais = System
      .getProperty("user.home") + "/Dropbox/entrada";
  private static String diretorioIndice = System.getProperty("user.home")
      + "/livro-lucene/indice-capitulo-01";
  private static final Logger logger = Logger
      .getLogger(IndexadorArquivosLocais.class);

  public static void main(String[] args) {
    new IndexadorArquivosLocais().processar();
  }

  public void processar() {
    try {
      Directory diretorio = FSDirectory.open(new File(diretorioIndice));
      Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
      IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_44,
          analyzer);
      conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
      IndexWriter writer = new IndexWriter(diretorio, conf);
      //
      Tika extrator = new Tika();
      File[] arquivosParaIndexar = new File(diretorioDocumentosLocais)
          .listFiles();
      for (File arquivo : arquivosParaIndexar) {
        if (!arquivo.isFile()) continue;
        try {
          Document doc = new Document();
          String textoArquivo = extrator.parseToString(new FileInputStream(
              arquivo));
          doc.add(new TextField("conteudo", textoArquivo, Store.YES));
          FieldType tipo = new FieldType();
          tipo.setIndexed(true);
          tipo.setStored(true);
          tipo.setTokenized(true);
          //doc.add(new Field("conteudo", textoArquivo, tipo ));
          doc.add(new StringField("tamanho", String.valueOf(arquivo.length()),
              Store.YES));
          doc.add(new StringField("nome", arquivo.getAbsolutePath(), Store.YES));
          doc.add(new StringField("dataAtualizacao", new Date(arquivo
              .lastModified()).toString(), Store.YES));
          doc.add(new StringField("caminho", arquivo.getAbsolutePath(),
              Store.YES));
          writer.addDocument(doc);
          logger.info("Arquivo indexado: " + arquivo.getAbsolutePath());
        } catch (Exception e) {
          logger.error("Não foi possível indexar o arquivo " + arquivo, e);
        }
      }
      writer.commit();
      writer.close();
      diretorio.close();
      logger.info("Índice gerado com sucesso");
    } catch (Exception e) {
      logger.error(e);
    }
  }
}
