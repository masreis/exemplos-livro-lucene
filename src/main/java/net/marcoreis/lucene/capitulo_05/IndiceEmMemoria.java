package net.marcoreis.lucene.capitulo_05;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

public class IndiceEmMemoria {

	public Directory recuperarDiretorioEmMemoria()
			throws IOException {
		RAMDirectory ramDirectory = new RAMDirectory();
		IndexWriterConfig conf = new IndexWriterConfig(
				new StandardAnalyzer());
		IndexWriter writer = new IndexWriter(ramDirectory, conf);
		criarDocumentos(writer);
		writer.close();
		return ramDirectory;
	}

	// Cria documentos fictícios
	private void criarDocumentos(IndexWriter writer)
			throws IOException {
		Document doc = new Document();
		String conteudo = "geralmente você pode usar as "
				+ "opções 'mute' ou 'unmute' para "
				+ "gerenciar os drivers ALSA no Linux";
		doc.add(new TextField("conteudo", conteudo, Store.YES));
		writer.addDocument(doc);
		//
		doc.clear();
		conteudo = "as versões 0.3 e 0.4 têm vários problemas "
				+ "devido à reestruturação da interface do mixer";
		doc.add(new TextField("conteudo", conteudo, Store.YES));
		writer.addDocument(doc);
		//
		doc.clear();
		conteudo = "você precisa carregar o módulo "
				+ "para o seu cartão de som ou usar "
				+ "o utilitário 'kmod'";
		doc.add(new TextField("conteudo", conteudo, Store.YES));
		writer.addDocument(doc);
	}

}
