package net.marcoreis.lucene.fragmentos;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.SlowCompositeReaderWrapper;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class FrequenciaDosTermos4 {
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home") + "/livro-lucene/cv";
	private static final Logger logger =
			Logger.getLogger(FrequenciaDosTermos4.class);

	public static void main(String[] args) throws IOException {
		Directory directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		IndexReader directoryReader =
				DirectoryReader.open(directory);
		String campo = "conteudoComPosicoes";
		Terms terms = SlowCompositeReaderWrapper
				.wrap(directoryReader).terms(campo);
		logger.info("Termos indexados: " + terms.size());
		logger.info(
				"Documentos com pelo menos um termo neste campo: "
						+ terms.getDocCount());
		logger.info(terms.getSumDocFreq());
		logger.info(terms.getSumTotalTermFreq());
		TermsEnum ite = terms.iterator();
		BytesRef term;
		while ((term = ite.next()) != null) {
			logger.info(ite.docFreq());
			System.out.println(term.utf8ToString());
		}
	}
}
