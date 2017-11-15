package net.marcoreis.lucene.fragmentos;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;

public class TesteSinonimo extends TokenFilter {

    protected TesteSinonimo(TokenStream input) {
        super(input);
        // TODO Auto-generated constructor stub
    }

    public boolean incrementToken() throws IOException {
        return false;
    }

}
