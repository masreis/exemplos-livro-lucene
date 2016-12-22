package net.marcoreis.lucene.capitulo_05;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexOptions;
import org.junit.Test;

public class ExemploFieldType {

	@Test
	public void criarDocumentoTextField() {
		Document doc = new Document();
		StringField campoTelefone = new StringField("telefone",
				"81194620", Store.YES);
		doc.add(campoTelefone);
		System.out.println("Tipo: " + campoTelefone.fieldType());
		//
		TextField campoEndereco = new TextField("endereco",
				"rua 37 sul", Store.YES);
		doc.add(campoEndereco);
		System.out.println("Tipo: " + campoEndereco.fieldType());
	}

	@Test
	public void criarDocumentoField() {
		FieldType ftString = new FieldType();
		ftString.setIndexOptions(IndexOptions.DOCS);
		ftString.setStored(true);
		ftString.setTokenized(false);
		ftString.setOmitNorms(true);
		ftString.freeze();
		System.out.println("Tipo ->" + ftString);
		//
		FieldType ftText = new FieldType();
		ftText.setIndexOptions(
				IndexOptions.DOCS_AND_FREQS_AND_POSITIONS);
		ftText.setStored(true);
		ftText.freeze();
		System.out.println("Tipo ->" + ftText);
		//
		Document doc = new Document();
		Field campoTelefone = new Field("telefone", "81194620",
				ftString);
		doc.add(campoTelefone);
		Field campoEndereco = new Field("endereco", "rua 37 sul",
				ftText);
		doc.add(campoEndereco);
	}

	public void criarDocumentoDoubleField() {
		Document doc = new Document();
		TextField campoNome = new TextField("nome",
				"marco antonio", Store.YES);
		doc.add(campoNome);
		DoublePoint campoSalario = new DoublePoint("salario",
				8000.01);
		doc.add(campoSalario);
	}

	public void criarDocumentoBoost() {
		Document doc = new Document();
		String textoDeUmaNoticia = "Dispositivo portátil de armazenamento"
				+ " com memória flash e capacidade de 32GB. "
				+ "Acessório super prático que abe no bolso."
				+ "Produzido pela Sandisk. ";
		TextField campoTitulo = new TextField("titulo",
				"pen drive 32GB sandisk", Store.YES);
		TextField campoTextoNoticia = new TextField(
				"textoNoticia", textoDeUmaNoticia, Store.YES);
		if (textoDeUmaNoticia.contains("linux")) {
			campoTextoNoticia.setBoost(3.0f);
		} else if (textoDeUmaNoticia.contains("android")) {
			campoTextoNoticia.setBoost(2.0f);
		}
		doc.add(campoTextoNoticia);
		doc.add(campoTitulo);
	}

}
