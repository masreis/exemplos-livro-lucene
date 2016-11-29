package net.marcoreis.lucene.capitulo_04;

import org.junit.Test;

public class TesteBuscadorAvancadoClassico {
	@Test
	public void testeConsultaProximidade() {
		BuscadorAvancadoClassico buscador = new BuscadorAvancadoClassico();
		String consulta = "";
		consulta = "data:[2010 TO 201202]";
		// consulta = "conteudo:rafael~2";
		// consulta = "dataAtualizacao:[2014-05-01 TO 2014-05-30]";
		// consulta = "conteudo:(rede social)";
		// consulta = "conteudo:a*";
		// consulta = "conteudo:itext";
		// consulta = "conteudoComVetores:u7ddle2941splce2rnrna";
		// consulta = "conteudo:(ssh AND integrator)";
		consulta = "conteudo:jmeter";
		consulta = "conteudo:\"Application Lifecycle Management\" AND extensao:pdf";
		consulta = "conteudo:(recall AND precision)";
		// consulta = "conteudo:MunicipiosBeneficiados";
		// consulta = "conteudo:(didáticas)";
		// consulta = "extensao:doc";
		// consulta = "conteudo:(\"instituto quadrix\")";
		consulta = "data:{20160120 TO 20161231}";
		consulta = "conteudo:(shell AND script)";
		// consulta = "tamanhoLong:[0 TO 400]";
		consulta = "conteudo:\"semantic provide\"~3";
		buscador.buscar(consulta);
	}
}
