package net.marcoreis.lucene.capitulo_05;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.util.AttributeImpl;

public class AnalisadorDeTermos {
	private static final Logger logger = LogManager
			.getLogger(AnalisadorDeTermos.class);

	public static void analisarFrase(Analyzer analyzer,
			String texto) {
		try {
			TokenStream stream = analyzer.tokenStream(null,
					new StringReader(texto));
			stream.reset();
			StringBuilder termos = new StringBuilder();
			termos.append(analyzer.getClass().getSimpleName());
			termos.append(" => ");
			while (stream.incrementToken()) {
				Iterator<AttributeImpl> ite = stream
						.getAttributeImplsIterator();
				AttributeImpl impl = ite.next();
				termos.append(impl);
				termos.append("|");
			}
			logger.info(termos);
		} catch (IOException e) {
			logger.error(e);
		}
	}
}
