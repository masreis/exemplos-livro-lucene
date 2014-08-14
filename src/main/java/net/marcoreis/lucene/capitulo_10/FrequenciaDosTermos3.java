package net.marcoreis.lucene.capitulo_10;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.Fields;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Bits;
import org.apache.lucene.util.BytesRef;

public class FrequenciaDosTermos3 {

    public static void main(String[] args) throws IOException {
        FSDirectory directory = FSDirectory.open(new File("/tmp/moo"));
        /*
         * IndexWriter writer = new IndexWriter(directory, new
         * IndexWriterConfig(Version.LUCENE_44, new
         * StandardAnalyzer(Version.LUCENE_44))); Document document = new
         * Document(); document.add(new TextField("foo", "abc", Store.YES));
         * document.add(new TextField("foo", "abc", Store.YES));
         * document.add(new TextField("foo", "aaa", Store.YES));
         * document.add(new TextField("bar", "abc", Store.YES));
         * writer.addDocument(document); writer.commit(); writer.close(true);
         */

        IndexReader indexReader = DirectoryReader.open(directory);

        Bits liveDocs = MultiFields.getLiveDocs(indexReader);
        Fields fields = MultiFields.getFields(indexReader);
        for (String field : fields) {

            TermsEnum termEnum = MultiFields.getTerms(indexReader, field)
                    .iterator(null);
            BytesRef bytesRef;
            while ((bytesRef = termEnum.next()) != null) {
                if (termEnum.seekExact(bytesRef)) {

                    DocsEnum docsEnum = termEnum.docs(liveDocs, null);

                    if (docsEnum != null) {
                        int doc;
                        while ((doc = docsEnum.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                            System.out
                                    .println(bytesRef.utf8ToString()
                                            + " in doc " + doc + ": "
                                            + docsEnum.freq());
                        }
                    }
                }
            }
        }

        for (String field : fields) {
            TermsEnum termEnum = MultiFields.getTerms(indexReader, field)
                    .iterator(null);
            BytesRef bytesRef;
            while ((bytesRef = termEnum.next()) != null) {
                int freq = indexReader.docFreq(new Term(field, bytesRef));

                System.out.println(bytesRef.utf8ToString() + " in " + freq
                        + " documents");

            }
        }
    }
}
