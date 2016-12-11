package net.marcoreis.lucene.capitulo_04;

import org.apache.log4j.Logger;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.xml.builders.MatchAllDocsQueryBuilder;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.MultiPhraseQuery.Builder;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.RegexpQuery;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.automaton.RegExp;
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
	public void testeBooleanQueryMultiple() {
		logger.info("Consulta BooleanQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query q1 = new TermQuery(new Term("conteudo", "java"));
		//
		boolean incluirLimiteInferior = true;
		boolean incluirLimiteSuperior = true;
		BytesRef limiteInferior = new BytesRef("20160101");
		BytesRef limiteSuperior = new BytesRef("20161231");
		Query q2 = new TermRangeQuery("data", limiteInferior,
				limiteSuperior, incluirLimiteInferior,
				incluirLimiteSuperior);
		//
		BooleanQuery query = new BooleanQuery.Builder()
				.add(q1, Occur.MUST).add(q2, Occur.MUST).build();
		buscador.buscar(query);
	}

	// @Test
	public void testePhraseQuery() {
		logger.info("Consulta PhraseQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query query = new PhraseQuery("conteudo", "rede", "social");
		buscador.buscar(query);
	}

	// @Test
	public void testePhraseQuerySlop() {
		logger.info("Consulta PhraseQuery");
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query query = new PhraseQuery(5, "conteudo", "proposta",
				"reforma");
		buscador.buscar(query);
	}

	// @Test
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

	// @Test
	public void testeTermRangeQueryDataExclusive() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		boolean incluirLimiteInferior = false;
		boolean incluirLimiteSuperior = false;
		BytesRef limiteInferior = new BytesRef("20160101");
		BytesRef limiteSuperior = new BytesRef("20161231");
		Query query = new TermRangeQuery("data", limiteInferior,
				limiteSuperior, incluirLimiteInferior,
				incluirLimiteSuperior);
		buscador.buscar(query);
	}

	// @Test
	public void testePrefirQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term termo = new Term("conteudo", "monitor");
		Query query = new PrefixQuery(termo);
		buscador.buscar(query);
	}

	// @Test
	public void testeMultiPhraseQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term[] termoJavaPlatform = new Term[] {
				new Term("conteudo", "java"),
				new Term("conteudo", "platform") };
		Term[] termoCdiWeld = new Term[] {
				new Term("conteudo", "cdi"),
				new Term("conteudo", "weld") };
		Query query = new MultiPhraseQuery.Builder()
				.add(termoJavaPlatform).add(termoCdiWeld).setSlop(5)
				.build();
		buscador.buscar(query);
	}

	// @Test
	public void testeMultiPhraseQuery2() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term[] termoJavaPlatform = new Term[] {
				new Term("conteudo", "java"),
				new Term("conteudo", "platform") };
		Term[] termoCdiWeld = new Term[] {
				new Term("conteudo", "cdi"),
				new Term("conteudo", "weld") };
		Builder builder = new MultiPhraseQuery.Builder();
		builder.add(termoJavaPlatform);
		builder.add(termoCdiWeld);
		builder.setSlop(5);
		Query query = builder.build();

		buscador.buscar(query);
	}

	// @Test
	public void testeWildcarcQuerySingle() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term termo = new Term("conteudo", "monitor?");
		Query query = new WildcardQuery(termo);
		buscador.buscar(query);
	}

	// @Test
	public void testeWildcarcQueryMultiple() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term termo = new Term("conteudo", "monitor*");
		Query query = new WildcardQuery(termo);
		buscador.buscar(query);
	}

	// @Test
	public void testeFuzzyQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term termo = new Term("conteudo", "manuel");
		FuzzyQuery query = new FuzzyQuery(termo);
		buscador.buscar(query);
	}

	// @Test
	public void testeFuzzyQueryTransf() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term termo = new Term("conteudo", "manuel");
		FuzzyQuery query = new FuzzyQuery(termo, 1);
		buscador.buscar(query);
	}

	// @Test
	public void testeFuzzyQueryPrefix() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Term termo = new Term("conteudo", "manuel");
		FuzzyQuery query = new FuzzyQuery(termo, 1, 5);
		buscador.buscar(query);
	}

	// @Test
	public void testeMatchAllDocsQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		MatchAllDocsQuery query = new MatchAllDocsQuery();
		buscador.buscar(query);
	}

	// @Test
	public void testeRangeQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query query = LongPoint.newRangeQuery("tamanhoLong", 0,
				500000);
		buscador.buscar(query);
	}

	// @Test
	public void testeSpanTermQueryOrdenada() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		//
		Term termoReforma = new Term("conteudo", "proposta");
		Term termoProposta = new Term("conteudo", "reforma");
		SpanQuery queryReforma = new SpanTermQuery(termoReforma);
		SpanQuery queryProposta = new SpanTermQuery(termoProposta);
		SpanQuery[] clausulas = new SpanQuery[] { queryProposta,
				queryReforma };
		SpanNearQuery query = new SpanNearQuery(clausulas, 5, true);
		buscador.buscar(query);
	}

	// @Test
	public void testeRegexQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		String regex = "@abc~def@"; // complemento
		regex = "@[0-9]{4}\\-[0-9]{4}@"; // telefone
		regex = "@[2-9][0-9]{3}\\-[0-9]{4}@"; // telefone BSB
		regex = "@[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}@"; // IP
		regex = ".*bug.{1,5}[0-9]{4}@"; // Bug
		regex = "@[a-z0-9\\.\\_\\%\\+\\-]+\\@[a-z0-9\\.\\-]+\\.[a-z]{2,}@"; // email
		regex = "@(0[1-9]|[1-2][0-9]|3[01])[\\- \\/\\.](0[1-9]|1[012])[\\- \\/\\.](19|20)[0-9][0-9]@"; // Data
		Term termo = new Term("conteudoNaoAnalisado", regex);
		RegexpQuery query = new RegexpQuery(termo);
		buscador.buscar(query);
	}

	@Test
	public void testeBoostQuery() {
		BuscadorArquivosLocais buscador = new BuscadorArquivosLocais();
		Query queryNuvem = new TermQuery(
				new Term("conteudo", "nuvem"));
		Query queryRede = new TermQuery(new Term("conteudo", "rede"));
		BoostQuery boostQuery = new BoostQuery(queryRede, 2);
		Query query = new BooleanQuery.Builder()
				.add(queryNuvem, Occur.SHOULD)
				.add(boostQuery, Occur.SHOULD).build();
		buscador.buscar(query);
	}
}
