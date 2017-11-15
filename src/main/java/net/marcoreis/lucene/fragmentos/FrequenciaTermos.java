package net.marcoreis.lucene.fragmentos;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

public class FrequenciaTermos {
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home") + "/livro-lucene/cv";

	@Test
	public void testeFrequenciaTermos() {
		try {
			Directory diretorio = FSDirectory
					.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			String campo = "conteudoComPosicoes";
			try {
				Terms terms = reader.getTermVector(0, campo);
				TermsEnum termsEnum = terms.iterator();
				BytesRef bytesRef = termsEnum.next();
				while (bytesRef != null) {
					System.out.println("Termo: "
							+ bytesRef.utf8ToString() + "["
							+ termsEnum.docFreq() + "]");
					System.out.println("totalTermFreq: "
							+ termsEnum.totalTermFreq());
					bytesRef = termsEnum.next();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
