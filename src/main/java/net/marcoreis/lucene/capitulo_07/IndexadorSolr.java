package net.marcoreis.lucene.capitulo_07;

import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;

import com.google.gson.JsonObject;

public class IndexadorSolr {
    private static String diretorioDocumentosLocais = System
	    .getProperty("user.home") + "/Dropbox/entrada";
    private static final Logger logger = Logger.getLogger(IndexadorSolr.class);

    public static void main(String[] args) {
	new IndexadorSolr().processar();
    }

    public void processar() {
	try {
	    //
	    Tika extrator = new Tika();
	    File[] arquivosParaIndexar = new File(diretorioDocumentosLocais)
		    .listFiles();
	    for (File arquivo : arquivosParaIndexar) {
		if (!arquivo.isFile())
		    continue;
		try {
		    //
		    Date dataAtualizacao = new Date(arquivo.lastModified());
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		    String dataFormatada = sdf.format(dataAtualizacao);
		    String textoArquivo = extrator
			    .parseToString(new FileInputStream(arquivo));
		    //
		    JsonObject json = new JsonObject();
		    json.addProperty("conteudo", textoArquivo);
		    json.addProperty("tamanho",
			    String.valueOf(arquivo.length()));
		    json.addProperty("dataAtualizacao", dataFormatada);
		    json.addProperty("caminho", arquivo.getAbsolutePath());
		    json.addProperty("nome", arquivo.getName());
		    //
		    URL url = new URL("http://localhost:8983/solr/update ");
		    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
		    //
		    logger.info("Arquivo indexado: "
			    + arquivo.getAbsolutePath());
		} catch (Exception e) {
		    logger.error("Nao foi possivel indexar o arquivo "
			    + arquivo, e);
		}
	    }
	    logger.info("Indice gerado com sucesso");
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
