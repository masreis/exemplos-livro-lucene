package net.marcoreis.lucene.capitulo_wikipedia;

public class CategoriaWikipedia {
    private Long id;
    private String descricao;

    public CategoriaWikipedia(String descricao) {
	this.descricao = descricao;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Long getId() {
	return id;
    }

    public void setDescricao(String descricao) {
	this.descricao = descricao;
    }

    public String getDescricao() {
	return descricao;
    }

}
