package net.marcoreis.lucene.fragmentos;

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
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home") + "/livro-lucene/cv";
	private static final Logger logger =
			Logger.getLogger(FrequenciaDosTermos2.class);

	public static void main(String[] args) throws IOException {
		Directory directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		IndexReader reader = DirectoryReader.open(directory);
		String campo = "conteudoComPosicoes";
		LuceneDictionary dic =
				new LuceneDictionary(reader, campo);
		InputIterator ite = dic.getEntryIterator();
		BytesRef next;
		while ((next = ite.next()) != null) {
			logger.info(next.utf8ToString());
		}
	}
}
