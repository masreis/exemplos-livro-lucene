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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class IndexadorArquivosLocais {
	private static final Logger logger = Logger
			.getLogger(IndexadorArquivosLocais.class);
	private IndexWriter writer;
	private Directory diretorio;
	private Tika extrator = new Tika();
	private boolean recursivo;
	private String diretorioIndice;
	private String diretorioDocumentos;
	private long totalArquivosIndexados;
	private long totalBytesIndexados;
	private boolean apagarIndice;

	public void inicializar() throws IOException {
		if (apagarIndice) {
			FileUtils.deleteDirectory(
					new File(diretorioIndice));
		}
		Analyzer analyzer = new StandardAnalyzer();
		diretorio = FSDirectory
				.open(Paths.get((diretorioIndice)));
		IndexWriterConfig conf = new IndexWriterConfig(
				analyzer);
		// logger.info(conf.toString());
		writer = new IndexWriter(diretorio, conf);
	}

	public void finalizar() {
		try {
			writer.close();
			diretorio.close();
			//
			logger.info("Total de arquivos indexados: "
					+ totalArquivosIndexados);
			logger.info("Total de bytes indexados (MB): "
					+ totalBytesIndexados / (1024 * 1024));
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void indexar()
			throws IOException, TikaException {
		indexarDiretorio(new File(diretorioDocumentos));
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
	public void indexarDiretorio(File diretorio)
			throws IOException, TikaException {
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
	public void indexarArquivo(File arquivo) {
		try {
			Document doc = new Document();
			Date dataModificacao = new Date(
					arquivo.lastModified());
			String dataParaIndexacao = DateTools
					.dateToString(dataModificacao,
							Resolution.DAY);
			String extensao = consultarExtensaoArquivo(
					arquivo.getName());
			String textoArquivo = extrator.parseToString(
					new FileInputStream(arquivo));
			// BEGIN Implementado no capítulo 4
			int tamanhoMaximo = 30000;
			if (textoArquivo.length() >= tamanhoMaximo) {
				doc.add(new StringField(
						"conteudoNaoAnalisado",
						textoArquivo.substring(0,
								tamanhoMaximo),
						Store.YES));
			} else {
				doc.add(new StringField(
						"conteudoNaoAnalisado",
						textoArquivo, Store.YES));
			}
			// doc.add(criarCampoComPosicoes("conteudoComPosicoes",
			// textoArquivo));
			//
			// END
			doc.add(new TextField("conteudo", textoArquivo,
					Store.YES));
			doc.add(new TextField("tamanho",
					String.valueOf(arquivo.length()),
					Store.YES));
			doc.add(new LongPoint("tamanhoLong",
					arquivo.length()));
			doc.add(new StringField("data",
					dataParaIndexacao, Store.YES));
			doc.add(new StringField("caminho",
					arquivo.getAbsolutePath(), Store.YES));
			doc.add(new StringField("extensao", extensao,
					Store.YES));
			writer.addDocument(doc);
			logger.info("Arquivo indexado ("
					+ (arquivo.length() / 1024) + " kb): "
					+ arquivo);
			totalArquivosIndexados++;
			totalBytesIndexados += arquivo.length();
		} catch (Exception e) {
			logger.error(
					"Não foi possível processar o arquivo "
							+ arquivo.getAbsolutePath());
			logger.error(e);
		}
	}

	private IndexableField criarCampoComPosicoes(
			String campo, String textoArquivo) {
		IndexOptions opts = IndexOptions.DOCS_AND_FREQS_AND_POSITIONS;
		FieldType campoComPosicoes = new FieldType();
		campoComPosicoes.setIndexOptions(opts);
		campoComPosicoes.setStored(true);
		campoComPosicoes.setTokenized(true);
		return new Field(campo, textoArquivo,
				campoComPosicoes);
	}

	// private String converterCaminhoArquivo(File arquivo) {
	// return arquivo.getAbsolutePath().replaceAll("/", "_").replaceAll(":",
	// "_").replaceAll(" ", "_").replaceAll("-",
	// "_");
	// }

	private String consultarExtensaoArquivo(String nome) {
		int posicaoDoPonto = nome.lastIndexOf('.');
		if (posicaoDoPonto > 1) {
			return nome.substring(posicaoDoPonto + 1,
					nome.length()).toLowerCase();
		}
		return "";
	}

	public void setApagarIndice(boolean apagarIndice) {
		this.apagarIndice = apagarIndice;
	}

	public void setDiretorioIndice(String diretorioIndice) {
		this.diretorioIndice = diretorioIndice;
	}

	public void setRecursivo(boolean recursivo) {
		this.recursivo = recursivo;
	}

	public void setDiretorioDocumentos(
			String diretorioDocumentos) {
		this.diretorioDocumentos = diretorioDocumentos;
	}
}
