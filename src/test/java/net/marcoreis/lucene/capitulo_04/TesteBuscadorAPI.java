package net.marcoreis.lucene.capitulo_04;

import org.apache.log4j.Logger;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.spans.SpanNearQuery;
import org.apache.lucene.search.spans.SpanQuery;
import org.junit.Test;

import net.marcoreis.lucene.capitulo_03.BuscadorArquivosLocais;

public class TesteBuscadorAPI {
	private static final Logger logger = Logger.getLogger(TesteBuscadorAPI.class);

	@Test
	public void testeQuery() {
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
		Term[] termosAplicacaoExemplo = new Term[] { new Term("conteudo", "aplicação"),
				new Term("conteudo", "exemplo") };
		Term[] termosBancoDados = new Term[] { new Term("conteudo", "banco"), new Term("conteudo", "dados") };
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
