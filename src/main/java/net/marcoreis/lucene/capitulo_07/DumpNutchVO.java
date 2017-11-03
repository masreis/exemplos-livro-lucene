package net.marcoreis.lucene.capitulo_07;

import java.util.List;

public class DumpNutchVO {
	private String recno;
	private List<String> crawlDatum;
	private String parseData;
	private String parseText;
	private String url;
	private String content;

	public String getRecno() {
		return recno;
	}

	public void setRecno(String recno) {
		this.recno = recno;
	}

	public List<String> getCrawlDatum() {
		return crawlDatum;
	}

	public void setCrawlDatum(List<String> crawlDatum) {
		this.crawlDatum = crawlDatum;
	}

	public String getParseData() {
		return parseData;
	}

	public void setParseData(String parseData) {
		this.parseData = parseData;
	}

	public String getParseText() {
		return parseText;
	}

	public void setParseText(String parseText) {
		this.parseText = parseText;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
