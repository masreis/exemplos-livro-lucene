package net.marcoreis.lucene.capitulo_07;

import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

public class FrequenciaTermos {
	private static String DIRETORIO_INDICE = System.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-01";

	@Test
	public void testeFrequenciaTermos() {
		try {
			Directory directory = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			DirectoryReader reader = DirectoryReader.open(directory);
			// PostingsEnum pe = MultiFields.getTermDocsEnum(r, field, term) ;
			// DocsEnum de = MultiFields.getTermDocsEnum(reader,
			// MultiFields.getLiveDocs(reader), "content",
			// new BytesRef("java"));
			int doc;
			// while ((doc = de.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
			// de.attributes();
			// System.out.println(de.freq());
			// }
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
