package net.marcoreis.lucene.capitulo_wikipedia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexadorWikipedia {
	private static Logger logger = Logger.getLogger(IndexadorWikipedia.class);
	private IndexWriter writer;
	private String diretorioIndice;
	private int quantidadePaginasIndexadas = 0;
	private UtilBusca buscador;

	public IndexadorWikipedia(String diretorioIndice) {
		try {
			this.diretorioIndice = diretorioIndice;
			FileUtils.deleteDirectory(new File(diretorioIndice));
			logger.info("Diretorio do indice: " + diretorioIndice);
			Directory d = FSDirectory.open(Paths.get(diretorioIndice));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			//
			// config.setUseCompoundFile(false);
			config.setRAMBufferSizeMB(1024);
			// TieredMergePolicy mergePolicy = new TieredMergePolicy();
			// LogDocMergePolicy mergePolicy = new LogDocMergePolicy();
			LogByteSizeMergePolicy mergePolicy = new LogByteSizeMergePolicy();
			// config.setMergePolicy(mergePolicy);
			// config.setMaxThreadStates(80);
			//
			// System.out.println(config);
			writer = new IndexWriter(d, config);
		} catch (Exception e) {
			logger.error(e);
		}
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

	public void fechar() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean indexar(Map<String, String> valores) {
		Document documento = new Document();
		if (jahIndexado(valores.get("id")))
			return false;
		try {
			for (String coluna : valores.keySet()) {
				String valor = valores.get(coluna);
				// valor = Normalizer.normalize(valor, Normalizer.Form.NFD);
				FieldType tipo = new FieldType();
				// tipo.setIndexed(true);
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

	public void commit() {
		try {
			writer.commit();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws IOException {
		String arquivo = "/Users/marcoreis/Docuwments/teste.txt";
		FileReader fileReader = new FileReader(arquivo);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String linha;
		while ((linha = bufferedReader.readLine()) != null) {
			String texto = Normalizer.normalize(linha, Normalizer.Form.NFD);
			System.out.println(texto);
		}
		bufferedReader.close();
	}

	public int getQuantidadeArquivosIndexados() {
		return quantidadePaginasIndexadas;
	}
}
