package net.marcoreis.lucene.capitulo_01;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexadorNomes {
    private static InputStream caminhoArquivo = IndexadorNomes.class
            .getClassLoader().getResourceAsStream("dados/nomes-teste.csv");
    private static String diretorioIndice = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-01";
    private IndexWriter writer;

    @Before
    public void inicializar() throws IOException {
        Directory diretorio = FSDirectory.open(new File(diretorioIndice));
        Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_48);
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_48,
                analyzer);
        writer = new IndexWriter(diretorio, conf);
    }

    @After
    public void finalizar() throws IOException {
        writer.close();
    }

    @Test
    public void indexar() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                caminhoArquivo));
        String linha = null;
        while ((linha = br.readLine()) != null) {
            Document doc = new Document();
            doc.add(new TextField("nome", linha, Store.YES));
            writer.addDocument(doc);
        }
        br.close();
        Assert.assertTrue(writer.numDocs() > 0);
    }

}
