package net.marcoreis.lucene.capitulo_03;

import org.junit.Test;

public class TesteBuscadorArquivosLocais {

	@Test
	public void testeConsultaPorExtensao() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "extensao:epub";
		buscador.buscar(consulta);
	}

	// @Test
	public void testeConsultaPorConteudo() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
<<<<<<< HEAD
		String consulta = "conteudo:(+big +data +messaging) "
				+ "AND extensao:pdf "
				+ "AND caminho:(home marco Dropbox mestrado state-of-the-art)";
=======
		String consulta = "conteudo:(+spark +map)";
>>>>>>> 47105e0af0c21b061b1f1e2363c75ffd3abd8e46
		buscador.buscar(consulta);
	}
}
