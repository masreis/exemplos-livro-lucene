package net.marcoreis.lucene.fragmentos;

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
	private static URL DIRETORIO_DESTINO = IndexadorProposicoesCamaraSAXParser.class.getClassLoader()
			.getResource("dados/proposicoes/");

	public void gerarXMLProposicoes() throws ClientProtocolException, IOException, ParserConfigurationException,
			SAXException, InterruptedException {
		HttpClient cliente = HttpClientBuilder.create().build();
		Collection<String> siglas = new TiposSiglasSAXParser().recuperarSiglas();
		int ano;
		ano = 2014;
		while (ano <= 2016) {
			for (String sigla : siglas) {
				Thread.sleep(1000);
				System.out.println(sigla + "-" + ano);
				String numeroProposicao = null;
				HttpGet requisicao = new HttpGet(getUrlServicoLista(sigla, ano));
				HttpResponse resposta = cliente.execute(requisicao);
				BufferedReader br = new BufferedReader(new InputStreamReader(resposta.getEntity().getContent()));
				File arquivoDestino = new File(
						DIRETORIO_DESTINO.getFile().replaceAll("target/classes", "src/main/resources") + "proposicoes-"
								+ sigla.replaceAll("/", "-") + "-" + ano + ".xml");
				//
				arquivoDestino.delete();
				arquivoDestino.createNewFile();
				FileWriter fw = new FileWriter(arquivoDestino);
				//
				String linha = "";
				br.readLine();// Descarta a primeira linha do cabeçalho
				while ((linha = br.readLine()) != null) {
					if (linha.startsWith("<erro>")) {
						// Não tem proposicao para os parametros informados
						arquivoDestino.delete();
						break;
					}
					if (linha.trim().startsWith("<numero>")) {
						numeroProposicao = linha.substring(linha.indexOf(">") + 1, linha.indexOf("</numero>"));
						break;
					}
					fw.write(linha);
					fw.write("\n");
				}
				br.close();
				fw.close();
			}
			ano++;
		}
	}

	public String getUrlServicoLista(String sigla, int ano) {
		return "http://www.camara.leg.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla=" + sigla
				+ "&numero=&ano=" + ano + "&datApresentacaoIni=&datApresentacaoFim="
				+ "&idTipoAutor=&parteNomeAutor=&siglaPartidoAutor="
				+ "&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=";
		// http://www.camara.leg.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla=PL&numero=&ano=2011&datApresentacaoIni=14/11/2011&datApresentacaoFim=16/11/2011&parteNomeAutor=&idTipoAutor=&siglaPartidoAutor=&siglaUFAutor=&generoAutor=&codEstado=&codOrgaoEstado=&emTramitacao=
	}

	public static void main(String[] args) {
		try {
			new CopiarProposicoes().gerarXMLProposicoes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
