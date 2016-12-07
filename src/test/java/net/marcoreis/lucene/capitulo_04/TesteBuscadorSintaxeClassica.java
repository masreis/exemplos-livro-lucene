package net.marcoreis.lucene.capitulo_04;

import org.apache.log4j.Logger;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class TesteBuscadorSintaxeClassica {
	private static final Logger logger = Logger
			.getLogger(TesteBuscadorSintaxeClassica.class);

	@Test
	public void testeConsultaSintaxeClassica() {
		logger.info("Sintaxe clássica");
		String consulta = "conteudo:java";
		// consulta = "conteudo:java AND data:\"20160606\"";
		consulta = "conteudo:(java OR cdi)";
		consulta = "conteudo:(java AND cdi)";
		consulta = "conteudo:(java -cdi)";
		consulta = "conteudo:\"rede social\"";
		consulta = "conteudo:monitor*";
		// consulta = "conteudo:*omitor_ ou _conteudo:?onitor.";
		// consulta = "conteudo:monitor?";
		// consulta = "nome:artur~";
		// consulta = "data:[20140501 TO 20140630]";
		// consulta = "data:{20140501 TO 20140630}";
		consulta = "conteudo:\"proposta reforma\"~5";
		// consulta = "conteudo:java AND conteudo:cdi AND data:[20160101 TO
		// 20161231]";
		// consulta = "+conteudo:java +conteudo:cdi +data:[20160101 TO
		// 20161231]";
		// consulta = "+conteudo:(+java +cdi) +data:[20160101 TO 20161231]";
		// consulta = "conteudo:(java NOT cdi) AND data:[20160101 TO 20161231]";
		// consulta = "+conteudo:(java -cdi) +data:[20160101 TO 20161231]";
		// consulta = "+(conteudo:java -conteudo:cdi) +data:[20160101 TO
		// 20161231]";
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		buscador.buscar(consulta);
	}

	// @Test
	public void testeConsultaRegex() {
		logger.info("Regex");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String regex = "";
		regex = "/@abc~def@/"; // complemento
		regex = "/@[0-9]{4}\\-[0-9]{4}@/"; // telefone
		regex = "/@[2-9][0-9]{3}\\-[0-9]{4}@/"; // telefone BSB
		regex = "/@[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}@/"; // IP
		regex = "/@(0[1-9]|[1-2][0-9]|3[01])[\\- \\/\\.](0[1-9]|1[012])[\\- \\/\\.](19|20)[0-9][0-9]@/"; // Data
		regex = "/@[a-z0-9\\.\\_\\%\\+\\-]+\\@[a-z0-9\\.\\-]+\\.[a-z]{2,}@/"; // email
		regex = "/.*bug.{1,5}[0-9]{4}@/"; // ”ug
		String consulta = "conteudoNaoAnalisado:" + regex;
		buscador.buscar(consulta);
	}
}
