package com.leocaliban.unit_test.builders;

import com.leocaliban.unit_test.entidades.Filme;

public class FilmeBuilder {
	
	private Filme filme;
	
	private FilmeBuilder() {
		
	}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Piratas Do Caribe");
		builder.filme.setPrecoLocacao(4.0);
		return builder;
	}
	
	//Indicado criar um builder personalizado quando um teste requerer muitas alterações no builder padrão
	public static FilmeBuilder umFilmeSemEstoque() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(0);
		builder.filme.setNome("Piratas Do Caribe");
		builder.filme.setPrecoLocacao(4.0);
		return builder;
	}
	
	public Filme agora() {
		return filme;
	}
	
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this;
	}
	
	public FilmeBuilder comValor(Double valor) {
		filme.setPrecoLocacao(valor);
		return this;
	}

}
