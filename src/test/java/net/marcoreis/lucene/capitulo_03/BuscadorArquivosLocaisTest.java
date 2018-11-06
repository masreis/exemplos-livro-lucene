package net.marcoreis.lucene.capitulo_03;

import org.junit.Test;

public class BuscadorArquivosLocaisTest {

	// @Test
	public void testConsultaPorExtensao() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "extensao:epub";
		buscador.buscar(consulta);
	}

	@Test
	public void testConsultaPorConteudo() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "conteudo:(lambda architecture) ";
		consulta += "AND extensao:pdf ";
		// consulta +=
		// "AND caminhoText:(+mestrado +state-of-the-art)";
//		consulta += "AND extensao:pdf ";
//		consulta += "AND caminhoText:(+home +marco literature)";
		buscador.buscar(consulta);
	}
}
