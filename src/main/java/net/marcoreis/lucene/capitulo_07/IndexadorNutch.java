package net.marcoreis.lucene.capitulo_07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import net.marcoreis.lucene.capitulo_03.IndexadorArquivosLocais;

public class IndexadorNutch {
	private static final Logger logger =
			Logger.getLogger(IndexadorArquivosLocais.class);
	protected IndexWriter writer;
	private Directory diretorio;
	protected Tika extrator = new Tika();
	private String diretorioIndice;
	private String diretorioSegmento;
	protected long totalUrlsIndexadas;
	private boolean apagarIndice;

	public void inicializar() throws IOException {
		if (apagarIndice) {
			FileUtils.deleteDirectory(new File(diretorioIndice));
		}
		Analyzer analyzer = new StandardAnalyzer();
		diretorio = FSDirectory.open(Paths.get(diretorioIndice));
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(diretorio, conf);
	}

	public void finalizar() {
		try {
			writer.close();
			diretorio.close();
			//
			logger.info("Total de Urls indexadas: "
					+ totalUrlsIndexadas);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void indexar() throws IOException, TikaException {
		indexarSegmento(new File(diretorioSegmento));
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
	public void indexarSegmento(File diretorio)
			throws IOException, TikaException {
		String nomeArquivoDump = "dump";
		File arquivo = new File(diretorio, nomeArquivoDump);
		indexarArquivo(arquivo);
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
			Date dataModificacao =
					new Date(arquivo.lastModified());
			String dataParaIndexacao = DateTools.dateToString(
					dataModificacao, Resolution.DAY);
			String extensao =
					consultarExtensaoArquivo(arquivo.getName());
			String textoArquivo = "";
			//
			InputStream is = new FileInputStream(arquivo);
			try {
				textoArquivo = extrator.parseToString(is);
			} catch (Throwable e) {
				logger.error(e);
			} finally {
				is.close();
			}
			String[] urls = textoArquivo.split("Recno::");
			for(String conteudo:urls) {
				if(conteudo.contains("ParseText::")) {
					System.out.println(conteudo);
				}
			}
			doc.add(new TextField("conteudo", textoArquivo,
					Store.YES));
			doc.add(new TextField("tamanho",
					String.valueOf(arquivo.length()),
					Store.YES));
			doc.add(new LongPoint("tamanhoLong",
					arquivo.length()));
			doc.add(new StringField("data", dataParaIndexacao,
					Store.YES));
			doc.add(new StringField("caminho",
					arquivo.getAbsolutePath(), Store.YES));
			doc.add(new StringField("extensao", extensao,
					Store.YES));
			writer.addDocument(doc);
			logger.info("Arquivo indexado ("
					+ (arquivo.length() / 1024) + " kb): "
					+ arquivo);
			totalUrlsIndexadas++;
		} catch (Exception e) {
			logger.error("Não foi possível processar o arquivo "
					+ arquivo.getAbsolutePath());
			logger.error(e);
		}
	}

	protected String consultarExtensaoArquivo(String nome) {
		int posicaoDoPonto = nome.lastIndexOf('.');
		if (posicaoDoPonto > 1) {
			return nome
					.substring(posicaoDoPonto + 1, nome.length())
					.toLowerCase();
		}
		return "";
	}

	public void setApagarIndice(boolean apagarIndice) {
		this.apagarIndice = apagarIndice;
	}

	public void setDiretorioIndice(String diretorioIndice) {
		this.diretorioIndice = diretorioIndice;
	}

	public void setDiretorioSegmento(String diretorioSegmento) {
		this.diretorioSegmento = diretorioSegmento;
	}

}
