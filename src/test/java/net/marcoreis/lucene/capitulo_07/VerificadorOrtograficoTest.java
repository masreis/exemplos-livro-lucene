package net.marcoreis.lucene.capitulo_07;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VerificadorOrtograficoTest {
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/cv";
	private static Directory directory;
	private static IndexReader reader;
	private String termoComErro = "progeto";
	private static String campo = "conteudo";

	@BeforeClass
	public static void inicializar() throws IOException {
		directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		reader = DirectoryReader.open(directory);
	}

	@Test
	public void test1IndexaDicionario() throws IOException {
		SpellChecker verificador = new SpellChecker(directory);
		Dictionary dicionario =
				new LuceneDictionary(reader, campo);
		IndexWriterConfig config = new IndexWriterConfig();
		verificador.indexDictionary(dicionario, config, true);
		verificador.close();
	}

	@Test
	public void testSeExiste() throws IOException {
		SpellChecker verificador = new SpellChecker(directory);
		System.out.println(String.format(
				"O termo '%s' está no dicionário: %s",
				termoComErro, verificador.exist(termoComErro)));
		verificador.close();
	}

	@Test
	public void testSugereAlternativas() throws IOException {
		System.out.println("Sugere alternativas");
		SpellChecker verificador = new SpellChecker(directory);
		int quantidadeSugestoes = 5;
		String[] sugestoes = verificador.suggestSimilar(
				termoComErro, quantidadeSugestoes);
		for (String sugestao : sugestoes) {
			System.out.println(sugestao);
		}
		verificador.close();
	}

	@Test
	public void testSugereAlternativasPrecisas()
			throws IOException {
		System.out.println("Sugere alternativas precisas");
		SpellChecker verificador = new SpellChecker(directory);
		int quantidadeSugestoes = 5;
		String[] sugestoes = verificador.suggestSimilar(
				termoComErro, quantidadeSugestoes, 0.6f);
		for (String sugestao : sugestoes) {
			System.out.println(sugestao);
		}
		verificador.close();
	}
}
