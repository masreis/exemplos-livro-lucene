package net.marcoreis.lucene.fragmentos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
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
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexadorNomes {
    private static String ARQUIVO_ENTRADA = System.getProperty("user.home")
            + "/dados/partes/partes.csv";
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-0x-exemplo-0x";
    private static final Logger logger = Logger.getLogger(IndexadorNomes.class);
    private IndexWriter writer;

    @Before
    public void inicializar() throws IOException {
        FileUtils.deleteDirectory(new File(DIRETORIO_INDICE));
        Directory diretorio = FSDirectory.open(new File(DIRETORIO_INDICE));
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_4_10_3,
                analyzer);
        writer = new IndexWriter(diretorio, conf);
    }

    @After
    public void finalizar() throws IOException {
        writer.close();
    }

    @Test
    public void indexar() throws IOException, TikaException {
        BufferedReader br = new BufferedReader(new FileReader(new File(
                ARQUIVO_ENTRADA)));
        int contador = 0;
        String line;
        while ((line = br.readLine()) != null) {
            Document doc = new Document();
            String[] dados = line.split("\\^");
            if (!StringUtils.isEmpty(dados[27]))
                doc.add(new TextField("nomeMae", dados[27], Store.YES));
            if (!StringUtils.isEmpty(dados[28]))
                doc.add(new TextField("nomePai", dados[28], Store.YES));
            if (!StringUtils.isEmpty(dados[29]))
                doc.add(new TextField("nome", dados[29], Store.YES));
            writer.addDocument(doc);
            contador++;
        }
        logger.info("Total de itens: " + contador);
        br.close();
        Assert.assertTrue(writer.numDocs() > 0);
    }

}
