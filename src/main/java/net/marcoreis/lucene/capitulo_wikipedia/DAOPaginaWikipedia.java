package net.marcoreis.lucene.capitulo_wikipedia;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

public class DAOPaginaWikipedia {
    private static Logger logger = Logger.getLogger(DAOPaginaWikipedia.class);
    private Connection conexao;
    private String pwd = "";
    private String user = "root";
    private String url = "jdbc:mysql://localhost:3306/db_wikipedia";
    private String driver = "com.mysql.jdbc.Driver";

    public DAOPaginaWikipedia() {
	try {
	    Class.forName(driver);
	    conexao = DriverManager.getConnection(url, user, pwd);
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void inserir(PaginaWikipedia pagina) {
	try {
	    String sql = "insert into PaginaWikipedia (id, title, timestamp, username, text, model, format, comment) values (?,?,?,?,?,?,?,?,?)";
	    PreparedStatement pstmt = conexao.prepareStatement(sql);
	    pstmt.setLong(1, pagina.getId());
	    pstmt.setString(2, pagina.getTitle());
	    Date data = new Date(pagina.getTimeStamp().getTime());
	    pstmt.setTimestamp(3, new Timestamp(data.getTime()));
	    pstmt.setString(4, pagina.getUserName());
	    pstmt.setString(5, pagina.getText());
	    pstmt.setString(6, pagina.getModel());
	    pstmt.setString(7, pagina.getFormat());
	    pstmt.setString(8, pagina.getComment());
	    int qtd = pstmt.executeUpdate();
	    if (qtd != 1) {
		logger.error("Registro não incluido -> " + pagina.getId());
	    }
	    pstmt.close();
	} catch (Exception e) {
	    logger.error(e);
	}
    }

    public ResultSet findAll() {
	try {
	    String sql = "select id, text from PaginaWikipedia";
	    PreparedStatement pstmt = conexao.prepareStatement(sql);
	    ResultSet rs = pstmt.executeQuery();
	    pstmt.close();
	    return rs;
	} catch (Exception e) {
	    throw new RuntimeException(e);
	}
    }

    public void fechar() throws SQLException {
	conexao.close();
    }
}
