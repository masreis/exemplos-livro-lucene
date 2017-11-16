package net.marcoreis.lucene.capitulo_07;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnalisadorTermosIndiceTest {
	private static String DIRETORIO_INDICE =
			System.getProperty("user.home")
					+ "/livro-lucene/cv-com-vetores";
	private static Directory directory;
	private static IndexReader reader;

	@BeforeClass
	public static void inicializar() throws IOException {
		directory =
				FSDirectory.open(Paths.get(DIRETORIO_INDICE));
		reader = DirectoryReader.open(directory);
	}

	// @Test
	public void testMostraTermosIndice() throws IOException {
		String campo = "conteudo";
		Terms termos = MultiFields.getTerms(reader, campo);
		TermsEnum iteTermos = termos.iterator();
		BytesRef next;
		while ((next = iteTermos.next()) != null) {
			System.out.println(next.utf8ToString());
		}
	}

	// @Test
	public void testDadosTermo() throws IOException {
		Term termo = new Term("conteudo", "java");
		String message = String.format(
				"%s:%s \nTotal de Ocorrências (TF): %3$d\n"
						+ "Total de Documentos (DF):%4$d",
				termo.field(), termo.text(),
				reader.totalTermFreq(termo),
				reader.docFreq(termo));
		System.out.println(message);
	}

	@Test
	public void testAnalisaTermosDocumento()
			throws IOException {
		for (int docId = 0; docId < reader.maxDoc(); docId++) {
			// Imprime nome do arquivo
			Document documento = reader.document(docId);
			System.out.println(
					String.format("Analisando documento %s",
							documento.get("caminho")));
			// Recupera termos do documento
			String campo = "conteudoComPosicoes";
			Terms vetorTermos =
					reader.getTermVector(docId, campo);
			if (vetorTermos == null) {
				System.err
						.println("Não há termos no documento.");
				continue;
			}
			TermsEnum termos = vetorTermos.iterator();
			// Monta um mapa com a quantidade de
			// ocorrências de cada termo dentro do documento
			Map<String, Integer> frequencias =
					criaMapaDeTermosEFrequencias(termos);
			// Imprime a frequência com que
			// cada termo aparece no documento
			String conteudo = frequencias.entrySet().stream()
					.map(e -> e.getKey() + "[" + e.getValue()
							+ "]\n")
					.collect(Collectors.joining());
			System.out.println(conteudo);
		}
	}

	private Map<String, Integer> criaMapaDeTermosEFrequencias(
			TermsEnum termos) throws IOException {
		BytesRef bytesRef = null;
		Map<String, Integer> frequencias = new TreeMap<>();
		while ((bytesRef = termos.next()) != null) {
			String termo = bytesRef.utf8ToString();
			PostingsEnum postingEnum =
					termos.postings(null, PostingsEnum.FREQS);
			int noMoreDocs = DocIdSetIterator.NO_MORE_DOCS;
			while (postingEnum.nextDoc() != noMoreDocs) {
				int freqDocumento = postingEnum.freq();
				Integer freqMapa = frequencias.get(termo);
				if (freqMapa == null) {
					freqMapa = 0;
				}
				freqMapa += freqDocumento;
				frequencias.put(termo, freqMapa);
			}
		}
		return frequencias;
	}
}
