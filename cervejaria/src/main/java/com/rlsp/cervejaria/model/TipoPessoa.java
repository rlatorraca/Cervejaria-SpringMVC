package com.rlsp.cervejaria.model;

public enum TipoPessoa {

	FISICA("Pessoa Fíica"),
	JURIDICA("Pessoa Jurídica");
	
	TipoPessoa(String descricao) {
		this.descricao = descricao;
	}
	
	private String descricao;

	public String getDescricao() {
		return descricao;
	}
}
