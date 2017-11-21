package net.marcoreis.lucene.capitulo_03;

import org.junit.Test;

public class TesteBuscadorArquivosLocais {

	// @Test
	public void testeConsultaPorExtensao() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "extensao:pdf";
		buscador.buscar(consulta);
	}

	@Test
	public void testeConsultaPorConteudo() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
<<<<<<< HEAD
		String consulta = "conteudo:(projeto)";
=======
		String consulta = "conteudo:springjunit4classrunner";
>>>>>>> ee084e13b288dce38f1df37eef67cb13611055b2
		buscador.buscar(consulta);
	}
}
