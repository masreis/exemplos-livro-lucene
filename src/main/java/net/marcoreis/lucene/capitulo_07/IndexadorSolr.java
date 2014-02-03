package net.marcoreis.lucene.capitulo_07;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
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
		    String formatoUTC = "yyyy-MM-dd'T'hh:mm:ss'Z'";
		    SimpleDateFormat sdf = new SimpleDateFormat(formatoUTC);
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
		    String sUrl = "http://localhost:8983/solr/arquivos-locais-core/update";
		    URL url = new URL(sUrl);
		    HttpURLConnection urlc = (HttpURLConnection) url
			    .openConnection();
		    urlc.setRequestMethod("POST");
		    urlc.setDoOutput(true);
		    urlc.setDoInput(true);
		    urlc.setUseCaches(false);
		    urlc.setAllowUserInteraction(false);
		    urlc.setRequestProperty("Content-type", "application/json");
		    String doc = "[" + json.toString() + "]";
		    InputStream input = new ByteArrayInputStream(doc.getBytes());
		    OutputStream output = urlc.getOutputStream();
		    IOUtils.copy(input, output);
		    output.close();
		    logger.info(urlc.getResponseMessage());
		    //
		    InputStream in = urlc.getInputStream();
		    IOUtils.copy(in, output);
		    urlc.disconnect();
		    //
		    logger.info("Arquivo indexado: "
			    + arquivo.getAbsolutePath());
		} catch (Exception e) {
		    logger.error("Nao foi possivel indexar o arquivo "
			    + arquivo, e);
		}
	    }
	    String sUrl = "http://localhost:8983/solr/arquivos-locais-core/update?commit=true";
	    URL url = new URL(sUrl);
	    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	    urlc.disconnect();
	    logger.info("Indice gerado com sucesso");
	} catch (Exception e) {
	    logger.error(e);
	}
    }
}
