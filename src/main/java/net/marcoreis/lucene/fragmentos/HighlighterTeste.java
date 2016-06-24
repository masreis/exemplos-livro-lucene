package net.marcoreis.lucene.fragmentos;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class HighlighterTeste {
	private static String DIRETORIO_INDICE = System.getProperty("user.home")
			+ "/livro-lucene/indice-capitulo-02-exemplo-01";
	private static final Logger logger = Logger.getLogger(HighlighterTeste.class);

	public static void main(String[] args) {
		HighlighterTeste buscador = new HighlighterTeste();
		buscador.highlightFuzzyQuery();
	}

	public void highlight(Query query, String campo) {
		try {
			Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher buscador = new IndexSearcher(reader);
			//
			TopDocs docs = buscador.search(query, 100);
			logger.info("Query: " + query);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			Scorer scorer = new QueryScorer(query);
			Highlighter hl = new Highlighter(scorer);
			//
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = buscador.doc(sd.doc);
				String fragmentos = hl.getBestFragment(new StandardAnalyzer(), campo, doc.get("conteudo"));
				logger.info(fragmentos);
				// Explanation explicacao = buscador.explain(query, sd.doc);
				// logger.info(explicacao.toString());
				logger.info(doc.get("nome"));
				logger.info(doc.get("dataAtualizacao"));
				// String[] comissoes = doc.getValues("comissao");
				// for (String comissao : comissoes) {
				// logger.info(comissao);
				// }
			}
			//
			reader.close();
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void highlightQueryParser() {
		try {
			String consulta = "";
			consulta = "nome:josé";
			consulta = "comissao:(comissão AND lei)";
			consulta = "comissao:\"reforma proposta\"~10";
			consulta = "conteudo:\"(aplicação exemplo) OR (banco dados)\"~1";
			consulta = "conteudo:idioma AND conteudo:java ";
			consulta = "conteudo:(+java +cdi) AND dataAtualizacao:[2013-01-01 TO 2014-12-31]";
			consulta = "conteudo:/[vc]alor/";
			consulta = "conteudo:manuel~1";
			consulta = "conteudo:aplicação";
			QueryParser qp = new QueryParser("", new StandardAnalyzer());
			Query query = qp.parse(consulta);
			highlight(query, "conteudo");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void highlightMultiPhraseQuery() {
		try {
			//
			Term[] termosAplicacaoExemplo = new Term[] { new Term("conteudo", "aplicação"),
					new Term("conteudo", "exemplo") };
			Term[] termosBancoDados = new Term[] { new Term("conteudo", "banco"), new Term("conteudo", "dados") };
			//
			MultiPhraseQuery mpq = null;// new MultiPhraseQuery();
			// mpq.add(termosAplicacaoExemplo);
			// mpq.add(termosBancoDados);
			// mpq.setSlop(2);
			//
			highlight(mpq, "conteudo");
			//
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void highlightRegexQuery() {
		try {
			//
			String regex = ".*ler\\scódigo.*";
			regex = ".*http[s].*";
			regex = ".*[0-9]{4}[^0-9][0-9]{4}.*";
			regex = ".*[0-9]{2}:[0-9]{2},847.*";
			regex = "bug(\\s){0,1}[0-9]{4}";
			regex = "[a-z]{3}\\|[0-9]{4}";
			// Term termo = new Term("conteudo", regex);
			String sQuery = "conteudo:/[0-9]{4}/";
			sQuery = "conteudo:/8.../";
			Query rq = null;
			rq = new QueryParser("", new StandardAnalyzer()).parse(sQuery);
			// rq = new RegexpQuery(termo);
			// RegexQuery rq = new RegexQuery(termo);
			highlight(rq, "conteudo");
		} catch (Exception e) {
			logger.error(e);
		}
	}

	public void highlightFuzzyQuery() {
		try {
			//
			Term termo = new Term("conteudo", "seção");
			FuzzyQuery fq = new FuzzyQuery(termo, 2, 2);
			//
			highlight(fq, "conteudo");
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
