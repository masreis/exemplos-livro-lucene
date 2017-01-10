package net.marcoreis.lucene.capitulo_05;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.RAMDirectory;

public class IndiceEmMemoria {
	private RAMDirectory ramDirectory;
	private IndexWriter writer;

	// Inicializa objetos
	public IndiceEmMemoria() throws IOException {
		ramDirectory = new RAMDirectory();
		IndexWriterConfig conf = new IndexWriterConfig(
				new StandardAnalyzer());
		writer = new IndexWriter(ramDirectory, conf);
		criarDocumentos();
		writer.close();
	}

	// Cria documentos fictícios
	private void criarDocumentos() throws IOException {
		Document doc = new Document();
		String conteudo = "geralmente você pode usar as "
				+ "opções 'mute' ou 'unmute' para "
				+ "gerenciar os drivers ALSA no Linux";
		doc.add(new TextField("conteudo", conteudo, Store.YES));
		getWriter().addDocument(doc);
		//
		doc.clear();
		conteudo = "as versões 0.3 e 0.4 têm vários problemas no linux "
				+ "devido à reestruturação da interface do mixer "
				+ "que teve de ser reescrito em função de problemas"
				+ "identificados anteriormente no linux";
		doc.add(new TextField("conteudo", conteudo, Store.YES));
		getWriter().addDocument(doc);
		//
		doc.clear();
		conteudo = "você precisa carregar o módulo "
				+ "para o seu cartão de som ou usar "
				+ "o utilitário 'kmod' do linux";
		doc.add(new TextField("conteudo", conteudo, Store.YES));
		getWriter().addDocument(doc);
	}

	public IndexWriter getWriter() {
		return writer;
	}

	public RAMDirectory getRamDirectory() {
		return ramDirectory;
	}

}
