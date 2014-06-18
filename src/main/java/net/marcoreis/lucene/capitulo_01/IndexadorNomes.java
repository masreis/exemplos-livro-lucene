package net.marcoreis.lucene.capitulo_01;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexadorNomes {
    private static String caminhoArquivo = System.getProperty("user.home")
            + "/Dropbox/entrada/nomes-teste.csv";
    private static String diretorioIndice = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-01";
    private static final Logger logger = Logger.getLogger(IndexadorNomes.class);
    private IndexWriter writer;

    public static void main(String[] args) {
        new IndexadorNomes().processar();
    }

    public void processar() {
        try {
            Directory diretorio = FSDirectory.open(new File(diretorioIndice));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
            IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_48,
                    analyzer);
            conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
            writer = new IndexWriter(diretorio, conf);
            //

            BufferedReader br = new BufferedReader(new FileReader(
                    caminhoArquivo));
            String linha = null;
            while ((linha = br.readLine()) != null) {
                Document doc = new Document();
                doc.add(new TextField("nome", linha, Store.YES));
                writer.addDocument(doc);
            }
            br.close();
            writer.close();
            logger.info("Nomes indexados com sucesso");
        } catch (Exception e) {
            logger.error(e);
        }
    }

}
