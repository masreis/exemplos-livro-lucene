package net.marcoreis.lucene.capitulo_03;

import org.apache.log4j.Logger;
import org.junit.Test;

public class TesteIndexadorArquivosLocais {
	private static final Logger logger = Logger
			.getLogger(TesteIndexadorArquivosLocais.class);
<<<<<<< HEAD
	private static String DIRETORIO_DOCUMENTOS = "/home/marco/temp/wiki/100000";
	private static String DIRETORIO_INDICE = System.getProperty(
			"user.home") + "/livro-lucene/indice-wiki-100000";
=======
	private static String DIRETORIO_DOCUMENTOS = "/home/marco/Dropbox/mestrado/ppca";
	private static String DIRETORIO_INDICE = System
			.getProperty("user.home")
			+ "/livro-lucene/indice-temp";
>>>>>>> af77dc805805ca8608deb6e3a1dc1c90ed59a538

	@Test
	public void testeIndexacao() {
		try {
			IndexadorArquivosLocais indexador = new IndexadorArquivosLocais();
			indexador.setApagarIndice(true);
			indexador.setDiretorioDocumentos(
					DIRETORIO_DOCUMENTOS);
			indexador.setDiretorioIndice(DIRETORIO_INDICE);
			indexador.setRecursivo(true);
			indexador.inicializar();
			indexador.indexar();
			indexador.finalizar();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
