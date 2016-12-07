package net.marcoreis.lucene.capitulo_04;

import org.apache.log4j.Logger;
import org.apache.lucene.search.Query;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class TesteBuscadorAPI {
	private static final Logger logger = Logger.getLogger(TesteBuscadorAPI.class);

	@Test
	public void testeQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query query = null;
		buscador.buscar(query);
	}

}
