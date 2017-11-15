package net.marcoreis.lucene.fragmentos;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

public class FrequenciaDosTermos3 {
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home") + "/livro-lucene/cv";

	public static void main(String[] args) throws IOException {
		FSDirectory directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		IndexReader indexReader =
				DirectoryReader.open(directory);

		Bits liveDocs = MultiFields.getLiveDocs(indexReader);
		Fields fields = MultiFields.getFields(indexReader);
		for (String field : fields) {

			TermsEnum termEnum = MultiFields
					.getTerms(indexReader, field).iterator();
			BytesRef bytesRef;
			while ((bytesRef = termEnum.next()) != null) {
				if (termEnum.seekExact(bytesRef)) {
					System.out.println(bytesRef.utf8ToString());
					// DocsEnum docsEnum =
					// termEnum.docs(liveDocs, null);
					// if (docsEnum != null) {
					// int doc;
					// while ((doc = docsEnum
					// .nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
					// System.out.println(
					// bytesRef.utf8ToString()
					// + " in doc " + doc
					// + ": "
					// + docsEnum.freq());
					// }
					// }
				}
			}
		}

		for (String field : fields) {
			TermsEnum termEnum = MultiFields
					.getTerms(indexReader, field).iterator();
			BytesRef bytesRef;
			while ((bytesRef = termEnum.next()) != null) {
				int freq = indexReader
						.docFreq(new Term(field, bytesRef));

				System.out.println(bytesRef.utf8ToString()
						+ " in " + freq + " documents");

			}
		}
	}
}
