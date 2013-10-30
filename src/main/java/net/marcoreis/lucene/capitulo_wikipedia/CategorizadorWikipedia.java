package net.marcoreis.lucene.capitulo_wikipedia;

import java.sql.ResultSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class CategorizadorWikipedia {
    private static Logger logger = Logger
	    .getLogger(CategorizadorWikipedia.class);
    private Pattern pattern = Pattern.compile(".*\\[\\[Categoria:(.*)\\]\\].*");
    private DAOCategoriaWikipedia daoCategoria = new DAOCategoriaWikipedia();
    private DAOPaginaWikipedia daoPagina = new DAOPaginaWikipedia();

    public void criarCategorias() {
	try {
	    int quantidadeRegistrosAnalisados = 0;
	    ResultSet rs = daoPagina.findAll();
	    while (rs.next()) {
		String texto = rs.getString("text");
		Matcher matcher = pattern.matcher(texto);
		//
		while (matcher.find()) {
		    String categoria = matcher.group(1);
		    //
		    if (categoria.contains("|")) {
			String subCategorias[] = categoria.split("\\|");
			for (String subCategoria : subCategorias) {
			    if (subCategoria.length() > 0)
				daoCategoria.inserir(new CategoriaWikipedia(
					categoria.trim()));
			}

		    } else {
			//
			CategoriaWikipedia categoriaWikipedia = new CategoriaWikipedia(
				categoria.trim());
			daoCategoria.inserir(categoriaWikipedia);
		    }
		}
		quantidadeRegistrosAnalisados++;
	    }
	    rs.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    public static void main(String[] args) {
	CategorizadorWikipedia c = new CategorizadorWikipedia();
	c.criarCategorias();
    }
}
