package net.marcoreis.lucene.capitulo_03;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.xml.sax.SAXException;

public class CopiarProposicoes {
    private static URL DIRETORIO_DESTINO = IndexadorProposicoesCamaraSAXParser.class
            .getClassLoader().getResource("dados/proposicoes/");

    public void gerarXML() throws ClientProtocolException, IOException,
            ParserConfigurationException, SAXException {
        HttpClient cliente = HttpClientBuilder.create().build();
        Collection<String> siglas = new TiposSiglasSAXParser()
                .recuperarSiglas();
        for (String sigla : siglas) {
            HttpGet requisicao = new HttpGet(getUrlServico(sigla, "2014"));
            HttpResponse resposta = cliente.execute(requisicao);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    resposta.getEntity().getContent()));
            File arquivoDestino = new File(DIRETORIO_DESTINO.getFile()
                    .replaceAll("target/classes", "src/main/resources")
                    + "proposicoes-" + sigla + ".xml");
            //
            arquivoDestino.delete();
            arquivoDestino.createNewFile();
            FileWriter fw = new FileWriter(arquivoDestino);
            //
            String linha = "";
            while ((linha = br.readLine()) != null) {
                if (linha.contains("CamaraWSSemComponente")) {
                    // Não tem proposicao para os parametros informados
                    arquivoDestino.delete();
                    break;
                }
                fw.write(linha);
                fw.write("\n");
            }
            br.close();
            fw.close();
        }
    }

    public String getUrlServico(String sigla, String ano) {
        return "http://www.camara.gov.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla="
                + sigla
                + "&numero=&ano="
                + ano
                + "&datApresentacaoIni=&datApresentacaoFim="
                + "&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor="
                + "&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=";
    }

    public static void main(String[] args) {
        try {
            new CopiarProposicoes().gerarXML();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
    }
}
