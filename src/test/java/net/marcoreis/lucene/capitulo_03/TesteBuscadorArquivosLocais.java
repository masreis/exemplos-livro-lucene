package net.marcoreis.lucene.capitulo_03;

public class TesteBuscadorArquivosLocais {

	public void testeConsultaPorExtensao() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String consulta = "extensao:pdf";
		buscador.buscar(consulta);
	}

	public void testeConsultaPorConteudo() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String consulta = "conteudo:computação";
		buscador.buscar(consulta);
	}
}
