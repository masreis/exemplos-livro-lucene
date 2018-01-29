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
		String consulta = "conteudo:(+spark +map)";
		buscador.buscar(consulta);
	}
}
