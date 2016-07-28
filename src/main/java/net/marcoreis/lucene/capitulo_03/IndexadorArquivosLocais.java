package net.marcoreis.lucene.capitulo_03;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexadorArquivosLocais {
	// private static String DIRETORIO_DOCUMENTOS =
	// System.getProperty("user.home") + "/Dropbox/material-de-estudo/master";
	private static String DIRETORIO_DOCUMENTOS = System.getProperty("user.home") + "/Dropbox/";
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/somente-dropbox";
	// private static String DIRETORIO_INDICE = System.getProperty("user.home")
	// + "/livro-lucene/cursos";
	private static final Logger logger = Logger.getLogger(IndexadorArquivosLocais.class);
	private IndexWriter writer;
	private Tika extrator = new Tika();
	private boolean recursivo = false;

	@Before
	public void inicializar() throws IOException {
		FileUtils.deleteDirectory(new File(DIRETORIO_INDICE));
		Analyzer analyzer = new StandardAnalyzer();
		Directory diretorio = FSDirectory.open(Paths.get((DIRETORIO_INDICE)));
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		logger.info(conf.toString());
		writer = new IndexWriter(diretorio, conf);
	}

	@After
	public void finalizar() throws IOException {
		writer.close();
	}

	@Test
	public void indexar() throws IOException, TikaException {
		indexarDiretorio(new File(DIRETORIO_DOCUMENTOS));
		Assert.assertTrue(writer.numDocs() > 0);
	}

	/**
	 * Indexa os arquivos de um diretório raíz e seus subdiretórios.
	 * 
	 * @param diretorio
	 *            - diretório raíz que será indexado.
	 * @throws TikaException
	 * @throws IOException
	 * 
	 */
	private void indexarDiretorio(File diretorio) throws IOException, TikaException {
		File[] arquivosParaIndexar = diretorio.listFiles();
		for (File arquivo : arquivosParaIndexar) {
			if (arquivo.isDirectory()) {
				if (isRecursivo()) {
					indexarDiretorio(arquivo);
				}
			} else {
				indexarArquivo(arquivo);
			}
		}
	}

	private boolean isRecursivo() {
		return recursivo;
	}

	/**
	 * Indexa o arquivo informado no parâmetro. Utiliza o Apache Tika para fazer
	 * a extração dos dados.
	 * 
	 * @param arquivo
	 *            Arquivo binário que será indexado
	 * @throws TikaException
	 * @throws IOException
	 */
	private void indexarArquivo(File arquivo) {
		try {
			Document doc = new Document();
			Date dataModificacao = new Date(arquivo.lastModified());
			String dataParaIndexacao = DateTools.dateToString(dataModificacao, Resolution.DAY);
			String extensao = consultarExtensaoArquivo(arquivo.getName());
			String textoArquivo = extrator.parseToString(new FileInputStream(arquivo));
			//
			doc.add(new TextField("conteudo", textoArquivo, Store.YES));
			doc.add(new TextField("tamanho", String.valueOf(arquivo.length()), Store.YES));
			doc.add(new LongPoint("tamanhoLong", arquivo.length()));
			doc.add(new StringField("data", dataParaIndexacao, Store.YES));
			doc.add(new StringField("caminho", arquivo.getAbsolutePath(), Store.YES));
			doc.add(new StringField("extensao", extensao, Store.YES));
			writer.addDocument(doc);
			logger.info("Arquivo indexado (" + (arquivo.length() / 1024) + " kb): " + arquivo.getAbsolutePath());
		} catch (Exception e) {
			logger.error("Não foi possível processar o arquivo " + arquivo.getAbsolutePath());
			logger.error(e);
		}
	}

	private String consultarExtensaoArquivo(String nome) {
		int posicaoDoPonto = nome.lastIndexOf('.');
		if (posicaoDoPonto > 1) {
			return nome.substring(posicaoDoPonto + 1, nome.length()).toLowerCase();
		}
		return "";
	}

	public void indexarVetores() {
		// FieldType ft = new FieldType();
		// ft.setIndexed(true);
		// ft.setStored(true);
		// ft.setTokenized(true);
		// ft.setStoreTermVectors(true);
		// ft.setStoreTermVectorOffsets(true);
		// ft.setStoreTermVectorPositions(true);
		// ft.setStoreTermVectorPayloads(true);
		// ft.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
		// doc.add(new Field("conteudoComVetores", textoArquivo, ft));
		//
		// doc.add(new StringField("conteudoNaoAnalisado", StringUtils.left(
		// textoArquivo, 32000), Store.YES));
		//

	}
}
