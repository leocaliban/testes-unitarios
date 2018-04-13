package com.leocaliban.unit_test.builders;

import com.leocaliban.unit_test.entidades.Usuario;

public class UsuarioBuilder {

	private Usuario usuario;

	private UsuarioBuilder() {
	}
	
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder();
		builder.usuario = new Usuario();
		builder.usuario.setNome("Jack Bauer");
		return builder;
		
	}
	
	public UsuarioBuilder comNome(String nome) {
		usuario.setNome(nome);
		return this;
	}
	
	public Usuario agora() {
		return usuario;
	}
	
	
}
