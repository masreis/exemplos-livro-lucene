package net.marcoreis.lucene.capitulo_05;

import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class BuscadorBoostTest {

	@Test
	public void testBoost() {
		BuscadorArquivosLocais buscador =
				new BuscadorArquivosLocais();
		String consulta =
				"conteudo:(+inteligência +artificial)";
		buscador.buscar(consulta);
	}
}
