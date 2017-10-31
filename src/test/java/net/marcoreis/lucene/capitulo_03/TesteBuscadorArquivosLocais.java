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
		String consulta = "conteudo:(java AND jsf)";
		consulta = "conteudo:(ciência da informação)";
		consulta = "conteudo:(flink AND single AND engine)";
		consulta = "conteudo:kafka";
		consulta = "conteudo:openstack";
		consulta =
				"conteudo:(Private AND IaaS AND Cloudstack AND opennebula)";
		consulta = "conteudo:(batch streaming)";
		consulta = "conteudo:(hadoop)";
		consulta =
				"conteudo:(Data-intensive applications, challenges, techniques and technologies: A survey on Big Data)";
		consulta = "conteudo:iot";
		consulta = "conteudo:(spark)";
		consulta = "conteudo:(\"ciência da informação\")";
		consulta = "conteudo:(hdp4)";
		buscador.buscar(consulta);
	}
}
