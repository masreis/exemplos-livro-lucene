package net.marcoreis.lucene.capitulo_07;

import java.io.File;

import org.junit.Test;

public class IndexadorNutchTest {

	private static final String DIRETORIO_INDICE =
			"/home/marco/livro-lucene/nutch";
	private static final String DIRETORIO_SEGMENTO =
			"/home/marco/software/apache-nutch-1.13/crawl/dump-conteudo";

	@Test
	public void indexar() {
		IndexadorNutch indexador = new IndexadorNutch();
		indexador.setDiretorioIndice(DIRETORIO_INDICE);
		indexador.indexar(new File(DIRETORIO_SEGMENTO));
	}

}
