package net.marcoreis.lucene.fragmentos;

public class ExpressaoRegular {
	public static void main(String[] args) {
		String s = "Marco Re-\nis blablabla...";
		String regex = "\\-\\n";
		String t = s.replaceAll(regex, "");
		System.out.println(t);
	}
}
