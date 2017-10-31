package net.marcoreis.lucene.capitulo_07;

import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.junit.Test;

public class IndexadorNutchTest {

	private static final String DIRETORIO_INDICE =
			"/home/marco/livro-lucene/nutch";
	private static final String DIRETORIO_SEGMENTO =
			"/home/marco/software/apache-nutch-1.13/dump-segmento";

	@Test
	public void indexar() {
		IndexadorNutch indexador = new IndexadorNutch();
		indexador.setDiretorioIndice(DIRETORIO_INDICE);
		indexador.setDiretorioSegmento(DIRETORIO_SEGMENTO);
		try {
			indexador.indexar();
		} catch (IOException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
