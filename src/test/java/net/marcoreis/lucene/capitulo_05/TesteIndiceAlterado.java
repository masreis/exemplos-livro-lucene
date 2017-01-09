package net.marcoreis.lucene.capitulo_05;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
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
		IndexWriter writer = new IndexWriter(diretorio,
				new IndexWriterConfig(new StandardAnalyzer()));
		DirectoryReader readerAnterior = DirectoryReader
				.open(writer);
		// Guarda a quantidade de itens
		int numDocsAnterior = readerAnterior.numDocs();
		// Abre um reader a partir do anterior
		IndexReader novoReader = DirectoryReader
				.openIfChanged(readerAnterior, writer);
		// O índice não foi alterado, retorna nulo
		assertTrue(novoReader == null);
		// Adiciona documento e não faz commit
		writer.addDocument(criaDocumento());
		// Índice alterado
		novoReader = DirectoryReader
				.openIfChanged(readerAnterior, writer);
		// A quantidade de documentos é maior
		assertTrue(novoReader.numDocs() > numDocsAnterior);
		//
		novoReader.close();
		readerAnterior.close();
		writer.close();
		diretorio.close();
	}

	private Iterable<? extends IndexableField> criaDocumento() {
		Document doc = new Document();
		doc.add(new TextField("conteudo", "Teste em tempo real",
				Store.YES));
		return doc;
	}
}
