package net.marcoreis.lucene.capitulo_03;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class IndexadorArquivosLocaisTest {
	private static final Logger logger =
			LogManager.getLogger(IndexadorArquivosLocaisTest.class);
	private static String DIRETORIO_DOCUMENTOS =
			"/home/marco/Dropbox/cv/epam";
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/dropbox-epam";

	@Test
	public void testIndexacao() {
		try {
			IndexadorArquivosLocais indexador =
					new IndexadorArquivosLocais();
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
