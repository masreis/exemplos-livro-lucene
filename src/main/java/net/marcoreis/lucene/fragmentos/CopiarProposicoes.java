package net.marcoreis.lucene.fragmentos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

public class CopiarProposicoes {
	private Logger logger =
			Logger.getLogger(CopiarProposicoes.class);

	public void gerarXMLProposicoes()
			throws ClientProtocolException, IOException,
			ParserConfigurationException, SAXException,
			InterruptedException {
		HttpClient cliente = null;// HttpClientBuilder.create().build();
		Collection<String> siglas =
				new TiposSiglasSAXParser().recuperarSiglas();
		int ano;
		ano = 2014;
		while (ano <= 2016) {
			for (String sigla : siglas) {
				Thread.sleep(500);
				System.out.println(sigla + "-" + ano);
				String numeroProposicao = null;
				String urlServicoLista =
						getUrlServicoLista(sigla, ano);
				HttpGet requisicao =
						new HttpGet(urlServicoLista);
				HttpResponse resposta =
						cliente.execute(requisicao);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(resposta
								.getEntity().getContent()));
				String pathname = System.getProperty("user.home")
						+ "/proposicoes/"
						+ sigla.replaceAll("/", "-") + "-" + ano
						+ ".xml";
				File arquivoDestino = new File(pathname);
				//
				arquivoDestino.mkdirs();
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
						logger.warn("Sigla inválida: "
								+ urlServicoLista);
						break;
					}
					// if (linha.trim().startsWith("<id>")) {
					// numeroProposicao = linha.substring(linha.indexOf(">") +
					// 1, linha.indexOf("</id>"));
					// break;
					// }
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
		return "http://www.camara.leg.br/SitCamaraWS/Proposicoes.asmx/ListarProposicoes?sigla="
				+ sigla + "&numero=&ano=" + ano
				+ "&datApresentacaoIni=&datApresentacaoFim="
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
