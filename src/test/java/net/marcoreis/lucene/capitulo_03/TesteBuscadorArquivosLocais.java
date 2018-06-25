package net.marcoreis.lucene.capitulo_03;

import org.junit.Test;

public class TesteBuscadorArquivosLocais {

	// @Test
	public void testeConsultaPorExtensao() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta = "extensao:epub";
		buscador.buscar(consulta);
	}

	@Test
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
		buscador.buscar(consulta);
	}
}
