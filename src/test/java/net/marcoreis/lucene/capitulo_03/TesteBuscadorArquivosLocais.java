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
		String consulta = "conteudo:(+big +data +messaging) "
				+ "AND extensao:pdf "
				+ "AND caminho:(home marco Dropbox mestrado state-of-the-art)";
		buscador.buscar(consulta);
	}
}
