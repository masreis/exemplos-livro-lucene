package net.marcoreis.lucene.capitulo_07;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;

public class NutchParser {
	private static final Logger logger =
			Logger.getLogger(NutchParser.class);
	protected Tika extrator = new Tika();

	public List<DumpNutchVO> parse(File arquivoDump)
			throws IOException {
		List<DumpNutchVO> lista = new ArrayList<DumpNutchVO>();
		InputStream is = new FileInputStream(arquivoDump);
		String textoArquivo = "";
		try {
			textoArquivo = extrator.parseToString(is);
		} catch (Throwable e) {
			logger.error(e);
		} finally {
			is.close();
		}
		String[] paginas = textoArquivo.split("Recno::");
		for (String conteudo : paginas) {
			if (conteudo.contains("Content::")) {
				lista.add(criaVO(conteudo));
			}
		}
		return lista;
	}

	private DumpNutchVO criaVO(String conteudo) {
		DumpNutchVO vo = new DumpNutchVO();
		int inicioConteudo = conteudo.indexOf("Content::");
		String conteudoCompleto =
				conteudo.substring(inicioConteudo);
		Pattern p = Pattern.compile("(url:\\s)(.*)");
		Matcher m = p.matcher(conteudoCompleto);
		String url = null;
		if (m.find()) {
			url = m.group(2);
		}
		int inicioHtml = conteudoCompleto.indexOf("Content:\n");
		String conteudoHtml =
				conteudoCompleto.substring(inicioHtml);
		vo.setUrl(url);
		vo.setContent(conteudoHtml);
		return vo;
	}

}
