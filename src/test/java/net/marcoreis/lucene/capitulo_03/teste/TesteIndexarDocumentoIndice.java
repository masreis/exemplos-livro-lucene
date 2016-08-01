package net.marcoreis.lucene.capitulo_03.teste;

import java.io.File;

import org.apache.log4j.Logger;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.IndexadorArquivosLocais;

public class TesteIndexarDocumentoIndice {
	private static final Logger logger = Logger.getLogger(TesteIndexadorArquivosLocais.class);
	private static String CAMINHO_ARQUIVO = "/home/marco/Dropbox/material-de-estudo/mestrado/thesis.pdf";
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/indice";

	@Test
	public void testeIndexacao() {
		try {
			IndexadorArquivosLocais indexador = new IndexadorArquivosLocais(DIRETORIO_INDICE, false);
			indexador.inicializar();
			indexador.indexarArquivo(new File(CAMINHO_ARQUIVO));
			indexador.finalizar();
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
