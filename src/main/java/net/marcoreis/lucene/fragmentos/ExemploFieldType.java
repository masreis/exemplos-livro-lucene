package net.marcoreis.lucene.fragmentos;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;

public class ExemploFieldType {

	public void criarDocumentoTextField() {
		Document doc = new Document();
		TextField campoNome = new TextField("nome",
				"marco antonio", Store.YES);
		doc.add(campoNome);
		TextField campoEndereco = new TextField("endereco",
				"rua 37 sul", Store.YES);
		doc.add(campoEndereco);
	}

	public void criarDocumentoField() {
		FieldType ft = new FieldType();
		// ft.setIndexed(true);
		ft.setStored(true);
		ft.setTokenized(true);
		//
		Document doc = new Document();
		Field campoNome = new Field("nome", "marco antonio",
				ft);
		doc.add(campoNome);
		Field campoEndereco = new Field("endereco",
				"rua 37 sul", ft);
		doc.add(campoEndereco);
	}

	public void criarDocumentoDoubleField() {
		Document doc = new Document();
		TextField campoNome = new TextField("nome",
				"marco antonio", Store.YES);
		doc.add(campoNome);
		DoublePoint campoSalario = new DoublePoint(
				"salario", 8000.01);
		doc.add(campoSalario);
	}

}
