package net.marcoreis.lucene.capitulo_05;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.junit.Test;

public class TesteWriteLock {

	@Test
	public void testeWriterDuplo() throws IOException {
		IndexWriter writer = null;
		IndexWriter segundoWriter = null;
		String caminho = System.getProperty("java.io.tmpdir")
				+ "/temp";
		Directory diretorio = FSDirectory
				.open(Paths.get(caminho));
		Analyzer analyzer = new StandardAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		IndexWriterConfig segundaConf = new IndexWriterConfig(
				analyzer);
		// Tenta abrir o diretório para gravação
		try {
			writer = new IndexWriter(diretorio, conf);
			segundoWriter = new IndexWriter(diretorio,
					segundaConf);
		} catch (LockObtainFailedException e) {
			e.printStackTrace();
		}
		// Verifica o estado dos writers
		assertNotNull(writer);
		assertNull(segundoWriter);
		// Fecha apenas o que está aberto
		writer.close();
	}
}
