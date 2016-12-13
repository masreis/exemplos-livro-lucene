package net.marcoreis.lucene.capitulo_03;

import org.apache.log4j.Logger;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.IndexadorArquivosLocais;

public class TesteIndexadorArquivosLocais {
	private static final Logger logger = Logger
			.getLogger(TesteIndexadorArquivosLocais.class);
	private static String DIRETORIO_DOCUMENTOS = "/home/marco/Dropbox/mestrado/papers";
	private static String DIRETORIO_INDICE = System
			.getProperty("user.home")
			+ "/livro-lucene/indice";

	@Test
	public void testeIndexacao() {
		try {
			IndexadorArquivosLocais indexador = new IndexadorArquivosLocais();
			indexador.setApagarIndice(true);
			indexador.setDiretorioDocumentos(
					DIRETORIO_DOCUMENTOS);
			indexador.setDiretorioIndice(DIRETORIO_INDICE);
			indexador.inicializar();
			indexador.indexar();
			indexador.finalizar();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
