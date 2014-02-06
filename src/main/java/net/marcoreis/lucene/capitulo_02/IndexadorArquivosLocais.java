package net.marcoreis.lucene.capitulo_02;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
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
	    + "/livro-lucene/indice-capitulo-02";
    private static final Logger logger = Logger
	    .getLogger(IndexadorArquivosLocais.class);
    private IndexWriter writer;
    private Tika extrator = new Tika();

    public static void main(String[] args) {
	new IndexadorArquivosLocais().processar();
    }

    public void processar() {
	try {
	    Directory diretorio = FSDirectory.open(new File(diretorioIndice));
	    Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_46);
	    IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_46,
		    analyzer);
	    conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
	    writer = new IndexWriter(diretorio, conf);
	    //
	    processarDiretorio(new File(diretorioDocumentosLocais));
	    writer.close();
	    logger.info("Indice gerado com sucesso");
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    /**
     * Indexa os arquivos de um diretório raíz e seus subdiretórios.
     * 
     * @param diretorio
     *            - diretório raíz que será indexado.
     * 
     */
    private void processarDiretorio(File diretorio) {
	File[] arquivosParaIndexar = diretorio.listFiles();
	for (File arquivo : arquivosParaIndexar) {
	    if (arquivo.isDirectory()) {
		processarDiretorio(arquivo);
	    } else if (arquivo.isFile()) {
		indexarArquivo(arquivo);
	    }
	}
    }

    /**
     * Indexa o arquivo informado no parâmetro. Utiliza o Apache Tika para fazer
     * a extração dos dados.
     * 
     * @param arquivo
     *            Arquivo binário que será indexado
     */
    private void indexarArquivo(File arquivo) {
	try {
	    logger.info("Arquivo indexado(" + (arquivo.length() / 1024)
		    + " kb): " + arquivo.getAbsolutePath());
	    Document doc = new Document();
	    FieldType tipo = new FieldType();
	    tipo.setIndexed(true);
	    tipo.setStored(true);
	    tipo.setTokenized(true);
	    //
	    Date dataAtualizacao = new Date(arquivo.lastModified());
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    String dataFormatada = sdf.format(dataAtualizacao);
	    String textoArquivo = extrator.parseToString(new FileInputStream(
		    arquivo));
	    //
	    doc.add(new Field("conteudo", textoArquivo, tipo));
	    doc.add(new Field("tamanho", String.valueOf(arquivo.length()), tipo));
	    doc.add(new Field("dataAtualizacao", dataFormatada, tipo));
	    doc.add(new Field("caminho", arquivo.getAbsolutePath(), tipo));
	    doc.add(new Field("nome", arquivo.getName(), tipo));
	    writer.addDocument(doc);
	} catch (Exception e) {
	    logger.error("Nao foi possivel indexar o arquivo " + arquivo, e);
	}
    }
}
