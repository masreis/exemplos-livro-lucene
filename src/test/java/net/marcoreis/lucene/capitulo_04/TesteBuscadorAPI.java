package net.marcoreis.lucene.capitulo_04;

import org.apache.log4j.Logger;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.util.BytesRef;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class TesteBuscadorAPI {
	private static final Logger logger = Logger
			.getLogger(TesteBuscadorAPI.class);

	// @Test
	public void testeTermQuery() {
		logger.info("Consulta TermQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term term = new Term("conteudo", "java");
		Query query = new TermQuery(term);
		buscador.buscar(query);
	}

	// @Test
	public void testeBooleanQueryShould() {
		logger.info("Consulta BooleanQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query q1 = new TermQuery(new Term("conteudo", "java"));
		Query q2 = new TermQuery(new Term("conteudo", "cdi"));
		BooleanQuery query = new BooleanQuery.Builder()
				.add(q1, Occur.SHOULD).add(q2, Occur.SHOULD).build();
		buscador.buscar(query);
	}

	// @Test
	public void testeBooleanQueryMust() {
		logger.info("Consulta BooleanQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query q1 = new TermQuery(new Term("conteudo", "java"));
		Query q2 = new TermQuery(new Term("conteudo", "cdi"));
		BooleanQuery query = new BooleanQuery.Builder()
				.add(q1, Occur.MUST).add(q2, Occur.MUST).build();
		buscador.buscar(query);
	}

	// @Test
	public void testeBooleanQueryMustNot() {
		logger.info("Consulta BooleanQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query q1 = new TermQuery(new Term("conteudo", "java"));
		Query q2 = new TermQuery(new Term("conteudo", "cdi"));
		BooleanQuery query = new BooleanQuery.Builder()
				.add(q1, Occur.MUST).add(q2, Occur.MUST_NOT).build();
		buscador.buscar(query);
	}

	// @Test
	public void testePhraseQuery() {
		logger.info("Consulta PhraseQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		PhraseQuery query = new PhraseQuery("conteudo", "rede",
				"social");
		buscador.buscar(query);
	}

	@Test
	public void testeTermRangeQueryDataInclusive() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		boolean incluirLimiteInferior = true;
		boolean incluirLimiteSuperior = true;
		BytesRef limiteInferior = new BytesRef("20160101");
		BytesRef limiteSuperior = new BytesRef("20161231");
		Query query = new TermRangeQuery("data", limiteInferior,
				limiteSuperior, incluirLimiteInferior,
				incluirLimiteSuperior);
		buscador.buscar(query);
	}

	public void teste() {
		logger.info("Consulta TermQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		//
		MatchAllDocsQuery all = new MatchAllDocsQuery();
		// NumericRangeQuery<Long> nrq =
		// NumericRangeQuery.newLongRange("tamanho", UM_MEGA, DOIS_MEGAS,
		// true, true);
		Term term = new Term("conteudo", "extrair");
		Query query = new TermQuery(term);
		// PhraseQuery query = new PhraseQuery("conteudo", "ciência", "da",
		// "informação");
		Term[] termosAplicacaoExemplo = new Term[] {
				new Term("conteudo", "aplicação"),
				new Term("conteudo", "exemplo") };
		Term[] termosBancoDados = new Term[] {
				new Term("conteudo", "banco"),
				new Term("conteudo", "dados") };
		//
		// MultiPhraseQuery mpq = new MultiPhraseQuery();
		// mpq.add(termosAplicacaoExemplo);
		// mpq.add(termosBancoDados);
		// mpq.setSlop(2);
		//
		Term termo = new Term("conteudo", "consider");
		PrefixQuery pq = new PrefixQuery(termo);
		// Term termo = new Term("conteudo", "consider");
		SpanQuery query1;
		SpanQuery[] clausulas = null;
		SpanNearQuery sq = new SpanNearQuery(clausulas, 2, true);
		BooleanQuery bq = null;// new BooleanQuery();
		TermQuery tq = new TermQuery(new Term("conteudo", "calor"));
		TermQuery tq1 = new TermQuery(new Term("conteudo", "java"));
		// PhraseQuery pq = null;// new PhraseQuery();
		// Term termo = new Term("conteudo", "calor");
		WildcardQuery wq = new WildcardQuery(termo);
		// Term termo = new Term("conteudo", "seção");
		FuzzyQuery fq = new FuzzyQuery(termo, 2);
		Query q = LongPoint.newRangeQuery("tamanhoLong", 8000, 40000);

		//
		// Query query = null;
		buscador.buscar(query);
	}
}
