package net.marcoreis.lucene.capitulo_03;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class UtilConvertePdtToText {
	private Tika extrator = new Tika();
	String diretorio = "/home/marco/temp/docs/";

	public UtilConvertePdtToText() {
		new File(diretorio).mkdirs();
	}

	public void analisaDiretorio(File diretorio)
			throws IOException, TikaException {
		File[] arquivosParaIndexar = diretorio.listFiles();
		for (File arquivo : arquivosParaIndexar) {
			if (arquivo.isDirectory()) {
				analisaDiretorio(arquivo);
			} else {
				converteArquivo(arquivo);
			}
		}
	}

	private void converteArquivo(File arquivo)
			throws FileNotFoundException, IOException,
			TikaException {
		String textoArquivo = "";
		try {
			textoArquivo = extrator
					.parseToString(new FileInputStream(arquivo));
		} catch (Throwable e) {
			return;
		}
		if (textoArquivo.trim().length() == 0) {
			return;
		}
		String fileName = diretorio + arquivo.getName() + ".txt";
		FileWriter writer = new FileWriter(fileName);
		writer.write(textoArquivo);
		writer.close();
	}

	public static void main(String[] args) {
		try {
			new UtilConvertePdtToText().analisaDiretorio(
					new File("/home/marco/Dropbox/"));
		} catch (IOException | TikaException e) {
			e.printStackTrace();
		}
	}
}
