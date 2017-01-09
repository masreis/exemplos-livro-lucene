package net.marcoreis.lucene.capitulo_05;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class TesteIndiceAlterado {
	private static String DIRETORIO_INDICE = System
			.getProperty("user.home") + "/livro-lucene/indice";

	@Test
	public void testeIndiceAlterado() throws IOException {
		// Cria um reader normalmente
		Directory diretorio = FSDirectory
				.open(Paths.get(DIRETORIO_INDICE));
		DirectoryReader readerAnterior = DirectoryReader
				.open(diretorio);
		// Guarda a quantidade de itens
		int numDocsAnterior = readerAnterior.numDocs();
		// Abre um reader a partir do anterior
		DirectoryReader novoReader = DirectoryReader
				.openIfChanged(readerAnterior);
		// O índice não foi alterado, retorna nulo
		assertTrue(novoReader == null);
		// Adiciona documento e não faz commit
		IndexWriter writer = new IndexWriter(diretorio,
				new IndexWriterConfig(new StandardAnalyzer()));
		writer.addDocument(criaDocumento());
		// Índice alterado
		novoReader = DirectoryReader
				.openIfChanged(readerAnterior, writer);
		assertTrue(novoReader.numDocs() > numDocsAnterior);
	}

	private Iterable<? extends IndexableField> criaDocumento() {
		Document doc = new Document();
		doc.add(new TextField("conteudo", "Teste em tempo real",
				Store.YES));
		return doc;
	}
}
