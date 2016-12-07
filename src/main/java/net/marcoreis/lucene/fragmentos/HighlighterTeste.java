package net.marcoreis.lucene.fragmentos;

import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class HighlighterTeste {
	private static String DIRETORIO_INDICE = System.getProperty("user.home") + "/livro-lucene/indice";
	private static final Logger logger = Logger.getLogger(HighlighterTeste.class);

	public static void main(String[] args) {
		HighlighterTeste realcador = new HighlighterTeste();
		String consulta = "conteudo:/@[2-9][0-9]{3}\\-[0-9]{4}@/";
		consulta = "conteudoComPosicoes:java";
		realcador.realcar(consulta, "conteudoComPosicoes");
	}

	public void realcar(Query query, String campo) {
		try {
			Directory diretorio = FSDirectory.open(Paths.get(DIRETORIO_INDICE));
			IndexReader reader = DirectoryReader.open(diretorio);
			IndexSearcher searcher = new IndexSearcher(reader);
			//
			TopDocs docs = searcher.search(query, 100);
			logger.info("Query: " + query);
			logger.info("Quantidade de itens encontrados: " + docs.totalHits);
			// Scorer scorer = new QueryScorer(query);
			// Highlighter hl = new Highlighter(scorer);
			//
			FastVectorHighlighter fhl = new FastVectorHighlighter();
			FieldQuery fq = fhl.getFieldQuery(query);
			//
			for (ScoreDoc sd : docs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				String fragmentos = fhl.getBestFragment(fq, reader, sd.doc, campo,
						doc.get(campo).length());
				if (fragmentos == null) {
					fragmentos = "";
					logger.error(doc.get("caminho"));
				}
				doc.add(new StringField(campo + ".hl", fragmentos, Store.NO));
				logger.info(fragmentos);
				// Explanation explicacao = buscador.explain(query, sd.doc);
				// logger.info(explicacao.toString());
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
			realcar(query, "conteudo");
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
			realcar(mpq, "conteudo");
			//
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private void realcar(String consulta, String campo) {
		try {
			Query rq = new QueryParser("", new StandardAnalyzer()).parse(consulta);
			realcar(rq, campo);
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
