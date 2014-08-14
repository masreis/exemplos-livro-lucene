package net.marcoreis.lucene.capitulo_04;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType.NumericType;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;

public class ExemploFieldType {

    public void criarDocumentoTextField() {
        Document doc = new Document();
        TextField campoNome = new TextField("nome", "marco antonio", Store.YES);
        doc.add(campoNome);
        TextField campoEndereco = new TextField("endereco", "rua 37 sul",
                Store.YES);
        doc.add(campoEndereco);
    }

    public void criarDocumentoField() {
        FieldType ft = new FieldType();
        ft.setIndexed(true);
        ft.setStored(true);
        ft.setTokenized(true);
        //
        Document doc = new Document();
        Field campoNome = new Field("nome", "marco antonio", ft);
        doc.add(campoNome);
        Field campoEndereco = new Field("endereco", "rua 37 sul", ft);
        doc.add(campoEndereco);
    }

    public void criarDocumentoDoubleField() {
        Document doc = new Document();
        TextField campoNome = new TextField("nome", "marco antonio", Store.YES);
        doc.add(campoNome);
        DoubleField campoSalario = new DoubleField("salario", 8000.01,
                Store.YES);
        doc.add(campoSalario);
    }

}
