package net.marcoreis.lucene.capitulo_03;

import org.junit.Test;

public class BuscadorArquivosLocaisTest {

	// @Test
<<<<<<< HEAD:src/test/java/net/marcoreis/lucene/capitulo_03/BuscadorArquivosLocaisTest.java
	public void testConsultaPorExtensao() {
=======
	public void testeConsultaPorExtensao() {
>>>>>>> 8d08a4fb1f606bfdce9a174a3da8ddc14495af28:src/test/java/net/marcoreis/lucene/capitulo_03/TesteBuscadorArquivosLocais.java
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "extensao:epub";
		buscador.buscar(consulta);
	}

	@Test
<<<<<<< HEAD:src/test/java/net/marcoreis/lucene/capitulo_03/BuscadorArquivosLocaisTest.java
	public void testConsultaPorConteudo() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta =
				"conteudo:(cloud unb energy ) ";
		consulta += "AND extensao:pdf ";
		// consulta +=
		// "AND caminhoText:(+mestrado +state-of-the-art)";
=======
	public void testeConsultaPorConteudo() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "";
		consulta += "";
		consulta+="conteudo:(Abordagem Qualitativa Objetivos Pesquisa descritiva)";
		// consulta += "conteudo:(+data +lake +object +storage) ";
		// consulta = "conteudo:(+big +data +monitoring)";
		consulta += "AND extensao:pdf ";
		consulta += "AND caminhoText:(+home +marco state)";
>>>>>>> 8d08a4fb1f606bfdce9a174a3da8ddc14495af28:src/test/java/net/marcoreis/lucene/capitulo_03/TesteBuscadorArquivosLocais.java
		buscador.buscar(consulta);
	}
}
