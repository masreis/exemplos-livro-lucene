package net.marcoreis.lucene.capitulo_02;

import java.util.regex.Pattern;

public class TesteRegex {
    public static void main(String[] args) {
        new TesteRegex().analisar();
    }

    public void analisar() {
        String s = "angular.js 8119 4620 (61) 8119-4620";
        String regex = ".*\\d{4}\\s\\d{4}.*";
        Pattern p = Pattern.compile(regex);
        System.out.println(s.matches(regex));
    }
}
