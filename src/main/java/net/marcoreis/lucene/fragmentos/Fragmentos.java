package net.marcoreis.lucene.fragmentos;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexableField;

public class Fragmentos {
    public static void main(String[] args) {
        StoredField s = new StoredField("abc", "value");
        System.out.println();
    }

    public void criarDocumentoTextField() {
        Document doc = new Document();
        TextField campoNome = new TextField("nome", "marco antonio", Store.YES);
        doc.add(campoNome);
        TextField campoEndereco = new TextField("endereco", "rua 37 sul",
                Store.YES);
        doc.add(campoEndereco);
    }

    public void recuperarDadosDocumento() {
        Document doc = new Document();
        List<IndexableField> campos = doc.getFields();
        IndexableField campo = doc.getField("");
    }
}
