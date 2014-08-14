package net.marcoreis.lucene.capitulo_10;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.DocsEnum;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class FrequenciaTermos {
    private static String DIRETORIO_INDICE = System.getProperty("user.home")
            + "/livro-lucene/indice-capitulo-02-exemplo-01";

    public static void main(String[] args) throws IOException, ParseException {
        Directory directory = FSDirectory.open(new File(DIRETORIO_INDICE));
        DirectoryReader reader = DirectoryReader.open(directory);
        DocsEnum de = MultiFields
                .getTermDocsEnum(reader, MultiFields.getLiveDocs(reader),
                        "content", new BytesRef("bla"));
        int doc;
        while ((doc = de.nextDoc()) != DocsEnum.NO_MORE_DOCS) {
            de.attributes();
            System.out.println(de.freq());
        }
        reader.close();
    }

}
