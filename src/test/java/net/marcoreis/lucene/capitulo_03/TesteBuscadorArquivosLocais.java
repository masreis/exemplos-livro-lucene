package net.marcoreis.lucene.capitulo_03;

import org.junit.Test;

public class TesteBuscadorArquivosLocais {

	@Test
	public void testeConsultaPorExtensao() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String consulta = "extensao:pdf";
		buscador.buscar(consulta);
	}

	@Test
	public void testeConsultaPorConteudo() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String consulta = "conteudo:(java AND jsf)";
		consulta = "conteudo:(ciência da informação)";
		consulta = "conteudo:(\"ciência da informação\")";
		consulta = "conteudo:(+\"private cloud\" +characteristic)";
		buscador.buscar(consulta);
	}
}
