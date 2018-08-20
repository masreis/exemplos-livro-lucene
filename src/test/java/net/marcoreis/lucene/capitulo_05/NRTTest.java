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
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class NRTTest {
	private static String DIRETORIO_INDICE = System
			.getProperty("user.home") + "/livro-lucene/indice";

	@Test
	public void testTempoReal() throws IOException {
		// Cria um reader normalmente
		Directory diretorio = FSDirectory
				.open(Paths.get(DIRETORIO_INDICE));
		IndexWriter writer = new IndexWriter(diretorio,
				new IndexWriterConfig(new StandardAnalyzer()));
		IndexReader readerAnterior = DirectoryReader
				.open(diretorio);
		IndexSearcher searcher = new IndexSearcher(
				readerAnterior);
		TopDocs hits = searcher.search(new MatchAllDocsQuery(),
				1);
		// Guarda a quantidade de itens
		long numDocsAnterior = hits.totalHits;
		// Adiciona documento e não faz commit
		writer.addDocument(criaDocumento());
		writer.addDocument(criaDocumento());
		// Abre um reader NRT através do writer
		IndexReader novoReader = DirectoryReader
				.open(writer);
		searcher = new IndexSearcher(novoReader);
		hits = searcher.search(new MatchAllDocsQuery(), 1);
		long numDocsAtual = hits.totalHits;
		// Verifica se tem um item a mais
		assertTrue(numDocsAtual == numDocsAnterior + 2);
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
