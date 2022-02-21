package net.marcoreis.lucene.capitulo_04;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class BuscadorSintaxeClassicaTest {
	private static final Logger logger =
			LogManager.getLogger(BuscadorSintaxeClassicaTest.class);

	@Test
	public void testConsultaSintaxeClassica() {
		logger.info("Sintaxe clássica");
		String consulta = "conteudo:java";
		consulta = "conteudo:java AND data:\"20160606\"";
		consulta = "conteudo:(java OR cdi)";
		consulta = "conteudo:(java AND cdi)";
		consulta = "conteudo:(java -cdi)";
		consulta = "conteudo:monitor?";
		consulta = "conteudo:monitor*";
		consulta =
				"conteudo:java AND data:[20160101 TO 20161231]";
		consulta = "conteudo:manuel~";
		consulta = "*:*";
		consulta = "conteudo:/@<1000-1200>@/";
		consulta = "tamanho:/<0-500000>/";
		consulta = "conteudo:\"rede social\"";
		consulta = "conteudo:(nuvem rede^2)";
		consulta = "conteudo:\"proposta reforma\"~5";
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		buscador.buscar(consulta);
	}

	@Test
	public void testConsultaRegex() {
		logger.info("Regex");
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String regex = "";
		regex = "/@abc~def@/"; // complemento
		regex = "/@[0-9]{4}\\-[0-9]{4}@/"; // telefone
		regex = "/@[2-9][0-9]{3}\\-[0-9]{4}@/"; // telefone BSB
		regex = "/@[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}@/"; // IP
		regex = "/.*bug.{1,5}[0-9]{4}@/"; // Bug
		regex = "/@[a-z0-9\\.\\_\\%\\+\\-]+\\@[a-z0-9\\.\\-]+\\.[a-z]{2,}@/"; // email
		regex = "/@(0[1-9]|[1-2][0-9]|3[01])[\\- \\/\\.](0[1-9]|1[012])[\\- \\/\\.](19|20)[0-9][0-9]@/"; // Data
		String consulta = "conteudoNaoAnalisado:" + regex;
		buscador.buscar(consulta);
	}
}
