package net.marcoreis.lucene.capitulo_03;

import org.apache.log4j.Logger;
import org.junit.Test;

public class IndexadorArquivosLocaisTest {
	private static final Logger logger =
			Logger.getLogger(IndexadorArquivosLocaisTest.class);
	private static String DIRETORIO_DOCUMENTOS =
			"/home/marco/Dropbox/";
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/indice-dropbox";

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
