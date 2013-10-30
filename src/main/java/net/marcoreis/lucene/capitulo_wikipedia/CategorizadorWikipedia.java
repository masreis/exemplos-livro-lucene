package net.marcoreis.lucene.capitulo_wikipedia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class CategorizadorWikipedia {
    private static Logger logger = Logger
	    .getLogger(CategorizadorWikipedia.class);
    private SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private Connection conexao;
    private String pwd = "";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";
    private Pattern pattern = Pattern.compile(".*\\[\\[Categoria:(.*)\\]\\].*");
    private Set<String> categorias = new HashSet<String>();

    public CategorizadorWikipedia() {
	try {
	    Class.forName(driver);
	    conexao = DriverManager.getConnection(url, user, pwd);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void criarCategorias() {
	try {
	    int quantidadeRegistrosAnalisados = 0;
	    String sql = "select id, text from PaginaWikipedia";
	    PreparedStatement pstmt = conexao.prepareStatement(sql);
	    ResultSet rs = pstmt.executeQuery();
	    while (rs.next()) {
		String texto = rs.getString("text");
		Matcher matcher = pattern.matcher(texto);
		while (matcher.find()) {
		    String categoria = matcher.group(1);
		    if (categoria.contains("|")) {
			String subCategorias[] = categoria.split("\\|");
			for (String subCategoria : subCategorias) {
			    if (subCategoria.length() > 0)
				categorias.add(subCategoria.trim());
			}
		    } else {
			categorias.add(categoria.trim());
		    }
		}
		quantidadeRegistrosAnalisados++;
	    }
	    rs.close();
	    pstmt.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    private void inserirCategorias() {
	try {
	    String sql = "insert into categoria (nome) values (?)";
	    PreparedStatement pstmt = conexao.prepareStatement(sql);
	    for (String categoria : categorias) {
		pstmt.setString(1, categoria);
		pstmt.executeUpdate();
	    }
	    pstmt.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    public static void main(String[] args) {
	CategorizadorWikipedia c = new CategorizadorWikipedia();
	c.criarCategorias();
	c.inserirCategorias();
    }
}
