package net.marcoreis.lucene.capitulo_04;

import org.apache.log4j.Logger;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class TesteBuscadorSintaxeClassica {
	private static final Logger logger = Logger.getLogger(TesteBuscadorSintaxeClassica.class);

//	@Test
	public void testeConsultaSintaxeClassica() {
		logger.info("Sintaxe clássica");
		String consulta = "conteudo:java";
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		buscador.buscar(consulta);
	}

	@Test
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
