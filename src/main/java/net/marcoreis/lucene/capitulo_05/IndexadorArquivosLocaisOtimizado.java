package net.marcoreis.lucene.capitulo_05;

import java.io.File;
import java.io.FileInputStream;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.BytesRef;

import net.marcoreis.lucene.capitulo_03.IndexadorArquivosLocais;

public class IndexadorArquivosLocaisOtimizado
		extends IndexadorArquivosLocais {
	private static final Logger logger = Logger
			.getLogger(IndexadorArquivosLocaisOtimizado.class);
	Document doc = new Document();
	TextField tfConteudo = new TextField("conteudo", "",
			Store.YES);
	TextField tfTamanho = new TextField("tamanho", "",
			Store.YES);
	LongPoint tfTamanhoLong = new LongPoint("tamanhoLong", 0);
	StringField tfData = new StringField("data", "", Store.YES);
	StringField tfCaminho = new StringField("caminho", "",
			Store.YES);
	StringField tfExtensao = new StringField("extensao", "",
			Store.YES);
	StringField tfConteudoNaoAnalisado = new StringField(
			"conteudoNaoAnalisado", "", Store.YES);

	public void indexarArquivo(File arquivo) {
		try {
			doc.clear();
			Date dataModificacao = new Date(
					arquivo.lastModified());
			String dataParaIndexacao = DateTools.dateToString(
					dataModificacao, Resolution.DAY);
			String extensao = consultarExtensaoArquivo(
					arquivo.getName());
			String textoArquivo = "";
			//
			try {
				textoArquivo = extrator.parseToString(
						new FileInputStream(arquivo));
			} catch (Throwable e) {
				logger.error(e);
			}
			int tamanhoMaximo = 30000;
			if (textoArquivo.length() >= tamanhoMaximo) {
				tfConteudoNaoAnalisado.setBytesValue(textoArquivo
						.substring(0, tamanhoMaximo).getBytes());
			} else {
				tfConteudoNaoAnalisado
						.setBytesValue(textoArquivo.getBytes());
			}
			tfConteudo.setBytesValue(
					new BytesRef(textoArquivo.getBytes()));
			tfTamanho.setLongValue(arquivo.length());
			tfCaminho.setBytesValue(
					arquivo.getAbsolutePath().getBytes());
			tfData.setBytesValue(dataParaIndexacao.getBytes());
			tfExtensao.setBytesValue(extensao.getBytes());
			tfTamanhoLong.setLongValue(arquivo.length());
			//
			doc.add(tfCaminho);
			doc.add(tfConteudo);
			doc.add(tfConteudoNaoAnalisado);
			doc.add(tfData);
			doc.add(tfExtensao);
			doc.add(tfTamanho);
			doc.add(tfTamanhoLong);
			//
			writer.addDocument(doc);
			logger.info("Arquivo indexado ("
					+ (arquivo.length() / 1024) + " kb): "
					+ arquivo);
			totalArquivosIndexados++;
			totalBytesIndexados += arquivo.length();
		} catch (Exception e) {
			logger.error("Não foi possível processar o arquivo "
					+ arquivo.getAbsolutePath());
			logger.error(e);
		}
	}
}
