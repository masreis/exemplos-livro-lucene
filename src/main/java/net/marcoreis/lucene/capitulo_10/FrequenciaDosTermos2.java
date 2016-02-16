package net.marcoreis.lucene.capitulo_10;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class FrequenciaDosTermos2 {
	private static String DIRETORIO_INDICE = System.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-01";
	private static final Logger logger = Logger.getLogger(FrequenciaDosTermos2.class);

	public static void main(String[] args) throws IOException {
		Directory directory = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		IndexReader reader = DirectoryReader.open(directory);
		String campo = "conteudoComVetores";
		LuceneDictionary dic = new LuceneDictionary(reader, campo);
		InputIterator ite = dic.getEntryIterator();
		for (BytesRef br : ite.contexts()) {
			BytesRef next = ite.next();
			logger.info(next);
		}
	}
}
