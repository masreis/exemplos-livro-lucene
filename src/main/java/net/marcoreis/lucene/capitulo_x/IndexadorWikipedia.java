package net.marcoreis.lucene.capitulo_x;

import java.io.File;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.benchmark.byTask.feeds.DocMaker;
import org.apache.lucene.benchmark.byTask.feeds.EnwikiContentSource;
import org.apache.lucene.benchmark.byTask.utils.Config;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.store.FSDirectory;

public class IndexadorWikipedia {
	private File arquivoWikipedia;
	private String diretorioSaida;
	private static Logger logger = Logger.getLogger(IndexadorWikipedia.class);

	public static void main(String[] args) throws Exception {
		String caminhoWikipedia = "/home/marco/Downloads/ptwiki-20160720-pages-articles-multistream.xml";
		String diretorioSaida = "/home/marco/livro-lucene/indice-wikipedia";
		IndexadorWikipedia indexador = new IndexadorWikipedia(caminhoWikipedia, diretorioSaida);
		indexador.indexar();
	}

	public IndexadorWikipedia(String caminhoWikipedia, String diretorioSaida) {
		this.arquivoWikipedia = new File(caminhoWikipedia);
		this.diretorioSaida = diretorioSaida;
	}

	public void indexar() throws Exception {
		long inicio = System.currentTimeMillis();
		FileUtils.deleteDirectory(new File(diretorioSaida));
		FSDirectory dir = FSDirectory.open(Paths.get(diretorioSaida));
		StandardAnalyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setRAMBufferSizeMB(1024);
		LogByteSizeMergePolicy mergePolicy = new LogByteSizeMergePolicy();
		config.setMergePolicy(mergePolicy);

		// config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter indexWriter = new IndexWriter(dir, config);
		//
		DocMaker docMaker = new DocMaker();
		Properties properties = new Properties();
		properties.setProperty("docs.file", arquivoWikipedia.getAbsolutePath());
		properties.setProperty("keep.image.only.docs", "false");
		Config configMaker = new Config(properties);
		EnwikiContentSource source = new EnwikiContentSource();
		source.setConfig(configMaker);
		docMaker.setConfig(configMaker, source);
		docMaker.resetInputs();
		int countador = 0;
		Document doc;
		while ((doc = docMaker.makeDocument()) != null) {
			indexWriter.addDocument(doc);
			++countador;
			if (countador % 100000 == 0) {
				String msg = "Documentos indexados: " + countador;
				logger.info(msg);
			}
			String title = doc.get("doctitle");
			if (title.contains(":")) {
				logger.info(title);
			}
		}
		long total = System.currentTimeMillis() - inicio;
		logger.info("Tempo total (minutos): " + (total * 1000) / 60);
		docMaker.close();
		indexWriter.close();
		source.close();
	}
}
