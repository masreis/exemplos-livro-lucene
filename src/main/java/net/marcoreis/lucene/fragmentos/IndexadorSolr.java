package net.marcoreis.lucene.fragmentos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;

import com.google.gson.JsonObject;

public class IndexadorSolr {
    private static final String APPLICATION_JSON = "application/json";
    private static final String APPLICATION_XML = "application/XML";
    private static String diretorioDocumentosLocais = System
	    .getProperty("user.home") + "/Dropbox/";
    private static final Logger logger = LogManager.getLogger(IndexadorSolr.class);
    private Tika extrator = new Tika();

    public static void main(String[] args) {
	logger.info("Iniciando processamento dos arquivos");
	IndexadorSolr indexadorSolr = new IndexadorSolr();
	indexadorSolr.processarDiretorio(new File(diretorioDocumentosLocais));
	indexadorSolr.commit();
	logger.info("Indice gerado com sucesso");
    }

    /**
     * Indexa os arquivos de um diretório raíz e seus subdiretórios.
     * 
     * @param diretorio
     *            - diretório raíz que será indexado.
     * 
     */
    private void processarDiretorio(File diretorio) {
	File[] arquivosParaIndexar = diretorio.listFiles();
	for (File arquivo : arquivosParaIndexar) {
	    if (arquivo.isDirectory()) {
		processarDiretorio(arquivo);
	    } else if (arquivo.isFile()) {
		indexarArquivo(arquivo);
	    }
	}
    }

    private void commit() {
	try {
	    String comando = "<commit/>";
	    enviarDadosServidorSolr(comando, APPLICATION_XML);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public void indexarArquivo(File arquivo) {
	try {
	    Date dataAtualizacao = new Date(arquivo.lastModified());
	    String formatoUTC = "yyyy-MM-dd'T'hh:mm:ss'Z'";
	    SimpleDateFormat sdf = new SimpleDateFormat(formatoUTC);
	    String dataFormatada = sdf.format(dataAtualizacao);
	    String textoArquivo = extrator.parseToString(new FileInputStream(
		    arquivo));
	    //
	    JsonObject json = new JsonObject();
	    json.addProperty("conteudo", textoArquivo);
	    json.addProperty("tamanho", String.valueOf(arquivo.length()));
	    json.addProperty("dataAtualizacao", dataFormatada);
	    json.addProperty("caminho", arquivo.getAbsolutePath());
	    json.addProperty("nome", arquivo.getName());
	    String doc = "[" + json.toString() + "]";
	    //
	    enviarDadosServidorSolr(doc, APPLICATION_JSON);
	    //
	    // logger.info("Arquivo indexado: " + arquivo.getAbsolutePath());
	} catch (Exception e) {
	    logger.error(e);
	}

    }

    private void enviarDadosServidorSolr(String doc, String contentType)
	    throws MalformedURLException, IOException, ProtocolException {
	String sUrl = "http://localhost:8983/solr/arquivos-locais-core/update";
	URL url = new URL(sUrl);
	HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	urlc.setRequestMethod("POST");
	urlc.setDoOutput(true);
	urlc.setDoInput(true);
	urlc.setUseCaches(false);
	urlc.setAllowUserInteraction(false);
	urlc.setRequestProperty("Content-type", contentType);
	//
	// Escreve os dados no servidor
	InputStream dados = new ByteArrayInputStream(doc.getBytes());
	OutputStream saidaServidor = urlc.getOutputStream();
	IOUtils.copy(dados, saidaServidor);
	saidaServidor.flush();
	// logger.info(urlc.getResponseMessage());
	//
	//
	InputStream in = urlc.getInputStream();
	IOUtils.copy(in, saidaServidor);
	saidaServidor.close();
	urlc.disconnect();
    }
}
